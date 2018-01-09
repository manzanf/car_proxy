package com.playtika.carproxy.web;

import com.playtika.carproxy.service.CarProxyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class CarProxyController {
    private final CarProxyService service;

    public CarProxyController(CarProxyService service) {
        this.service = service;
    }

    @PostMapping(value = "/cardeals", produces = "application/json") ////ifFilePathIsIncorrectFileNotFoundException and 500, map to smth
    public List<Long> processCarDealsFile(@RequestParam("fileUrl") String url) throws IOException {
        return service.processCarDealsFile(url);
    }
}
