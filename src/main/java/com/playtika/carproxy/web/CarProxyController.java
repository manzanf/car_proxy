package com.playtika.carproxy.web;

import com.playtika.carproxy.service.CarProxyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CarProxyController {
    private final CarProxyService service;

    public CarProxyController(CarProxyService service) {
        this.service = service;
    }

    @PostMapping(value = "/cars", produces = "application/json")
    public void processCarDealsFile(@RequestParam("fileUrl") String url) throws IOException {
        service.processCarDealsFile(url);
    }
}
