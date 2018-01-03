package com.playtika.carproxy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarProxyServiceImpl implements CarProxyService {
    @Override
    public List<String> processCarDealsFile(String url) throws IOException {
        File file = new File(url);
        if (!file.isFile()) {
            log.error("There is no file for this path");
            throw new FileNotFoundException("There is no file for this path");
        }
        try (InputStream is = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            return br.lines()
                    .skip(1)   // header of the csv file
                    .map(this::toCarDeal)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("The file {} cannot be processed", url, e);
            throw new IOException("The file cannot be processed");
        }
    }

    private <R> R toCarDeal(String s) {
    }
}
