package com.playtika.carproxy.carshop;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.playtika.carproxy.domain.Car;
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
}
