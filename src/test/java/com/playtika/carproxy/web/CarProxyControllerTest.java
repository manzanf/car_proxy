package com.playtika.carproxy.web;

import com.playtika.carproxy.domain.ClosingCarDealResponse;
import com.playtika.carproxy.service.CarProxyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.rmi.NoSuchObjectException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void ifFileWasNotPassedInRequestReturnBadRequestStatus() throws Exception {
        mockMvc.perform(post("/cardeals")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void purchaseClaimShouldBeAdded() throws Exception {
        when(service.addPurchase(2L, 23000)).thenReturn(1L);
        String id = mockMvc.perform(post("/purchase")
                .param("carDealId", "2")
                .param("price", "23000")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(("application/json;charset=UTF-8")))
                .andReturn().getResponse().getContentAsString();
        assertThat(id, is(equalTo("1")));
    }

    @Test
    public void ifCarDealIdWasNotPassedInRequestReturnBadRequestStatus() throws Exception {
        mockMvc.perform(post("/purchase")
                .param("price", "23000")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void ifPriceWasNotPassedInRequestReturnBadRequestStatus() throws Exception {
        mockMvc.perform(post("/purchase")
                .param("carDealId", "2")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void ifPurchaseClaimWasNotAddedReturnNotModifiedStatus() throws Exception {
        when(service.addPurchase(2L, 23000)).thenThrow(NoSuchObjectException.class);
        mockMvc.perform(post("/purchase")
                .param("carDealId", "2")
                .param("price", "23000")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isNotModified());
    }

    @Test
    public void purchaseClaimCouldBeRejected() throws Exception {
        when(service.rejectPurchaseClaim(1L)).thenReturn(true);
        mockMvc.perform(post("/purchase/reject")
                .param("purchaseClaimId", "1")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    public void ifPurchaseClaimWasNotRejectedReturnNotFoundStatus() throws Exception {
        when(service.rejectPurchaseClaim(1L)).thenReturn(false);
        mockMvc.perform(post("/purchase/reject")
                .param("purchaseClaimId", "1")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void ifPurchaseClaimIdWasNotPassedReturnBadRequestStatus() throws Exception {
        mockMvc.perform(post("/purchase/reject")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void bestBidCouldBeReturned() throws Exception {
        when(service.acceptBestPurchaseClaim(1L)).thenReturn(new ClosingCarDealResponse(10L, "ACCEPTED"));
        String price = mockMvc.perform(get("/cardeals/bestBid")
                .param("carDealId", "1")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(price, is(equalTo("10")));
    }

    @Test
    public void ifCarDealWasNotFoundReturnNotFoundStatus() throws Exception {
        when(service.acceptBestPurchaseClaim(1L)).thenReturn(new ClosingCarDealResponse("NO_CAR_DEAL"));
        mockMvc.perform(get("/cardeals/bestBid")
                .param("carDealId", "1")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void ifPurchaseClaimsForTheCarDealWasNotFoundReturnNotFoundStatus() throws Exception {
        when(service.acceptBestPurchaseClaim(1L)).thenReturn(new ClosingCarDealResponse("NO_PURCHASE_CLAIMS"));
        mockMvc.perform(get("/cardeals/bestBid")
                .param("carDealId", "1")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void ifCarDealWasAlreadyClosedReturnOkStatus() throws Exception {
        when(service.acceptBestPurchaseClaim(1L)).thenReturn(new ClosingCarDealResponse("ALREADY_CLOSED_CAR_DEAL"));
        mockMvc.perform(get("/cardeals/bestBid")
                .param("carDealId", "1")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    public void ifCarDealIdWasNotPassedReturnBadRequestStatus() throws Exception {
        mockMvc.perform(get("/cardeals/bestBid")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }
}