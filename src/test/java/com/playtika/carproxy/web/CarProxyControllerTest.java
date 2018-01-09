package com.playtika.carproxy.web;

import com.playtika.carproxy.service.CarProxyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class CarProxyControllerTest {
    @Mock
    private CarProxyService service;

    private CarProxyController controller;
    private MockMvc mockMvc;
    private List<Long> addedCarDealsId = Arrays.asList(1L, 2L);

    @Before
    public void setUp() throws Exception {
        controller = new CarProxyController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void сarDealsShouldBeAdded() throws Exception {
        when(service.processCarDealsFile("cardeals.csv")).thenReturn(addedCarDealsId);
        String ids = mockMvc.perform(post("/cardeals")
                .param("fileUrl", "cardeals.csv")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(("application/json;charset=UTF-8")))
                .andReturn().getResponse().getContentAsString();
        //Assert.assertThat(Long.parseLong(ids), is(equalTo(addedCarDealsId)));
        Assert.assertThat(ids, is(equalTo("[1,2]")));
    }

    @Test
    public void ifFileIsNotPassedInRequestBadRequestExceptionShouldBeThrown() throws Exception {
        mockMvc.perform(post("/cardeals")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void ifFilePathIsIncorrectFileNotFoundExceptionShouldBeThrown() throws Exception {
        mockMvc.perform(post("/cardeals")
                .param("fileUrl", "smth")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isInternalServerError())   //!! map to smth better
                .andExpect(status().reason("There is no file for this path"));
    }

    //test for corrupted file
    //test for file without header
}