package com.playtika.carproxy.carshop;

import com.playtika.carproxy.domain.Car;
import com.playtika.carproxy.domain.ClosingCarDealResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("CarDealService")
public interface CarShopRestClient {
    @PostMapping(value = "/cars", produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Long> addCarDeals(@RequestBody Car car,
                                     @RequestParam("price") long price,
                                     @RequestParam("sellerContacts") String sellerContacts);

    @PostMapping(value = "/purchase", produces = "application/json")
    ResponseEntity<Long> addPurchase(@RequestParam("carDealId") long carDealId,
                                     @RequestParam("price") long price);

    @PostMapping(value = "/purchase/reject", produces = "application/json")
    ResponseEntity<HttpStatus> rejectPurchaseClaim(@RequestParam("purchaseClaimId") long id);

    @GetMapping(value = "/cars/bestBid")
    ClosingCarDealResponse acceptBestPurchaseClaim(@RequestParam("carDealId") long id);
}
