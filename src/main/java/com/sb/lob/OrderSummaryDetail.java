package com.sb.lob;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderSummaryDetail {

    private BigDecimal quantity;
    private BigDecimal price;
    private OrderType orderType;

    public OrderSummaryDetail(BigDecimal quantity, BigDecimal price, OrderType orderType) {

        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
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

    @Override
    public String toString() {
        return "OrderSummaryDetail{" +
                "quantity=" + quantity +
                ", price=" + price +
                ", orderType=" + orderType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderSummaryDetail that = (OrderSummaryDetail) o;
        return quantity.compareTo(that.quantity) == 0 &&
                price.compareTo(that.price) == 0 &&
                orderType == that.orderType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, price, orderType);
    }
}
