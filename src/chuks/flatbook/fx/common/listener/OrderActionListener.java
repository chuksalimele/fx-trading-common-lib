/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbook.fx.common.listener;

import chuks.flatbook.fx.common.account.order.Order;
import java.util.List;

/**
 *
 * @author user
 */
public interface OrderActionListener {    
    void onNewMarketOrder(Order order);
    void onClosedMarketOrder(Order order);
    void onModifiedMarketOrder(Order order);
    void onTriggeredPendingOrder(Order order);
    void onNewPendingOrder(Order order);
    void onDeletedPendingOrder(Order order);
    void onModifiedPendingOrder(Order order);
    void onOrderSendFailed(Order order, String errMsg);
    void onOrderRemoteError(Order order, String errMsg);
    void onOrderNotAvailable(String req_identifier, String errMsg);
    void onAddAllOpenOrders(List<Order> orders);
    void onAddAllPendingOrders(List<Order> orders);
    void onAddAllHistoryOrders(List<Order> orders);
}
