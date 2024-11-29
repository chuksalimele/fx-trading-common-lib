/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.account.profile;

/**
 *
 * @author user
 */
abstract public class BasicInfo {
    protected int accountNumber;
    protected String accountName;
    protected String email;
    protected byte[] password; // Security-sensitive field
    protected int approvedBy_AdminID;
    protected boolean isLoggedIn;

    // Constructor
    public BasicInfo() {
    }
    
    public BasicInfo(String str) {
        
        String[] fields = str.split("|");
        for (int i = 0; i < fields.length; i++) {
            String[] token = fields[i].split("=");
            String field_name = token[0];
            String value = token[1];

            if (field_name.equals("accountNumber")) {
                this.accountNumber = Integer.parseInt(value);
            }
            if (field_name.equals("accountName")) {
                this.accountName = value;
            }
            if (field_name.equals("email")) {
                this.email = value;
            }
            if (field_name.equals("password")) {
                this.password = value.getBytes();     
                value = "";//clear sensitive data
            }

            if (field_name.equals("approvedBy_AdminID")) {
                this.approvedBy_AdminID = Integer.parseInt(value);
            }
            
       
        }
    }

    abstract public String stringify(boolean include_pwd);
    

    public boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }
    
    public void setIsLoggedIn(boolean logged_in) {
        this.isLoggedIn = logged_in;
    }    
    
    // Getters and Setters
    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public int getApprovedBy() {
        return approvedBy_AdminID;
    }

    public void setApprovedBy(int approvedBy_AdminID) {
        this.approvedBy_AdminID = approvedBy_AdminID;
    }

    // Clear sensitive data
    public void clearPassword() {
        if (this.password != null) {
            java.util.Arrays.fill(this.password, (byte) 0x00);
        }
    }    


}
