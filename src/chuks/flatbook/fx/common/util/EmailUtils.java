/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author user
 */
public class EmailUtils {

    // Regex pattern for validating email
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*" +
            "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean validate(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static String getRules() {
        StringBuilder rules = new StringBuilder();
        rules.append("Email must meet the following criteria:\n")
             .append("- Contains at least one '@' symbol\n")
             .append("- Contains a valid domain name after '@' (e.g., example.com)\n")
             .append("- Local part (before '@') can contain letters, digits, and special characters\n")
             .append("- Domain name must contain only letters and periods ('.')\n")
             .append("- Top-level domain (after last '.') should be 2 to 7 letters long (e.g., '.com')");
        return rules.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRules());

        String[] emails = {
            "valid.email@example.com",    // valid
            "invalid-email",              // invalid, no '@'
            "another.valid@example.org",  // valid
            "invalid@.com",               // invalid, no domain name
            "invalid@domain..com",        // invalid, double period in domain
            "valid123@example.co.uk"      // valid
        };

        for (String email : emails) {
            System.out.println("Email: " + email + " is valid: " + validate(email));
        }
    }
}
