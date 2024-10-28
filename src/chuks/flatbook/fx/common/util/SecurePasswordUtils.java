/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 *
 * @author user
 */
public class SecurePasswordUtils {

    // Regex pattern for password validation
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR = Pattern.compile("[^a-zA-Z0-9]");

    
    public static byte[] hashPassword(char[] password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] passwordBytes = new byte[password.length];
        for (int i = 0; i < password.length; i++) {
            passwordBytes[i] = (byte) password[i];
        }
        md.update(passwordBytes);
        return md.digest();
    }
    
    // Method to validate the password
    public static boolean validate(char[] password) {
        if (password == null || password.length < 8) {
            return false;
        }

        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password) {
            if (LOWERCASE.matcher(String.valueOf(c)).matches()) {
                hasLowercase = true;
            } else if (UPPERCASE.matcher(String.valueOf(c)).matches()) {
                hasUppercase = true;
            } else if (DIGIT.matcher(String.valueOf(c)).matches()) {
                hasDigit = true;
            } else if (SPECIAL_CHAR.matcher(String.valueOf(c)).matches()) {
                hasSpecialChar = true;
            }

            if (hasLowercase && hasUppercase && hasDigit && hasSpecialChar) {
                break;
            }
        }


        return hasLowercase && hasUppercase && hasDigit && hasSpecialChar;
    }

    // Method to clear the password from memory
    private static void clearPassword(char[] password) {
        for (int i = 0; i < password.length; i++) {
            password[i] = '\0';
        }
    }

    // Method to display password validation getRules
    public static String getRules() {
        StringBuilder rules = new StringBuilder();
        rules.append("Password must meet the following criteria:\n");
        rules.append("- At least 8 characters long\n");
        rules.append("- At least one lowercase letter [a-z]\n");
        rules.append("- At least one uppercase letter [A-Z]\n");
        rules.append("- At least one digit [0-9]\n");
        rules.append("- At least one special character [e.g., !@#$%^&*()]\n");
        return rules.toString();
    }

    public static void main(String[] args) {
        char[] password = {'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3', '!'};

        boolean isValid = validate(password);
        System.out.println("Password is valid: " + isValid);

        // Display password getRules
        System.out.println(getRules());

        // Ensure password is cleared from memory
        clearPassword(password);
    }
}
