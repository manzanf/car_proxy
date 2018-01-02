package com.playtika.carproxy.service;

import com.playtika.carproxy.carshop.CarShopRestClient;
import com.playtika.carproxy.domain.Car;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CarProxyServiceImplTest {
    private CarProxyService service;

    @Mock
    private CarShopRestClient client;

    @Before
    public void setUp() throws Exception {
        service = new CarProxyServiceImpl(client);
    }

    @Test
    public void carDealsFileCouldBeProcessed() throws Exception {
        when(client.addCarDeals(new Car("red", "HK23TT"), 23000, "hr"))
                .thenReturn(new ResponseEntity<>(1L, HttpStatus.OK));
        List<Long> actual = service.processCarDealsFile("src/test/resources/cardeal.csv");
        assertThat(actual, is(asList(1L)));
    }

    @Test(expected = FileNotFoundException.class)
    public void ifFileUrlIsIncorrectFileNotFoundExceptionShouldBeThrown() throws Exception {
        service.processCarDealsFile("src");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void ifFileIsCorruptedOutOfBoundsExceptionShouldBeThrown() throws Exception {
        service.processCarDealsFile("src/test/resources/corrupted.csv");
    }

    @Test
    public void ifFileIsEmptyEmptyListOfIdsShouldBeReturned() throws Exception {
        List<Long> actual = service.processCarDealsFile("src/test/resources/empty.csv");
        assertThat(actual, is(empty()));
    }

}