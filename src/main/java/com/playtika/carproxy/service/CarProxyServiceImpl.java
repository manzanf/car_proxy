package com.playtika.carproxy.service;

import com.playtika.carproxy.carshop.CarShopRestClient;
import com.playtika.carproxy.domain.Car;
import com.playtika.carproxy.domain.CarWithSaleInfo;
import com.playtika.carproxy.domain.ClosingCarDealResponse;
import com.playtika.carproxy.domain.SaleInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@AllArgsConstructor
@Service
public class CarProxyServiceImpl implements CarProxyService {
    CarShopRestClient carShopClient;

    @Override
    public List<Long> processCarDealsFile(String url) throws IOException {
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
                    .map(this::retrieveCarDealId)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toList());
        } catch (IOException e) {
            log.error("The file {} cannot be processed", url, e);
            throw new IOException("The file cannot be processed");
        }
    }

    @Override
    public Long addPurchase(long carDealId, long price) throws NoSuchObjectException {
        Long id = carShopClient.addPurchase(carDealId, price).getBody();
        if (id == null) {
            log.warn("There is no corresponding car deal with id {}, purchase claim was not added", carDealId);
            throw new NoSuchObjectException("There is no corresponding car deal, purchase claim was not added");
        }
        return id;
    }

    @Override
    public boolean rejectPurchaseClaim(long id) {
        return ifRejectSucceed(id);
    }

    @Override
    public ClosingCarDealResponse acceptBestPurchaseClaim(long carDealId) {
        return carShopClient.acceptBestPurchaseClaim(carDealId);
    }

    private Optional<Long> retrieveCarDealId(CarWithSaleInfo cd) {
        Long id = carShopClient.addCarDeals(cd.getCar(), cd.getSaleInfo().getPrice(),
                cd.getSaleInfo().getSellerContacts()).getBody();
        if (id == null) {
            return empty();
        }
        return of(id);
    }

    private CarWithSaleInfo toCarDeal(String line) {
        String[] columns = line.split("\\s*,\\s*");
        Car car = new Car(columns[0], columns[1]);
        SaleInfo saleInfo = new SaleInfo(columns[3], Long.parseLong(columns[2]));
        return new CarWithSaleInfo(car, saleInfo);
    }

    private boolean ifRejectSucceed(long id) {
        return carShopClient.rejectPurchaseClaim(id).getStatusCode() == HttpStatus.OK;
    }
}
