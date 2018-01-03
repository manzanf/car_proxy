package com.playtika.carproxy.web;

import com.playtika.carproxy.service.CarProxyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping(value = "/cardeals", produces = "application/json")
public class CarProxyController {
    private final CarProxyService service;

    public CarProxyController(CarProxyService service) {
        this.service = service;
    }

    @PostMapping
    public void processCarDealsFile(@RequestParam("fileUrl") String url) throws FileNotFoundException {
        service.processCarDealsFile(url);
    }
}
