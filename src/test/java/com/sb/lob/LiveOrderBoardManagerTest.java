package com.sb.lob;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class LiveOrderBoardManagerTest {

    private static final OrderType SELL = OrderType.SELL;
    private static final OrderType BUY = OrderType.BUY;

    private static final String USER_1 = "User 1";
    private static final String USER_2 = "User 2";
    private static final String USER_3 = "User 3";
    private static final String USER_4 = "User 4";
    private static final String USER_5 = "User 5";
    private static final String USER_6 = "User 6";

    private static final BigDecimal QUANTITY_1_AND_A_HALF = new BigDecimal(1.5);
    private static final BigDecimal QUANTITY_2_AND_A_HALF = new BigDecimal(2.5);
    private static final BigDecimal QUANTITY_3_AND_A_HALF = new BigDecimal(3.5);
    private static final BigDecimal QUANTITY_6 = new BigDecimal(6.0);

    private static final BigDecimal PRICE_100 = new BigDecimal(100);
    private static final BigDecimal PRICE_200 = new BigDecimal(200);
    private static final BigDecimal PRICE_300 = new BigDecimal(300);

    private LiveOrderBoardManager liveOrderBoardManager = new LiveOrderBoardManager();

    @Test
    public void registerOrderOK() {

        Order order1 = new Order(USER_1, QUANTITY_1_AND_A_HALF, PRICE_100, SELL);

        UUID orderNumber1 = liveOrderBoardManager.registerOrder(order1);

        Map<UUID, Order> orders = liveOrderBoardManager.getOrders();

        assertTrue(orders.containsKey(orderNumber1));
    }

    @Test
    public void cancelOrderOK() {

        Order order1 = new Order(USER_1, QUANTITY_1_AND_A_HALF, PRICE_100, SELL);
        Order order2 = new Order(USER_2, QUANTITY_2_AND_A_HALF, PRICE_200, SELL);

        UUID orderNumber1 = liveOrderBoardManager.registerOrder(order1);
        UUID orderNumber2 = liveOrderBoardManager.registerOrder(order2);

        Map<UUID, Order> orders = liveOrderBoardManager.getOrders();

        liveOrderBoardManager.cancelOrder(orderNumber1);

        assertFalse(orders.containsKey(orderNumber1));
        assertTrue(orders.containsKey(orderNumber2));
    }

    @Test
    public void cancelOrderKO() {

        Order order1 = new Order(USER_1, QUANTITY_1_AND_A_HALF, PRICE_100, SELL);
        Order order2 = new Order(USER_2, QUANTITY_2_AND_A_HALF, PRICE_200, SELL);

        UUID orderNumber1 = liveOrderBoardManager.registerOrder(order1);
        UUID orderNumber2 = liveOrderBoardManager.registerOrder(order2);

        Map<UUID, Order> orders = liveOrderBoardManager.getOrders();

        liveOrderBoardManager.cancelOrder(orderNumber1);

        assertThrows(RuntimeException.class, () -> liveOrderBoardManager.cancelOrder(orderNumber1));
    }

    @Test
    public void summarySellOrderingOK() {

        Order order1 = new Order(USER_1, QUANTITY_1_AND_A_HALF, PRICE_300, SELL);
        Order order2 = new Order(USER_2, QUANTITY_2_AND_A_HALF, PRICE_100, SELL);
        Order order3 = new Order(USER_3, QUANTITY_3_AND_A_HALF, PRICE_200, SELL);

        liveOrderBoardManager.registerOrder(order1);
        liveOrderBoardManager.registerOrder(order2);
        liveOrderBoardManager.registerOrder(order3);

        List<OrderSummaryDetail> expectedSummaryDetailList = new ArrayList<>();
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_2_AND_A_HALF, PRICE_100, SELL));
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_3_AND_A_HALF, PRICE_200, SELL));
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_1_AND_A_HALF, PRICE_300, SELL));

        List<OrderSummaryDetail> orderSummaryDetailList = liveOrderBoardManager.fetchSummaryDetails();

        assertArrayEquals(expectedSummaryDetailList.toArray(), orderSummaryDetailList.toArray());
    }

    @Test
    public void summaryBuyOrderingOK() {

        Order order1 = new Order(USER_1, QUANTITY_1_AND_A_HALF, PRICE_300, BUY);
        Order order2 = new Order(USER_2, QUANTITY_2_AND_A_HALF, PRICE_100, BUY);
        Order order3 = new Order(USER_3, QUANTITY_3_AND_A_HALF, PRICE_200, BUY);

        liveOrderBoardManager.registerOrder(order1);
        liveOrderBoardManager.registerOrder(order2);
        liveOrderBoardManager.registerOrder(order3);

        List<OrderSummaryDetail> expectedSummaryDetailList = new ArrayList<>();
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_1_AND_A_HALF, PRICE_300, BUY));
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_3_AND_A_HALF, PRICE_200, BUY));
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_2_AND_A_HALF, PRICE_100, BUY));

        List<OrderSummaryDetail> orderSummaryDetailList = liveOrderBoardManager.fetchSummaryDetails();

        assertArrayEquals(expectedSummaryDetailList.toArray(), orderSummaryDetailList.toArray());
    }

    @Test
    public void summaryMergingOK() {

        Order order1 = new Order(USER_1, QUANTITY_1_AND_A_HALF, PRICE_300, SELL);
        Order order2 = new Order(USER_2, QUANTITY_2_AND_A_HALF, PRICE_100, SELL);
        Order order3 = new Order(USER_3, QUANTITY_3_AND_A_HALF, PRICE_200, SELL);
        Order order4 = new Order(USER_4, QUANTITY_3_AND_A_HALF, PRICE_100, SELL);

        liveOrderBoardManager.registerOrder(order1);
        liveOrderBoardManager.registerOrder(order2);
        liveOrderBoardManager.registerOrder(order3);
        liveOrderBoardManager.registerOrder(order4);

        List<OrderSummaryDetail> expectedSummaryDetailList = new ArrayList<>();
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_6, PRICE_100, SELL));
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_3_AND_A_HALF, PRICE_200, SELL));
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_1_AND_A_HALF, PRICE_300, SELL));

        List<OrderSummaryDetail> orderSummaryDetailList = liveOrderBoardManager.fetchSummaryDetails();

        assertArrayEquals(expectedSummaryDetailList.toArray(), orderSummaryDetailList.toArray());
    }

    @Test
    public void summaryNotMergingAcrossOrderTypeOK() {

        Order order1 = new Order(USER_1, QUANTITY_1_AND_A_HALF, PRICE_300, SELL);
        Order order2 = new Order(USER_2, QUANTITY_2_AND_A_HALF, PRICE_100, SELL);
        Order order3 = new Order(USER_3, QUANTITY_3_AND_A_HALF, PRICE_200, SELL);
        Order order4 = new Order(USER_4, QUANTITY_3_AND_A_HALF, PRICE_100, BUY);

        liveOrderBoardManager.registerOrder(order1);
        liveOrderBoardManager.registerOrder(order2);
        liveOrderBoardManager.registerOrder(order3);
        liveOrderBoardManager.registerOrder(order4);

        List<OrderSummaryDetail> expectedSummaryDetailList = new ArrayList<>();
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_2_AND_A_HALF, PRICE_100, SELL));
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_3_AND_A_HALF, PRICE_200, SELL));
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_1_AND_A_HALF, PRICE_300, SELL));
        expectedSummaryDetailList.add(new OrderSummaryDetail(QUANTITY_3_AND_A_HALF, PRICE_100, BUY));

        List<OrderSummaryDetail> orderSummaryDetailList = liveOrderBoardManager.fetchSummaryDetails();

        assertArrayEquals(expectedSummaryDetailList.toArray(), orderSummaryDetailList.toArray());
    }
}
