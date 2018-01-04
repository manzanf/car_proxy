package com.playtika.carproxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarWithSaleInfo {
    Car car;
    SaleInfo saleInfo;
}
