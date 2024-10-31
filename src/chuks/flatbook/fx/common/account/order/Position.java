/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.account.order;

/**
 *
 * @author user
 */
public class Position {

        String ID;
        long time;
        double price;
        char side;
        double qty;
        String symbol;
        UnfilledOrder unfilledOrder;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public char getSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public UnfilledOrder getUnfilledOrder() {
        return unfilledOrder;
    }

    public void setUnfilledOrder(UnfilledOrder unfilledOrder) {
        this.unfilledOrder = unfilledOrder;
    }
        
        
}
