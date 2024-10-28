/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author user
 */
public class Utils {
        
    public static LocalDateTime convertMillisToLocalDateTime(long epochMillis, ZoneId zoneId) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), zoneId);
    }
    
    public static LocalDateTime convertSecondsToLocalDateTime(long epochSeconds, ZoneId zoneId) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), zoneId);
    }
    
    public static byte[] charArrayToBytes(char[] password)  {
        byte[] passwordBytes = new byte[password.length];
        for (int i = 0; i < password.length; i++) {
            passwordBytes[i] = (byte) password[i];
        }
        return passwordBytes;
    }
    
    public static double pips(double higher_price, double lower_pips, double point) {
        return (higher_price - lower_pips) / point;
    }    

}
