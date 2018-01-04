package com.playtika.carproxy.service;

import com.playtika.carproxy.domain.Car;
import com.playtika.carproxy.domain.CarWithSaleInfo;
import com.playtika.carproxy.domain.SaleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Slf4j
@Service
public class CarProxyServiceImpl implements CarProxyService {
    @Override
    public List<String> addCarDeals(String url) throws IOException {
        return null;
    }

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
                    .forEach(cd -> CarShopRestClient.addCarDeals(cd));
                    //.collect(Collectors.toList());
        } catch (IOException e) {
            log.error("The file {} cannot be processed", url, e);
            throw new IOException("The file cannot be processed");
        }
    }

    private CarWithSaleInfo toCarDeal(String line) {
        String[] columns = line.split("\\s*,\\s*");
        Car car = new Car(columns[0], columns[1]);
        SaleInfo saleInfo = new SaleInfo(columns[4], Long.parseLong(columns[3]));
        return new CarWithSaleInfo(car, saleInfo);
    }


}
