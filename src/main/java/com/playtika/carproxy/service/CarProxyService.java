package com.playtika.carproxy.service;

import java.io.FileNotFoundException;

public interface CarProxyService {
    public void processCarDealsFile(String url) throws FileNotFoundException;
}
