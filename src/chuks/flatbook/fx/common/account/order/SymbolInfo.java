/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.account.order;

/**
 *
 * @author user
 */
public class SymbolInfo {

    private String name;
    private double pipette_point;
    private double pip_point;
    private int digits = 5; //default is 5
    private double tickValue;
    private double tickSize;
    private double bid;
    private double ask;
    private double price; //current market price of the symbol updated by MarketDataRequest
    private double swapLong;
    private double swapShort;
    
    private double minAllowedVolume;
    private double maxAllowedVolume;
    
    //Symbol Restrictions
    private boolean disabled;
    private boolean allowLongOnly;
    private boolean allowShortOnly;
    private boolean allowCloseOnly;
    private boolean noRestriction = true;//initial
    private double time;// The last incoming tick time
    private double close; // Close day price
    private double low; // Low day price
    private double high; // High day price
    private double open;  // Open day price
    private double lot_size;// Lot size in the base currency


    public SymbolInfo(String _name, int _digits, double tick_value, double tick_size) {
        this.name = _name;
        this.tickValue = tick_value;
        this.tickSize = tick_size;
        this.digits = Math.abs(_digits);
        this.pipette_point = Math.pow(10, -_digits);
        this.pip_point = pipette_point * 10;
    }

    public SymbolInfo(String str) {
        String[] fields = str.split("|");
        for (int i = 0; i < fields.length; i++) {
            String[] token = fields[i].split("=");
            String field_name = token[0];
            String value = token[1];

            if (field_name.equals("name")) {
                this.name = value;
            }
            if (field_name.equals("pipette_point")) {
                this.pipette_point = Double.parseDouble(value);
            }
            if (field_name.equals("pip_point")) {
                this.pip_point = Double.parseDouble(value);
            }
            if (field_name.equals("digits")) {
                this.digits = Integer.parseInt(value);
            }
            if (field_name.equals("tickValue")) {
                this.tickValue = Double.parseDouble(value);
            }
            if (field_name.equals("tickSize")) {
                this.tickSize = Double.parseDouble(value);
            }
            if (field_name.equals("bid")) {
                this.bid = Double.parseDouble(value);
            }
            if (field_name.equals("ask")) {
                this.ask = Double.parseDouble(value);
            }
            if (field_name.equals("price")) {
                this.price = Double.parseDouble(value);
            }
            if (field_name.equals("swapLong")) {
                this.swapLong = Double.parseDouble(value);
            }
            if (field_name.equals("swapShort")) {
                this.swapShort = Double.parseDouble(value);
            }            
            if (field_name.equals("minAllowedVolume")) {
                this.minAllowedVolume = Double.parseDouble(value);
            }
            if (field_name.equals("maxAllowedVolume")) {
                this.maxAllowedVolume = Double.parseDouble(value);
            }
            if (field_name.equals("disabled")) {
                this.disabled = Boolean.parseBoolean(value);
            }
            if (field_name.equals("allowLongOnly")) {
                this.allowLongOnly = Boolean.parseBoolean(value);
            }
            if (field_name.equals("allowShortOnly")) {
                this.allowShortOnly = Boolean.parseBoolean(value);
            }
            if (field_name.equals("allowCloseOnly")) {
                this.allowCloseOnly = Boolean.parseBoolean(value);
            }
            if (field_name.equals("noRestriction")) {
                this.noRestriction = Boolean.parseBoolean(value);
            }
        }
    }

    public String stringify() {
        // Initialize the StringBuilder
        StringBuilder strBuilder = new StringBuilder();

        // Append each field to the StringBuilder
        strBuilder.append("name=").append(name)
                .append("|pipette_point=").append(pipette_point)
                .append("|pip_point=").append(pip_point)
                .append("|digits=").append(digits)
                .append("|tickValue=").append(tickValue)
                .append("|tickSize=").append(tickSize)
                .append("|bid=").append(bid)
                .append("|ask=").append(ask)
                .append("|price=").append(price)
                .append("|swapLong=").append(swapLong)
                .append("|swapShort=").append(swapShort)
                .append("|minAllowedVolume=").append(minAllowedVolume)
                .append("|maxAllowedVolume=").append(maxAllowedVolume)
                .append("|disabled=").append(disabled)
                .append("|allowLongOnly=").append(allowLongOnly)
                .append("|allowShortOnly=").append(allowShortOnly)
                .append("|allowCloseOnly=").append(allowCloseOnly)
                .append("|noRestriction=").append(noRestriction);

        // Convert the StringBuilder to a String and return it
        return strBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public int getDigits() {
        return this.digits;
    }

    public boolean isFiveDigits() {
        return digits == 5;
    }

    public boolean isThreeDigits() {
        return digits == 3;
    }

    public boolean isTwoDigits() {
        return digits == 2;
    }

    public double getPipettePoint() {
        return pipette_point;
    }

    public double getPipPoint() {
        return pip_point;
    }

    public int getSpreadPip() {
        return getSpreadPipette() / 10;
    }

    public int getSpreadPipette() {
        if (bid <= 0 || ask <= 0) {
            return 0;
        }
        return (int)((ask - bid) / pipette_point);
    }

    public double getTickValue() {
        return tickValue;
    }

    public double getTickSize() {
        return tickSize;
    }

    public double getBid() {
        return bid;
    }

    public double getAsk() {
        return ask;
    }

    public double getPrice() {
        if (ask <= 0 || bid <= 0) {
            return 0;
        }
        return (bid + ask) / 2;
    }

    public void setBid(double bidPrice) {
        if (bidPrice <= 0) {
            return;
        }
        bid = bidPrice;
    }

    public void setAsk(double askPrice) {
        if (askPrice <= 0) {
            return;
        }
        ask = askPrice;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisable() {
         disabled = true;//set true
         allowLongOnly = false;
         allowShortOnly = false;
         allowCloseOnly = false;
         noRestriction = false;
    }
    
    public boolean isAllowLongTradesOnly() {
        return allowLongOnly;
    }

    public void setAllowLongTradesOnly() {
         disabled = false;
         allowLongOnly = true;//set true
         allowShortOnly = false;
         allowCloseOnly = false;
         noRestriction = false;
    }
    
    public boolean isAllowShortTradesOnly() {
        return allowShortOnly;
    }

    public void setAllowShortTradesOnly() {
         disabled = false;
         allowLongOnly = false;
         allowShortOnly = true;//set true
         allowCloseOnly = false;
         noRestriction = false;
    }
    
    public boolean isAllowCloseTradesOnly() {
        return allowCloseOnly;
    }
    
    public void setAllowCloseTradesOnly() {
         disabled = false;
         allowLongOnly = false;
         allowShortOnly = false;
         allowCloseOnly = true;//set true
         noRestriction = false;
    }
    
    public boolean isNoRestriction() {
        return noRestriction;
    }
    
    public void setNoRestriction() {
         disabled = false;
         allowLongOnly = false;
         allowShortOnly = false;
         allowCloseOnly = false;
         noRestriction = true;//set true
    }

    public double getMinAllowedVolume() {
        return minAllowedVolume;
    }

    public double getMaxAllowedVolume() {
        return maxAllowedVolume;
    }

    public double getSwapLong() {
        return swapLong;
    }

    public double getSwapShort() {
        return swapShort;
    }

    /**
     * The last incoming tick time
     * 
     * @return 
     */
    public double getLotSize() {
        return lot_size;
    }

    /**
     * Open day price
     * 
     * @return 
     */
    public double getOpen() {
        return open;
    }
    
    /**
     * High day price
     * 
     * @return 
     */
    public double getHigh() {
        return high;
    }

     /**
     * Low day price
     * 
     * @return 
     */
    public double getLow() {
        return low;
    }

     /**
     * Close day price
     * 
     * @return 
     */
    public double getClose() {
        return close;
    }

    /**
     * The last incoming tick time
     * 
     * @return 
     */
    public double getTime() {
        return time;
    }

    
    /**
     * Spread value in points
     * 
     * @return 
     */
    public double getSpread() {
        return getSpreadPipette();
    }

}
