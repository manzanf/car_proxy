package com.playtika.carproxy.web;

import com.playtika.carproxy.carshop.CarShopRestClient;
import com.playtika.carproxy.domain.Car;
import com.playtika.carproxy.domain.ClosingCarDealResponse;
import lombok.extern.slf4j.Slf4j;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
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

    @Test
    public void ifPurchaseClaimIsAddedReturnOkStatus() throws Exception {
        when(client.addPurchase(2L, 23000))
                .thenReturn(new ResponseEntity<>(1L, HttpStatus.OK));
        mockMvc.perform(post("/purchase")
                .param("carDealId", "2")
                .param("price", "23000")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(("application/json;charset=UTF-8")));
    }

    @Test
    public void ifPurchaseClaimWasNotAddedReturnNotModifiedStatus() throws Exception {
        when(client.addPurchase(2L, 23000))
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        mockMvc.perform(post("/purchase")
                .param("carDealId", "2")
                .param("price", "23000")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isNotModified());
    }

    @Test
    public void ifPurchaseClaimWasRejectedReturnOkStatus() throws Exception {
        when(client.rejectPurchaseClaim(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(post("/purchase/reject")
                .param("purchaseClaimId", "1")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    public void ifPurchaseClaimWasNotRejectedReturnNotFoundStatus() throws Exception {
        when(client.rejectPurchaseClaim(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        mockMvc.perform(post("/purchase/reject")
                .param("purchaseClaimId", "1")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void bestBidCouldBeReturned() throws Exception {
        when(client.acceptBestPurchaseClaim(1L)).thenReturn(new ClosingCarDealResponse(10L, "ACCEPTED"));
        String price = mockMvc.perform(get("/cardeals/bestBid")
                .param("carDealId", "1")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(price, is(equalTo("10")));
    }
}
