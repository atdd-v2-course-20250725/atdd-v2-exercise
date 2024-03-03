package com.odde.atddv2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderTransaction {
    @JsonIgnore
    private Order order;

    public String getOrderCode() {
        return order.getCode();
    }

    private String paidAt;
    private BigDecimal amount;
}
