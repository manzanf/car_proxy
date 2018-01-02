package com.playtika.carproxy.web;

import com.playtika.carproxy.service.CarProxyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CarProxyControllerTest {
    @Mock
    private CarProxyService service;

    private CarProxyController controller;
    private MockMvc mockMvc;
    private List<Long> addedCarDealsId = asList(1L, 2L);

    @Before
    public void setUp() throws Exception {
        controller = new CarProxyController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void carDealsShouldBeAdded() throws Exception {
        when(service.processCarDealsFile("cardeals.csv")).thenReturn(addedCarDealsId);
        String ids = mockMvc.perform(post("/cardeals")
                .param("fileUrl", "cardeals.csv")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(("application/json;charset=UTF-8")))
                .andReturn().getResponse().getContentAsString();
        assertThat(ids, is(equalTo("[1,2]")));
    }

    @Test
    public void ifFileIsNotPassedInRequestBadRequestExceptionShouldBeThrown() throws Exception {
        mockMvc.perform(post("/cardeals")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }
}