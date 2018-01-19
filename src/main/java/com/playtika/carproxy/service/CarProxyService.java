package com.playtika.carproxy.service;

import com.playtika.carproxy.domain.ClosingCarDealResponse;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.util.List;

public interface CarProxyService {
    List<Long> processCarDealsFile(String url) throws IOException;

    Long addPurchase(long carDealId, long price) throws NoSuchObjectException;

    boolean rejectPurchaseClaim(long id);

    ClosingCarDealResponse acceptBestPurchaseClaim(long carDealId);
}
