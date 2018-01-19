package com.playtika.carproxy.carshop;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.playtika.carproxy.domain.Car;
import com.playtika.carproxy.domain.ClosingCarDealResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarShopRestClientTest {
    @Autowired
    CarShopRestClient client;

    private long price = 10;
    private final String sellerContacts = "tom";
    private final String serializedCar = "{\"color\": \"red\", \"model\": \"VV63SS\"}";
    private final Car car = new Car("red", "VV63SS");
    private final long carDealId = 1L;
    private final long purchaseClaimId = 2L;

    @Rule
    public WireMockRule wmr = new WireMockRule(8082);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void shouldPassIdForNewCarDeal() throws Exception {
        stubFor(post(urlPathEqualTo("/cars"))
                .withQueryParam("price", equalTo(Long.toString(price)))
                .withQueryParam("sellerContacts", equalTo(sellerContacts))
                .withRequestBody(equalToJson(serializedCar))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("1")));
        ResponseEntity<Long> response = client.addCarDeals(car, price, sellerContacts);
        assertThat(response.getBody(), is(1L));
    }

    @Test
    public void shouldPassNullForEarlierAddedCarDeal() throws Exception {
        stubFor(post(urlPathEqualTo("/cars"))
                .withQueryParam("price", equalTo(Long.toString(price)))
                .withQueryParam("sellerContacts", equalTo(sellerContacts))
                .withRequestBody(equalToJson(serializedCar))
                .willReturn(aResponse()
                        .withStatus(204)));
        ResponseEntity<Long> response = client.addCarDeals(car, price, sellerContacts);
        assertThat(response.getBody(), is(nullValue()));
    }

    @Test
    public void shouldPassIdForAddedPurchaseClaim() throws Exception {
        stubFor(post(urlPathEqualTo("/purchase"))
                .withQueryParam("carDealId", equalTo(Long.toString(carDealId)))
                .withQueryParam("price", equalTo(Long.toString(price)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("1")
                        .withStatus(200)));
        ResponseEntity<Long> response = client.addPurchase(carDealId, price);
        assertThat(response.getBody(), is(1L));
    }

    @Test
    public void shouldPassNullIfPurchaseClaimWasNotAdded() throws Exception {
        stubFor(post(urlPathEqualTo("/purchase"))
                .withQueryParam("carDealId", equalTo(Long.toString(carDealId)))
                .withQueryParam("price", equalTo(Long.toString(price)))
                .willReturn(aResponse()
                        .withStatus(204)));
        ResponseEntity<Long> response = client.addPurchase(carDealId, price);
        assertThat(response.getBody(), is(nullValue()));
    }

    @Test
    public void shouldPassOkStatusIfPurchaseClaimWasRejected() throws Exception {
        stubFor(post(urlPathEqualTo("/purchase/reject"))
                .withQueryParam("purchaseClaimId", equalTo(Long.toString(purchaseClaimId)))
                .willReturn(aResponse()
                        .withStatus(200)));
        int code = client.rejectPurchaseClaim(purchaseClaimId).getStatusCodeValue();
        assertThat(code, is(200));
    }

    @Test
    public void shouldPassNoContentStatusIfPurchaseClaimWasNotRejected() throws Exception {
        stubFor(post(urlPathEqualTo("/purchase/reject"))
                .withQueryParam("purchaseClaimId", equalTo(Long.toString(purchaseClaimId)))
                .willReturn(aResponse()
                        .withStatus(204)));
        int code = client.rejectPurchaseClaim(purchaseClaimId).getStatusCodeValue();
        assertThat(code, is(204));
    }

    @Test
    public void shouldPassPriceAndStateForBestPricePurchaseClaim() throws Exception {
        stubFor(get(urlPathEqualTo("/cars/bestBid"))
                .withQueryParam("carDealId", equalTo(Long.toString(carDealId)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" + "  \"price\": 10,\n" +
                                "  \"state\": \"ACCEPTED\"\n" + "}")
                        .withStatus(200)));
        ClosingCarDealResponse actual = client.acceptBestPurchaseClaim(1L);
        assertThat(actual, is(new ClosingCarDealResponse(10L, "ACCEPTED")));
    }

    @Test
    public void shouldPassNoCarDealState() throws Exception {
        stubFor(get(urlPathEqualTo("/cars/bestBid"))
                .withQueryParam("carDealId", equalTo(Long.toString(carDealId)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" + "  \"price\": null,\n" +
                                "  \"state\": \"NO_CAR_DEAL\"\n" + "}")
                        .withStatus(200)));
        ClosingCarDealResponse actual = client.acceptBestPurchaseClaim(1L);
        assertThat(actual.getState(), is("NO_CAR_DEAL"));
    }

    @Test
    public void shouldPassNoPurchaseClaimsState() throws Exception {
        stubFor(get(urlPathEqualTo("/cars/bestBid"))
                .withQueryParam("carDealId", equalTo(Long.toString(carDealId)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" + "  \"price\": null,\n" +
                                "  \"state\": \"NO_PURCHASE_CLAIMS\"\n" + "}")
                        .withStatus(200)));
        ClosingCarDealResponse actual = client.acceptBestPurchaseClaim(1L);
        assertThat(actual.getState(), is("NO_PURCHASE_CLAIMS"));
    }

    @Test
    public void shouldPassAlreadyClosedCarDealState() throws Exception {
        stubFor(get(urlPathEqualTo("/cars/bestBid"))
                .withQueryParam("carDealId", equalTo(Long.toString(carDealId)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" + "  \"price\": null,\n" +
                                "  \"state\": \"ALREADY_CLOSED_CAR_DEAL\"\n" + "}")
                        .withStatus(200)));
        ClosingCarDealResponse actual = client.acceptBestPurchaseClaim(1L);
        assertThat(actual.getState(), is("ALREADY_CLOSED_CAR_DEAL"));
    }
}
