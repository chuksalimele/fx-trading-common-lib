/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.listener;

import chuks.flatbook.fx.common.account.order.Order;
import java.awt.Component;
import java.util.List;

/**
 *
 * @author user
 */
 abstract public class OrderActionAdapter implements OrderActionListener{

    private Component comp = null;

    public OrderActionAdapter() {
    }

    public OrderActionAdapter(Component comp) {
        this.comp = comp;
    }

    public Component getComponent(){
        return comp;
    }
     
    @Override
    public void onNewMarketOrder(Order order) {
    }

    @Override
    public void onClosedMarketOrder(Order order) {        
    }

    @Override
    public void onModifiedMarketOrder(Order order) {        
    }

    @Override
    public void onTriggeredPendingOrder(Order order) {        
    }

    @Override
    public void onNewPendingOrder(Order order) {        
    }

    @Override
    public void onDeletedPendingOrder(Order order) {        
    }

    @Override
    public void onModifiedPendingOrder(Order order) {        
    }

    @Override
    public void onOrderSendFailed(Order order, String errMsg) {
    }
    
    @Override
    public void onOrderRemoteError(Order order, String errMsg) {
    }

    @Override
    public void onAddAllHistoryOrders(List<Order> order) {
    }

    @Override
    public void onAddAllOpenOrders(List<Order> order) {
    }

    @Override
    public void onAddAllPendingOrders(List<Order> order) {
    }

}
