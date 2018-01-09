package com.playtika.carproxy.carshop;

import com.playtika.carproxy.domain.Car;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("CarDealService")
public interface CarShopRestClient {
    @PostMapping(value = "/cars", produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addCarDeals(@RequestBody Car car,
                        @RequestParam("price") long price,
                        @RequestParam("sellerContacts") String sellerContacts);
}
