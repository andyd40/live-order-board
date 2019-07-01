package com.sb.lob;

import java.math.BigDecimal;

public class Order {

    private final String user;
    private final BigDecimal quantity;
    private final BigDecimal price;
    private final OrderType orderType;

    public Order (String user, BigDecimal quantity, BigDecimal price, OrderType orderType) {

        this.user = user;
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
    }

    public String getUser() {
        return user;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
