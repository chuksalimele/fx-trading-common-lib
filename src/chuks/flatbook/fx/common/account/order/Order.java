/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.account.order;

/**
 *
 * @author user
 */
 public class Order extends AbstractOrder{


    public Order(String str_order) {        
        this(str_order, FIELD_SEPARATOR, NESTED_FIELD_SEPARATOR);
    }

    public Order(String str_order, String field_sep, String nested_field_sep) {        
        setFields(str_order, field_sep, nested_field_sep);
    }

    public Order(SymbolInfo symbol_info, char side, double target_price, double stoploss_price) throws OrderException {

        this.symbolInfo = symbol_info;
        this.pip_point = symbol_info.getPipPoint();
        this.side = side;
        //finally
        validateOrder();

    }

}
