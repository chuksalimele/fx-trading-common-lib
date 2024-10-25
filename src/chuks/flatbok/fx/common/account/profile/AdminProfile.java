/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.common.account.profile;

/**
 *
 * @author user
 */
public class AdminProfile extends BasicAccountProfile{
    private long registrationTime;
    private long approvalTime;

    // Constructor
    public AdminProfile() {
    }
    
    public AdminProfile(String str) {
        
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
            if (field_name.equals("isLoggedIn")) {
                this.isLoggedIn = Boolean.parseBoolean(value);
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
                .append("|isLoggedIn=").append(isLoggedIn);

        // Convert the StringBuilder to a String and return it
        return strBuilder.toString();
    }
    
    public int getAdminID() {
        return accountNumber;
    }
    
    public String getAdminName() {
         return accountName;
    }    
    
    public void setAdminID(int accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public void setAdminName(String accountName) {
        this.accountName = accountName;
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

}
