package com.playtika.carproxy.service;

import java.io.IOException;
import java.util.List;

public interface CarProxyService {
    List<String> processCarDealsFile(String url) throws IOException;
}
