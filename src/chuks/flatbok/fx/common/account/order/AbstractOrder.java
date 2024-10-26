/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.common.account.order;

import chuks.flatbok.fx.common.util.Utils;
import java.util.Date;

/**
 *
 * @author user
 */
abstract class AbstractOrder {

    protected String marketOrderRequestIdentifier;
    protected String pendingOrderRequestIdentifier;
    protected String modifiyOrderRequestIdentifier;
    protected String deleteOrderRequestIdentifier;
    protected String closeOrderRequestIdentifier;

    public static class Side {

        public final static char NONE = '0';
        public final static char BUY = '1';
        public final static char SELL = '2';
        public final static char BUY_LIMIT = '3';
        public final static char SELL_LIMIT = '4';
        public final static char BUY_STOP = '5';
        public final static char SELL_STOP = '6';
    }

    public static final int FX_LOT_QTY = 100000;

    protected String orderID;
    protected long ticket;
    protected double openPrice;
    protected double closePrice;
    protected double pip_point; //e.g 0.0001 for EURUSD and 0.01 for XAUUSD
    protected double lotSize;
    protected char side = Side.NONE;
    protected SymbolInfo symbolInfo;
    protected double targetPrice;
    protected double stoplossPrice;
    protected double commission;
    protected double swap;
    protected Date open_time;
    protected Date close_time;

    protected void setFields(String str) {
        String[] fields = str.split("|");
        for (int i = 0; i < fields.length; i++) {
            String[] token = fields[i].split("=");
            String field_name = token[0];
            String value = token[1];

            if (field_name.equals("orderID")) {
                this.orderID = value;
            }
            if (field_name.equals("marketOrderRequestIdentifier")) {
                this.marketOrderRequestIdentifier = value;
            }
            if (field_name.equals("pendingOrderRequestIdentifier")) {
                this.pendingOrderRequestIdentifier = value;
            }
            if (field_name.equals("modifiyOrderRequestIdentifier")) {
                this.modifiyOrderRequestIdentifier = value;
            }
            if (field_name.equals("deleteOrderRequestIdentifier")) {
                this.deleteOrderRequestIdentifier = value;
            }
            if (field_name.equals("closeOrderRequestIdentifier")) {
                this.closeOrderRequestIdentifier = value;
            }
            if (field_name.equals("ticket")) {
                this.ticket = Long.parseLong(value);
            }
            if (field_name.equals("openPrice")) {
                this.openPrice = Double.parseDouble(value);
            }
            if (field_name.equals("closePrice")) {
                this.closePrice = Double.parseDouble(value);
            }
            if (field_name.equals("pip_point")) {
                this.pip_point = Double.parseDouble(value);
            }
            if (field_name.equals("lotSize")) {
                this.lotSize = Double.parseDouble(value);
            }
            if (field_name.equals("side")) {
                this.side = value.charAt(0);
            }
            if (field_name.equals("symbolInfo")) {
                //remove the enclosing bracket
                value = value.substring(1, value.length() - 1);
                this.symbolInfo = new SymbolInfo(value);
            }

            if (field_name.equals("targetPrice")) {
                this.targetPrice = Double.parseDouble(value);
            }
            if (field_name.equals("stoplossPrice")) {
                this.stoplossPrice = Double.parseDouble(value);
            }
            if (field_name.equals("commission")) {
                this.commission = Double.parseDouble(value);
            }
            if (field_name.equals("swap")) {
                this.swap = Double.parseDouble(value);
            }
            if (field_name.equals("open_time")) {
                this.open_time = new Date(Long.parseLong(value));
            }
            if (field_name.equals("close_time")) {
                this.close_time = new Date(Long.parseLong(value));
            }

        }
    }

    public String stringify() {
        // Initialize the StringBuilder with an estimate of the final size
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("|orderID=").append(orderID)
                .append("|marketOrderRequestIdentifier=").append(marketOrderRequestIdentifier)
                .append("|pendingOrderRequestIdentifier=").append(pendingOrderRequestIdentifier)
                .append("|modifiyOrderRequestIdentifier=").append(modifiyOrderRequestIdentifier)
                .append("|deleteOrderRequestIdentifier=").append(deleteOrderRequestIdentifier)
                .append("|closeOrderRequestIdentifier=").append(closeOrderRequestIdentifier)                
                .append("|ticket=").append(ticket)
                .append("|openPrice=").append(openPrice)
                .append("|closePrice=").append(closePrice)
                .append("|pip_point=").append(pip_point)
                .append("|lotSize=").append(lotSize)
                .append("|side=").append(side)
                .append("|symbolInfo=[").append(symbolInfo.stringify()).append("]")
                .append("|targetPrice=").append(targetPrice)
                .append("|stoplossPrice=").append(stoplossPrice)
                .append("|commission=").append(commission)
                .append("|swap=").append(swap)
                .append("|open_time=").append(open_time)
                .append("|close_time=").append(close_time);

        // Convert the StringBuilder to a String and return it
        return strBuilder.toString();
    }

    // Initialize the StringBuilder with an estimate of the final size
    // Convert the StringBuilder to a String and return it
    protected void validateOrder() throws OrderException {

        double current_price = symbolInfo.getPrice();
        double target_pips = Math.abs(Utils.pips(current_price, targetPrice, pip_point));
        double stoploss_pips = Math.abs(Utils.pips(current_price, stoplossPrice, pip_point));
        double pips_from_market_price = Math.abs(Utils.pips(current_price, openPrice, pip_point));
        String strCommonSuffixMsgTP_SL = "is invalid or too close - also make sure it is at least " + Config.MINIMUM_TP_SL_PRICE_AWAY + " pips away";
        String strCommonSuffixMsgSTOP_LIMIT = "is invalid or too close - also make sure it is at least " + Config.MINIMUM_PENDING_STOP_LIMIT_PRICE_AWAY + " pips away";

        switch (side) {
            case Side.BUY -> {
                if (targetPrice > 0 && (targetPrice < current_price || target_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Target " + strCommonSuffixMsgTP_SL);
                }
                if (stoplossPrice > 0 && (stoplossPrice > current_price || stoploss_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Stoploss " + strCommonSuffixMsgTP_SL);
                }
            }
            case Side.SELL -> {
                if (targetPrice > 0 && (targetPrice > current_price || target_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Target " + strCommonSuffixMsgTP_SL);
                }
                if (stoplossPrice > 0 && (stoplossPrice < current_price || stoploss_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Stoploss " + strCommonSuffixMsgTP_SL);
                }
            }
            case Side.BUY_LIMIT -> {
                if (targetPrice > 0 && (targetPrice < current_price || target_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Target " + strCommonSuffixMsgTP_SL);
                }
                if (stoplossPrice > 0 && (stoplossPrice > current_price || stoploss_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Stoploss " + strCommonSuffixMsgTP_SL);
                }
                if (openPrice > current_price || pips_from_market_price < Config.MINIMUM_TP_SL_PRICE_AWAY) {
                    throw new OrderException("BUY LIMIT Price " + strCommonSuffixMsgSTOP_LIMIT);
                }
            }
            case Side.SELL_LIMIT -> {
                if (targetPrice > 0 && (targetPrice > current_price || target_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Target " + strCommonSuffixMsgTP_SL);
                }
                if (stoplossPrice > 0 && (stoplossPrice < current_price || stoploss_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Stoploss " + strCommonSuffixMsgTP_SL);
                }
                if (openPrice < current_price || pips_from_market_price < Config.MINIMUM_TP_SL_PRICE_AWAY) {
                    throw new OrderException("SELL LIMIT Price " + strCommonSuffixMsgSTOP_LIMIT);
                }
            }
            case Side.BUY_STOP -> {
                if (targetPrice > 0 && (targetPrice < current_price || target_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Target " + strCommonSuffixMsgTP_SL);
                }
                if (stoplossPrice > 0 && (stoplossPrice > current_price || stoploss_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Stoploss " + strCommonSuffixMsgTP_SL);
                }
                if (openPrice < current_price || pips_from_market_price < Config.MINIMUM_TP_SL_PRICE_AWAY) {
                    throw new OrderException("BUY STOP Price " + strCommonSuffixMsgSTOP_LIMIT);
                }
            }
            case Side.SELL_STOP -> {
                if (targetPrice > 0 && (targetPrice > current_price || target_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Target " + strCommonSuffixMsgTP_SL);
                }
                if (stoplossPrice > 0 && (stoplossPrice < current_price || stoploss_pips < Config.MINIMUM_TP_SL_PRICE_AWAY)) {
                    throw new OrderException("Stoploss " + strCommonSuffixMsgTP_SL);
                }
                if (openPrice > current_price || pips_from_market_price < Config.MINIMUM_TP_SL_PRICE_AWAY) {
                    throw new OrderException("SELL STOP Price " + strCommonSuffixMsgSTOP_LIMIT);
                }
            }
            default -> {
                return;
            }
        }

    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getMarketOrderRequestIdentifier() {
        return OrderIDUtil.getMarketOrderRequestIdentifier(orderID);
    }

    public String getPendingOrderRequestIdentifier() {
        return OrderIDUtil.getPendingOrderRequestIdentifier(orderID);
    }

    public String getModifyOrderRequestIdentifier() {
        return OrderIDUtil.getModifyOrderRequestIdentifier(orderID);
    }

    public String getDeleteOrderRequestIdentifier() {
        return OrderIDUtil.getDeleteOrderRequestIdentifier(orderID);
    }

    public String getCloseOrderRequestIdentifier() {
        return OrderIDUtil.getCloseOrderRequestIdentifier(orderID);
    }

    public void setTicket(long ticket) {
        this.ticket = ticket;
    }

    public boolean isMarketOrder() {
        return side == Side.BUY
                || side == Side.SELL;
    }

    public boolean isPendingOrder() {
        return side == Side.BUY_LIMIT
                || side == Side.BUY_STOP
                || side == Side.SELL_LIMIT
                || side == Side.SELL_STOP;
    }

    public String getOrderID() {
        return orderID;
    }

    public long getTicket() {
        return ticket;
    }

    public double getBid() {
        return this.symbolInfo.getBid();
    }

    public double getAsk() {
        return this.symbolInfo.getAsk();
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double close_price) {
        closePrice = close_price;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double open_price) {
        openPrice = open_price;
    }

    public double getProfit() {

        double pipettes;
        if (side == Side.BUY) {
            pipettes = Utils.pips(symbolInfo.getBid(), openPrice, symbolInfo.getPipettePoint());
        } else {
            pipettes = Utils.pips(openPrice, symbolInfo.getAsk(), symbolInfo.getPipettePoint());
        }

        return pipettes * symbolInfo.getTickValue() * lotSize;
    }

    public boolean isSymbolFiveDigits() {
        return this.symbolInfo.isFiveDigits();
    }

    public double getSymbolPoint() {
        return this.symbolInfo.getPipettePoint();
    }

    public int getSymbolDigits() {
        return this.symbolInfo.getDigits();
    }

    public double getLotSize() {
        return lotSize;
    }

    public void setLotSize(double lot_size) {
        this.lotSize = lot_size;
    }

    public char getSide() {
        return side;
    }

    public String getSymbol() {
        return symbolInfo.getName();
    }

    public double getTargetPrice() {

        return targetPrice;
    }

    public double getStoplossPrice() {
        return stoplossPrice;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double _commission) {
        commission = _commission;
    }

    public double getSwap() {
        return swap;
    }

    public void setSwap(double _swap) {
        swap = _swap;
    }

    public Date getOpenTime() {
        return open_time;
    }

    public void setOpenTime(Date _time) {
        open_time = _time;
    }

    public Date getCloseTime() {
        return close_time;
    }

    public void setCloseTime(Date _time) {
        close_time = _time;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Order
                && ((AbstractOrder) obj).orderID.equals(this.orderID);
    }

}
