/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.common.account.order;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ManagedOrder extends AbstractOrder {

    private int accountNumber;

    private final List<String> stoplossOrderIDList = new LinkedList(); //list of modified stoploss orders
    private final List<String> targetOrderIDList = new LinkedList(); //list of modified target orders
    private final List<String> closeOrderIDList = new LinkedList(); //list of closed orders - when users click to close the orders or uses EA or any other means outside TP and SL
    private final List<String> deletedOrderIDList = new LinkedList(); //list of deleted pending orders - when users click to delete the orders or uses EA or any other means outside TP and SL
    private final List<String> stoplossOrderIDCancelProcessingList = new LinkedList();
    private final List<String> targetOrderIDCancelProcessingList = new LinkedList();

    public ManagedOrder(String req_identifier, ManagedOrder order) throws OrderException {
        this(req_identifier, order.accountNumber, order.symbolInfo, order.side, order.targetPrice, order.stoplossPrice);
    }

    public ManagedOrder(String req_identifier, int account_number, SymbolInfo symbol_info, char side, double target_price, double stoploss_price) throws OrderException {
        try {
            this.accountNumber = account_number;
            this.symbolInfo = symbol_info;
            this.pip_point = symbol_info.getPipPoint();
            this.side = side;
            this.finalizeInit(req_identifier, target_price, stoploss_price);
        } catch (SQLException ex) {
            Logger.getLogger(ManagedOrder.class.getName()).log(Level.SEVERE, null, ex);
            throw new OrderException(ex);
        }
    }

    public ManagedOrder(String req_identifier, int account_number, String str) throws OrderException, SQLException {

        this.accountNumber = account_number;
        this.setFields(str);
        finalizeInit(req_identifier, targetPrice, stoplossPrice);
    }

    private void finalizeInit(String req_identifier, double target_price, double stoploss_price) throws SQLException, OrderException {

        switch (side) {
            case Order.Side.BUY, Order.Side.SELL -> {
                this.orderID = OrderIDFamily.createMarketOrderID(accountNumber, req_identifier);
                this.ticket = OrderIDFamily.getMarketOrderTicket(this.orderID);
            }
            case Order.Side.BUY_STOP, Order.Side.SELL_STOP, Order.Side.BUY_LIMIT, Order.Side.SELL_LIMIT -> {
                this.orderID = OrderIDFamily.createPendingOrderID(accountNumber, req_identifier);
                this.ticket = OrderIDFamily.getPendingOrderTicket(this.orderID);
            }
            default -> throw new OrderException("Unknow order type");
        }

        modifyTPAndSL(req_identifier, target_price, stoploss_price);
        //finally
        validateOrder();
    }

    private void modifyTPAndSL(String req_identifier, double target_price, double stoploss_price) throws SQLException {
        this.targetPrice = target_price;
        this.stoplossPrice = stoploss_price;

        if (target_price > 0) {
            this.targetOrderIDList.add(
                    OrderIDFamily.createTargetOrderID(this, req_identifier)
            );
        }

        if (stoploss_price > 0) {
            this.stoplossOrderIDList.add(
                    OrderIDFamily.createStoplossOrderID(this, req_identifier)
            );
        }

    }

    public String markForCloseAndGetID(String req_identifier) throws SQLException {
        String close_order_id = OrderIDFamily.createCloseOrderID(this, req_identifier);
        this.closeOrderIDList.add(close_order_id);
        return close_order_id;
    }
    
    public String markForDeleteAndGetID(String req_identifier) throws SQLException {
        String deleted_order_id = OrderIDFamily.createDeleteOrderID(this, req_identifier);
        this.deletedOrderIDList.add(deleted_order_id);
        return deleted_order_id;
    }

    public void modifyOrder(String req_identifier, double target_pips, double stoploss_pips) throws SQLException {
        modifyTPAndSL(req_identifier, target_pips, stoploss_pips);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public List getStoplossOrderIDList() {
        return stoplossOrderIDList;
    }

    public List getTargetOrderIDList() {
        return targetOrderIDList;
    }

    public List getCloseOrderIDList() {
        return closeOrderIDList;
    }

    public List getDeletedOrderIDList() {
        return deletedOrderIDList;
    }    

    public void removeTargetOrderID(String clOrdID) {
        targetOrderIDList.remove(clOrdID);
    }

    public void removeStoplossOrderID(String clOrdID) {
        stoplossOrderIDList.remove(clOrdID);
    }

    public void removeCloseOrderID(String clOrdID) {
        closeOrderIDList.remove(clOrdID);
    }

    public void removeDeletedOrderID(String clOrdID) {
        deletedOrderIDList.remove(clOrdID);
    }    

    public String getLastStoplossOrderID() {
        return stoplossOrderIDList.getLast();
    }

    public String getLastTargetOrderID() {
        return targetOrderIDList.getLast();
    }

    public String getLastCloseOrderID() {
        return closeOrderIDList.getLast();
    }
        
    public String getLastDeletedOrderID() {
        return deletedOrderIDList.getLast();
    }

    public List getStoplossOrderIDCancelProcessingList() {
        return stoplossOrderIDCancelProcessingList;
    }

    public List getTargetOrderIDCancelProcessingList() {
        return targetOrderIDCancelProcessingList;
    }

    public void removeStoplossOrderIDCancelProcessingList(String clOrdID) {
        stoplossOrderIDCancelProcessingList.remove(clOrdID);
    }

    public void removeTargetOrderIDCancelProcessingList(String clOrdID) {
        targetOrderIDCancelProcessingList.remove(clOrdID);
    }

    public void removeStoplossOrderIDList(String clOrdID) {
        stoplossOrderIDList.remove(clOrdID);
    }

    public void removeTargetOrderIDList(String clOrdID) {
        targetOrderIDList.remove(clOrdID);
    }

    public void addTargetOrderIDCancelProcessing(String clOrdID) {
        targetOrderIDCancelProcessingList.add(clOrdID);
    }

    public void addStoplossOrderIDCancelProcessing(String clOrdID) {
        stoplossOrderIDCancelProcessingList.add(clOrdID);
    }

}
