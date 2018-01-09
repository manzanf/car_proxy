package com.playtika.carproxy.service;

import java.io.IOException;
import java.util.List;

public interface CarProxyService {
    List<Long> processCarDealsFile(String url) throws IOException;
}
