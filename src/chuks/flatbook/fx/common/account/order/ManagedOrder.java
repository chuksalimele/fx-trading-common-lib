/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.account.order;

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
    private final List<String> takeProfitOrderIDList = new LinkedList(); //list of modified target orders
    private final List<String> closeOrderIDList = new LinkedList(); //list of closed orders - when users click to close the orders or uses EA or any other means outside TP and SL
    private final List<String> deletedOrderIDList = new LinkedList(); //list of deleted pending orders - when users click to delete the orders or uses EA or any other means outside TP and SL
    private final List<String> cancelledStoplossOrderIDList = new LinkedList();
    private final List<String> cancelledTargetOrderIDList = new LinkedList(); 
    private double _lastTakeProfitPrice;
    private double _lastStoplossPrice;
    


    public ManagedOrder(String req_identifier, ManagedOrder order) throws OrderException {
        this(req_identifier, order.accountNumber, order.symbolInfo, order.side, order.takeProfitPrice, order.stoplossPrice);
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
        finalizeInit(req_identifier, takeProfitPrice, stoplossPrice);
    }

    private void finalizeInit(String req_identifier, double target_price, double stoploss_price) throws SQLException, OrderException {

        switch (side) {
            case Order.Side.BUY, Order.Side.SELL -> {
                this.orderID = OrderIDUtil.createMarketOrderID(accountNumber, req_identifier);
                this.ticket = OrderIDUtil.getMarketOrderTicket(this.orderID);
            }
            case Order.Side.BUY_STOP, Order.Side.SELL_STOP, Order.Side.BUY_LIMIT, Order.Side.SELL_LIMIT -> {
                this.orderID = OrderIDUtil.createPendingOrderID(accountNumber, req_identifier);
                this.ticket = OrderIDUtil.getPendingOrderTicket(this.orderID);
            }
            default -> throw new OrderException("Unknow order type");
        }

        modifySL(req_identifier, stoploss_price);
        modifyTP(req_identifier, target_price);
        
        //finally
        validateOrder();
    }



    private void modifySL(String req_identifier, double stoploss_price) throws SQLException {
        this._lastStoplossPrice = this.stoplossPrice;
        this.stoplossPrice = stoploss_price;

        if (stoploss_price > 0) {
            this.stoplossOrderIDList.add(
                    OrderIDUtil.createModifyStoplossOrderID(this, req_identifier)
            );
        }

    }

    private void modifyTP(String req_identifier, double target_price) throws SQLException {
        this._lastTakeProfitPrice = this.takeProfitPrice;
        this.takeProfitPrice = target_price;

        if (target_price > 0) {
            this.takeProfitOrderIDList.add(
                    OrderIDUtil.createModifyTargetOrderID(this, req_identifier)
            );
        }

    }

    public void undoLastStoplossModify() {
        //do this first
        if(this.stoplossPrice > 0){
            this.stoplossOrderIDList.removeLast();
        }
        //then this
        this.stoplossPrice = this._lastStoplossPrice;                
    }
    
    public void undoLastTakeProfitModify() {
        //do this first
        if(this.takeProfitPrice > 0){
            this.takeProfitOrderIDList.removeLast();
        }
        
        //then this
        this.takeProfitPrice = this._lastTakeProfitPrice;             
    }

    public String markForCloseAndGetID(String req_identifier) throws SQLException {
        String close_order_id = OrderIDUtil.createCloseOrderID(this, req_identifier);
        this.closeOrderIDList.add(close_order_id);
        return close_order_id;
    }
    
    public String markForDeleteAndGetID(String req_identifier) throws SQLException {
        String deleted_order_id = OrderIDUtil.createDeleteOrderID(this, req_identifier);
        this.deletedOrderIDList.add(deleted_order_id);
        return deleted_order_id;
    }

    public void modifyTakeProfit(String req_identifier, double target_pips) throws SQLException {
        modifyTP(req_identifier, target_pips);
    }

    public void modifyStoploss(String req_identifier, double stoploss_pips) throws SQLException {
        modifySL(req_identifier, stoploss_pips);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public List getStoplossOrderIDList() {
        return stoplossOrderIDList;
    }

    public List getTakeProfitOrderIDList() {
        return takeProfitOrderIDList;
    }

    public List getCloseOrderIDList() {
        return closeOrderIDList;
    }

    public List getDeletedOrderIDList() {
        return deletedOrderIDList;
    }    


    public List getCancelledStoplossOrderIDList() {
        return cancelledStoplossOrderIDList;
    }

    public List getCancelledTakeProfitOrderIDList() {
        return cancelledTargetOrderIDList;
    }

    public String getLastStoplossOrderID() {
        return stoplossOrderIDList.getLast();
    }

    public String getLastTakeProfitOrderID() {
        return takeProfitOrderIDList.getLast();
    }

    public String getLastCloseOrderID() {
        return closeOrderIDList.getLast();
    }
        
    public String getLastDeletedOrderID() {
        return deletedOrderIDList.getLast();
    }

    public void cancelStoplossOrderID(String clOrdID) {
        stoplossOrderIDList.remove(clOrdID);
        cancelledStoplossOrderIDList.add(clOrdID);
    }

    public void cancelTakeProfitOrderID(String clOrdID) {
        takeProfitOrderIDList.remove(clOrdID);
        cancelledTargetOrderIDList.add(clOrdID);
    }

    public void removeTakeProfitOrderID(String clOrdID) {
        takeProfitOrderIDList.remove(clOrdID);
    }

    public void removeStoplossOrderID(String clOrdID) {
        stoplossOrderIDList.remove(clOrdID);
    }

    public void removeCloseOrderID(String clOrdID) {
        closeOrderIDList.remove(clOrdID);
    }

}
