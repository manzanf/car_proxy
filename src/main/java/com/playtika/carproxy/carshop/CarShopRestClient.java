package com.playtika.carproxy.carshop;

import com.playtika.carproxy.domain.Car;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("car_shop")
interface CarShopRestClient {
    @PostMapping(value = "/cardeals")
    boolean addCarDeals(@RequestBody Car car,
                        @RequestParam("price") long price,
                        @RequestParam("sellerContacts") String sellerContacts);
}
