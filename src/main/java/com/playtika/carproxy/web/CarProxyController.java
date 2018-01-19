package com.playtika.carproxy.web;

import com.playtika.carproxy.domain.ClosingCarDealResponse;
import com.playtika.carproxy.service.CarProxyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.util.List;

@RestController
@RequestMapping(produces = "application/json")
public class CarProxyController {
    private final CarProxyService service;

    public CarProxyController(CarProxyService service) {
        this.service = service;
    }

    @PostMapping(value = "/cardeals")
    public List<Long> processCarDealsFile(@RequestParam("fileUrl") String url) throws IOException {
        return service.processCarDealsFile(url);
    }

    @PostMapping(value = "/purchase")
    public ResponseEntity<Long> addPurchase(@RequestParam("carDealId") long carDealId,
                                            @RequestParam("price") long price) {
        try {
            Long id = service.addPurchase(carDealId, price);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (NoSuchObjectException e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping(value = "/purchase/reject")
    public ResponseEntity<Void> rejectPurchaseClaim(@RequestParam("purchaseClaimId") long id) {
        if (!service.rejectPurchaseClaim(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/cardeals/bestBid")
    public ResponseEntity<String> acceptBestPurchaseClaim(@RequestParam("carDealId") long carDealId) throws Exception {
        ClosingCarDealResponse response = service.acceptBestPurchaseClaim(carDealId);
        String state = response.getState();
        switch (state) {
            case "NO_CAR_DEAL":
                return getResponse(HttpStatus.NOT_FOUND, "There is no such car deal");
            case "ALREADY_CLOSED_CAR_DEAL":
                return getResponse(HttpStatus.OK, "The car deal was already closed");
            case "NO_PURCHASE_CLAIMS":
                return getResponse(HttpStatus.NOT_FOUND, "There are no purchase claims for the car deal");
            case "ACCEPTED":
                return new ResponseEntity<>(response.getPrice().toString(), HttpStatus.OK);
            default:
                throw new OperationNotSupportedException();
        }
    }

    private ResponseEntity<String> getResponse(HttpStatus status, String body) {
        return ResponseEntity
                .status(status)
                .body("{\"message\":\"" + body + "\"}");
    }
}


