package com.odde.atddv2.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StandaloneOrderTransaction {
    private String orderCode, paidAt;
    private BigDecimal amount;
}
