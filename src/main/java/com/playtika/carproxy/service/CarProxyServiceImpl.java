package com.playtika.carproxy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;

@Slf4j
@Service
public class CarProxyServiceImpl implements CarProxyService {
    @Override
    public void processCarDealsFile(String url) throws FileNotFoundException {
        File file = new File(url);
        if (!file.isFile()) {
            log.error("There is no file for this path");
            throw new FileNotFoundException("There is no file for this path");
        }

    }
}
