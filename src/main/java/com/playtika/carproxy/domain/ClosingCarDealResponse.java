package com.playtika.carproxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ClosingCarDealResponse {
    private Long price;

    @NonNull
    private String state;

    public ClosingCarDealResponse(String state) {
        this.state = state;
    }
}
