package com.sb.lob;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class LiveOrderBoardManager {

    private final Map<UUID, Order> orders = new HashMap<>();

    /**
     * Register an order for the silver bars live order board.
     * The order is given a unique id and stored in memory.
     * @param order : Order details - user, quantity, price and order type (BUY or SELL).
     * @return Unique number to reference the order.
     */
    public UUID registerOrder(Order order) {

        UUID orderNumber = UUID.randomUUID();
        orders.put(orderNumber, order);

        return orderNumber;
    }

    /**
     * Cancel a registered order by passing the unique reference for that order.
     * A run time exception is returned if the order to be cancelled does not exist.
     * @param Unique reference for the order.
     */
    public void cancelOrder(UUID orderNumber) {
        if (null == orders.get(orderNumber)) {
            throw new RuntimeException("No order for " + orderNumber + " found.");
        }
        orders.remove(orderNumber);
    }

    /**
     * Fetch the summary details.
     * Orders are sorted as follows : Sells with ascending prices, Buys with descending prices.
     * The sorted orders are also grouped so orders with the same prices and order type
     * have their quantities summed. This is regardless of the user.
     * @return List of the summary details.
     */
    public List<OrderSummaryDetail> fetchSummaryDetails() {

        List<OrderSummaryDetail> orderSummaryDetails = new ArrayList<>();

        orderSummaryDetails.addAll(gatherAndSortOrderSummaryDetail(Comparator.naturalOrder(), OrderType.SELL));
        orderSummaryDetails.addAll(gatherAndSortOrderSummaryDetail(Comparator.reverseOrder(), OrderType.BUY));

        return orderSummaryDetails;
    }

    /**
     * Method to sum and sort the orders given the comparator and order type.
     * @param comparator : Comparator passed to sort the orders.
     * @param orderType : Order type - BUY or SELL.
     * @return Sorted and summed list of the orders.
     */
    private List<OrderSummaryDetail> gatherAndSortOrderSummaryDetail(
            Comparator<BigDecimal> comparator, OrderType orderType) {
        return orders.values()
                .stream()
                .filter(order -> order.getOrderType() == orderType)
                .collect(Collectors.groupingBy(
                        Order::getPrice,
                        Collectors.reducing(BigDecimal.ZERO,
                                Order::getQuantity,
                                BigDecimal::add)))
                .entrySet()
                .stream()
                .map(summary -> new OrderSummaryDetail(summary.getValue(), summary.getKey(), orderType))
                .sorted(Comparator.comparing(OrderSummaryDetail::getPrice, comparator))
                .collect(Collectors.toList());
    }

    public Map<UUID, Order> getOrders() {
        return orders;
    }
}
