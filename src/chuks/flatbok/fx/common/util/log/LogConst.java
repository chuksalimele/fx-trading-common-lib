/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbok.fx.common.util.log;

/**
 *
 * @author user
 */
public interface LogConst {
    static final String STR_REJECTED_IP = "REJECTED IP";    
    static final String STR_SUSPICIOUS_IP = "SUSPICIOUS IP";   
    
    static String concatLogMsg(String str1, String str2) {
        return str1 +" - "+str2;
    }
}
