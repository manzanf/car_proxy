package com.playtika.carproxy.service;

import com.playtika.carproxy.carshop.CarShopRestClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


public class CarProxyServiceImplTest {
    private CarProxyService service;

    @Mock
    CarShopRestClient client;

    @Before
    public void setUp() throws Exception {
        service = new CarProxyServiceImpl(client);
    }

    @Test
    public void processCarDealsFile() throws Exception {
    }

}