/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbook.fx.common.account.profile;

/**
 *
 * @author user
 */
public enum UserType {

    TRADER((int) 1),
    ADMIN((int) 2),
    ;

    private final int value;

    UserType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserType fromValue(int value) {
        for (UserType type : UserType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}