package com.playtika.carproxy.web;

import com.playtika.carproxy.service.CarProxyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/cardeals", produces = "application/json")
public class CarProxyController {
    private final CarProxyService service;

    public CarProxyController(CarProxyService service) {
        this.service = service;
    }

    @PostMapping
    public void processCarDealsFile(@RequestParam("fileUrl") String url) {
        service.processCarDealsFile(url);
    }
}
