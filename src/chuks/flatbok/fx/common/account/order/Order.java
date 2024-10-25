/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.common.account.order;

/**
 *
 * @author user
 */
 public class Order extends AbstractOrder{


    public Order(String str) {        
        setFields(str);
    }

    public Order(SymbolInfo symbol_info, char side, double target_price, double stoploss_price) throws OrderException {

        this.symbolInfo = symbol_info;
        this.pip_point = symbol_info.getPipPoint();
        this.side = side;
        //finally
        validateOrder();

    }

}
