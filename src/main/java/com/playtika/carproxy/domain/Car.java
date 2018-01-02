package com.playtika.carproxy.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public class Car {
    @NonNull
    private String color;

    @NonNull
    private String model;
}