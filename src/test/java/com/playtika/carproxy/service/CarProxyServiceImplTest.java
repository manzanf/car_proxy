package com.playtika.carproxy.service;

import com.playtika.carproxy.carshop.CarShopRestClient;
import com.playtika.carproxy.domain.Car;
import com.playtika.carproxy.domain.ClosingCarDealResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.rmi.NoSuchObjectException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
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
    public void ifFileUrlIsIncorrectThrowFileNotFoundException() throws Exception {
        service.processCarDealsFile("src");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void ifFileIsCorruptedThrowOutOfBoundsException() throws Exception {
        service.processCarDealsFile("src/test/resources/corrupted.csv");
    }

    @Test
    public void ifFileIsEmptyReturnEmptyListOfIds() throws Exception {
        List<Long> actual = service.processCarDealsFile("src/test/resources/empty.csv");
        assertThat(actual, is(empty()));
    }

    @Test
    public void purchaseClaimCouldBeAdded() throws Exception {
        when(client.addPurchase(2L, 23000))
                .thenReturn(new ResponseEntity<>(1L, HttpStatus.OK));
        Long actual = service.addPurchase(2L, 23000);
        assertThat(actual, is(1L));
    }

    @Test(expected = NoSuchObjectException.class)
    public void ifPurchaseClaimWasNotAddedThrowNoSuchObjectException() throws Exception {
        when(client.addPurchase(2L, 23000)).thenThrow(NoSuchObjectException.class);
        service.addPurchase(2L, 23000);
    }

    @Test
    public void ifPurchaseClaimWasRejectedReturnTrue() throws Exception {
        when(client.rejectPurchaseClaim(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        assertThat(service.rejectPurchaseClaim(1L), is(true));
    }

    @Test
    public void ifPurchaseClaimWasNotRejectedReturnFalse() throws Exception {
        when(client.rejectPurchaseClaim(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        assertThat(service.rejectPurchaseClaim(1L), is(false));
    }

    @Test
    public void bestPricePurchaseClaimCouldBeFound() throws Exception {
        when(client.acceptBestPurchaseClaim(1L)).thenReturn(new ClosingCarDealResponse(10L, "ACCEPTED"));
        ClosingCarDealResponse actual = service.acceptBestPurchaseClaim(1L);
        assertThat(actual, is(equalTo(new ClosingCarDealResponse(10L, "ACCEPTED"))));
    }
}
