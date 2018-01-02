package com.playtika.carproxy.web;

import com.playtika.carproxy.carshop.CarShopRestClient;
import com.playtika.carproxy.domain.Car;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarProxyControllerSystemTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private CarShopRestClient client;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void ifCarDealIsAddedReturnOkStatus() throws Exception {
        when(client.addCarDeals(new Car("red", "HK23TT"), 23000, "hr"))
                .thenReturn(new ResponseEntity<>(1L, HttpStatus.OK));
        mockMvc.perform(post("/cardeals")
                .param("fileUrl", "src/test/resources/cardeal.csv")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(("application/json;charset=UTF-8")));
    }

    @Test
    public void ifCarDealWasAlreadyAddedReturnOkStatus() throws Exception {
        when(client.addCarDeals(new Car("red", "HK23TT"), 23000, "hr"))
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        mockMvc.perform(post("/cardeals")
                .param("fileUrl", "src/test/resources/cardeal.csv")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk());
    }
}
