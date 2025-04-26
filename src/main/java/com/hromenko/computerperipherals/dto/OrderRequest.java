package com.hromenko.computerperipherals.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<Long> items;
    private float totalPrice;

}
