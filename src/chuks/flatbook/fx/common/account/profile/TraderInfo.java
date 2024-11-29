/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.account.profile;

/**
 *
 * @author user
 */
public class TraderInfo extends BasicInfo{
    private long registrationTime;
    private long approvalTime;
    private boolean isActive;
    private boolean isEnabled;
    private long emailVerifiedTime;
    private AccountInfo accountInfo;

    // Constructor
    public TraderInfo() {
    }
    
    public TraderInfo(String str) {
        
        String[] fields = str.split("|");
        for (String field : fields) {
            String[] token = field.split("=");
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
            if (field_name.equals("registrationTime")) {
                this.registrationTime = Long.parseLong(value);
            }
            if (field_name.equals("approvalTime")) {
                this.approvalTime = Long.parseLong(value);
            }
            if (field_name.equals("approvedBy_AdminID")) {
                this.approvedBy_AdminID = Integer.parseInt(value);
            }
            if (field_name.equals("isActive")) {
                this.isActive = Boolean.parseBoolean(value);
            }
            if (field_name.equals("isEnabled")) {
                this.isEnabled = Boolean.parseBoolean(value);
            }
            if (field_name.equals("emailVerifiedTime")) {
                this.emailVerifiedTime = Long.parseLong(value);
            }
            if (field_name.equals("isLoggedIn")) {
                this.isLoggedIn = Boolean.parseBoolean(value);
            }                               
            if (field_name.equals("accountInfo")) {
                //remove the enclosing bracket
                value = value.substring(1, value.length() - 1);
                this.accountInfo = new AccountInfo(value);
            }
        }
    }

    @Override
    public String stringify(boolean include_pwd) {
        // Initialize the StringBuilder with an estimate of the final size
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("|accountNumber=").append(accountNumber)
                .append("|accountName=").append(accountName)
                .append("|email=").append(email)
                .append("|password=").append(password)
                .append("|password=").append(include_pwd? new String(password):"")
                .append("|registrationTime=").append(registrationTime)
                .append("|approvalTime=").append(approvalTime)
                .append("|approvedBy_AdminID=").append(approvedBy_AdminID)
                .append("|isActive=").append(isActive)
                .append("|isEnabled=").append(isEnabled)
                .append("|emailVerifiedTime=").append(emailVerifiedTime)
                .append("|isLoggedIn=").append(isLoggedIn)
                .append("|accountInfo=[").append(accountInfo.stringify()).append("]");

        // Convert the StringBuilder to a String and return it
        return strBuilder.toString();
    }        
    
    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }
    
    public long getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(long registrationTime) {
        this.registrationTime = registrationTime;
    }

    public long getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(long approvalTime) {
        this.approvalTime = approvalTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public long getEmailVerifiedTime() {
        return emailVerifiedTime;
    }

    public void setEmailVerifiedTime(long emailVerifiedTime) {
        this.emailVerifiedTime = emailVerifiedTime;
    }


}
