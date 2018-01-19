package com.playtika.carproxy.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FullCycleScenario {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test //be sure that car_shop application is running
    public void fullCycle() throws Exception {
        String carDealId = addCarDeal();
        carDealId = carDealId.replace("[", "").replace("]", "");
        log.info("The car deal with id {} was successfully added", carDealId);
        String purchaseClaimId1 = addPurchaseClaim(carDealId, "23000");
        log.info("Purchase claim with id {} and price 23000 was successfully added for the car deal", purchaseClaimId1);
        String purchaseClaimId2 = addPurchaseClaim(carDealId, "24000");
        log.info("Purchase claim with id {} and price 24000 was successfully added for the car deal", purchaseClaimId2);
        String purchaseClaimId3 = addPurchaseClaim(carDealId, "25000");
        log.info("Purchase claim with id {} and price 25000 was successfully added for the car deal", purchaseClaimId3);
        rejectPurchaseClaim(purchaseClaimId1);
        log.info("Purchase claim with id {} was successfully rejected", purchaseClaimId1);
        String price = getBestBid(carDealId);
        log.info("The best bid was found for the car deal, the price is {}", price);
        assertThat(price, is(equalTo("25000")));
    }

    private String getBestBid(String carDealId) throws Exception {
        return mockMvc.perform(get("/cardeals/bestBid")
                .param("carDealId", carDealId)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    private void rejectPurchaseClaim(String purchaseClaimId1) throws Exception {
        mockMvc.perform(post("/purchase/reject")
                .param("purchaseClaimId", purchaseClaimId1)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    private String addPurchaseClaim(String carDealId, String price) throws Exception {
        return mockMvc.perform(post("/purchase")
                .param("carDealId", carDealId)
                .param("price", price)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(("application/json;charset=UTF-8")))
                .andReturn().getResponse().getContentAsString();
    }

    private String addCarDeal() throws Exception {
        return mockMvc.perform(post("/cardeals")
                .param("fileUrl", "src/test/resources/cardeal.csv")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(("application/json;charset=UTF-8")))
                .andReturn().getResponse().getContentAsString();
    }
}