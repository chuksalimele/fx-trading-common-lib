/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.account.order;

import java.sql.SQLException;

/**
 *
 * @author user
 */
public class OrderException extends Exception {

    public OrderException(String ex) {
        super(ex);
    }

    public OrderException(SQLException ex) {
        super(ex);
    }
    
}
