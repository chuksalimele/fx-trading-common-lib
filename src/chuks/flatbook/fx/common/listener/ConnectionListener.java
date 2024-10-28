/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbook.fx.common.listener;

/**
 *
 * @author user
 */
public interface ConnectionListener {    
    void onConnectionProgress(String status);
    void onConnected();
    void onDisconnected(String errMsg);
}
