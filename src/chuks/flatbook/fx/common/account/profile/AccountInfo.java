/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.account.profile;

/**
 *
 * @author user
 */
public class AccountInfo {
    int accountNumber;
    String currency;
    double balance;
    double equity;
    double credit;
    double margin;
    double marginRatio;
    double freeMargin;
    double stopout;
    int leverage;
    
    public AccountInfo(int accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public AccountInfo(String str) {
        
        String[] fields = str.split("|");
        for (String field : fields) {
            String[] token = field.split("=");
            String field_name = token[0];
            String value = token[1];
            if (field_name.equals("accountNumber")) {
                this.accountNumber = Integer.parseInt(value);
            }
            if (field_name.equals("balance")) {
                this.balance = Double.parseDouble(value);
            }
            if (field_name.equals("credit")) {
                this.credit = Double.parseDouble(value);
            }
            if (field_name.equals("currency")) {
                this.currency = value;
            }
            if (field_name.equals("equity")) {
                this.equity = Double.parseDouble(value);
            }
            if (field_name.equals("freeMargin")) {
                this.freeMargin = Double.parseDouble(value);
            }
            if (field_name.equals("margin")) {
                this.margin = Double.parseDouble(value);
            }
            if (field_name.equals("marginRatio")) {
                this.marginRatio =Double.parseDouble(value);
            }   
            if (field_name.equals("stopout")) {
                this.stopout =Double.parseDouble(value);
            }   
            if (field_name.equals("leverage")) {
                this.leverage =Integer.parseInt(value);
            }        
        }
    }

    public String stringify() {
        // Initialize the StringBuilder with an estimate of the final size
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("|accountNumber=").append(this.accountNumber)
                .append("|balance=").append(this.balance)
                .append("|credit=").append(this.credit)
                .append("|currency=").append(this.currency)
                .append("|equity=").append(this.equity)
                .append("|freeMargin=").append(this.freeMargin)
                .append("|leverage=").append(this.leverage)
                .append("|margin=").append(this.margin)
                .append("|marginRatio=").append(this.marginRatio)                
                .append("|stopout=").append(this.stopout);

        // Convert the StringBuilder to a String and return it
        return strBuilder.toString();
    }
    
    public int getAccountNumber() {
        return accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getEquity() {
        return equity;
    }

    public void setEquity(double equity) {
        this.equity = equity;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public double getMarginRatio() {
        return marginRatio;
    }

    public void setMarginRatio(double marginRatio) {
        this.marginRatio = marginRatio;
    }

    public double getFreeMargin() {
        return freeMargin;
    }

    public void setFreeMargin(double freeMargin) {
        this.freeMargin = freeMargin;
    }

    public double getStopout() {
        return stopout;
    }

    public void setStopout(double stopout) {
        this.stopout = stopout;
    }

    public double getLeverage() {
        return leverage;
    }

    public void setLeverage(int leverage) {
        this.leverage = leverage;
    }    
}
