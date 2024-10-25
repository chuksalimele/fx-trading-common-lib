/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbok.fx.common.util.log;

/**
 *
 * @author user
 */
public enum LogLevel {
     INFO((int) 1),
     WARN((int) 2),
     DEBUG ((int) 3),
     ERROR((int) 4),
     REJECTED_IP((int) 5),  
     SUSPICIOUS_IP((int) 6),          
    ;

    private final int value;

    LogLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static LogLevel fromValue(int value) {
        for (LogLevel type : LogLevel.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}    

