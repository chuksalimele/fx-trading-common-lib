/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.account.order;

import chuks.flatbook.fx.common.account.persist.OrderDB;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 ** The class is responsible for market order ID and Pending Order ID and its
 * related orders which are Stoploss , Target, Delete and Close orders * Below
 * is the structure of the ID
 *
 * account-no_[value], time_[value], send-market-order-sn_[value],
 * send-market-order-ticket_[value], send-market-order-request-id_[value],
 * send-pending-order-sn_[value], send-pending-order-ticket_[value],
 * send-pending-order-request-id_[value], modify-stoploss-order-sn_[value],
 * modify-stoploss-order-request-id_[value], modify-take-profit-order-sn_[value],
 * modify-take-profit-order-request-id_[value], delete-pending-order-sn_[value],
 * delete-pending-order-request-id_[value], close-market-order-sn_[value],
 * close-market-order-request-id_[value],
 *
 *
 * Note the request id also called request-response identifier is provided by
 * the client and is use to track the part of the client code that generated the
 * request
 *
 * @author user
 */
public class OrderIDUtil {

    private static final String STR_ACCCOUNT_NO = "account-no";

    private static final String STR_TIME = "time";

    private static final String STR_SEND_MARKET_ORDER_SN = "send-market-order-sn";
    private static final String STR_SEND_MARKET_ORDER_REQUEST_ID = "send-market-order-request-id";
    private static final String STR_SEND_MARKET_ORDER_TICKET = "send-market-order-ticket";

    private static final String STR_SEND_PENDING_ORDER_SN = "send-pending-order-sn";
    private static final String STR_SEND_PENDING_ORDER_REQUEST_ID = "send-pending-order-request-id";
    private static final String STR_SEND_PENDING_ORDER_TICKET = "send-pending-order-ticket";

    private static final String STR_MODIFY_STOPLOSS_ORDER_SN = "modify-stoploss-order-sn";
    private static final String STR_MODIFY_STOPLOSS_ORDER_REQUEST_ID = "modify-stoploss-order-request-id";

    private static final String STR_MODIFY_TAKE_PROFIT_ORDER_SN = "modify-take-profit-order-sn";
    private static final String STR_MODIFY_TAKE_PROFIT_ORDER_REQUEST_ID = "modify-take-profit-order-request-id";

    private static final String STR_MODIFY_ENTRY_PRICE_ORDER_SN = "modify-entry-price-order-sn";
    private static final String STR_MODIFY_ENTRY_PRICE_ORDER_REQUEST_ID = "modify-entry-price-order-request-id";

    private static final String STR_MODIFY_HEDGE_ORDER_SN = "modify-hedge-order-sn";
    private static final String STR_MODIFY_HEDGE_ORDER_REQUEST_ID = "modify-hedge-order-request-id";

    private static final String STR_DELETE_PENDING_ORDER_SN = "delete-pending-order-sn";
    private static final String STR_DELETE_PENDING_ORDER_REQUEST_ID = "delete-pending-order-request-id";

    private static final String STR_CLOSE_MARKET_ORDER_SN = "close-market-order-sn";
    private static final String STR_CLOSE_MARKET_ORDER_REQUEST_ID = "close-market-order-request-id";

    private static final String DASH_SEP = "_";
    private static final String COMMA_SEP = ",";

    static private String decodeBaseOrderIDFrom(String clOrderID, String match) {

        String[] comma_split = clOrderID.split(COMMA_SEP);
        StringBuilder str = new StringBuilder();
        for (String token : comma_split) {
            if (token.startsWith(match)) {
                str.append(token);
                return str.toString();
            } else {
                str.append(token).append(COMMA_SEP);
            }
        }

        return null;
    }

    static private String decodeMarketOrderIDFrom(String clOrderID) {
        return decodeBaseOrderIDFrom(clOrderID, STR_SEND_MARKET_ORDER_TICKET);
    }

    static private String decodePendingOrderIDFrom(String clOrderID) {
        return decodeBaseOrderIDFrom(clOrderID, STR_SEND_PENDING_ORDER_TICKET);
    }

    static public List<MarketOrderIDFamily> groupByMarketOrderIDFamily(List<String> order_ids) {

        Map<String, MarketOrderIDFamily> idMap = new LinkedHashMap();

        for (int i = 0; i < order_ids.size(); i++) {
            String clOrderId = order_ids.get(i);
            if (isMarketOrderID(clOrderId)) {
                var family = idMap.getOrDefault(clOrderId,
                        new MarketOrderIDFamily(clOrderId));

                idMap.put(clOrderId, family);
            } else if (isStoplossOrderID(clOrderId)) {
                String marketId = decodeMarketOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(marketId,
                        new MarketOrderIDFamily(marketId));

                family.modifyStoplossOrderIDs.add(clOrderId);
                idMap.put(marketId, family);
            } else if (isTargetOrderID(clOrderId)) {

                String marketId = decodeMarketOrderIDFrom(clOrderId);

                var family = idMap.getOrDefault(marketId,
                        new MarketOrderIDFamily(marketId));

                family.modifyTakeProfitOrderIDs.add(clOrderId);
                idMap.put(marketId, family);
            } else if (isCloseOrderID(clOrderId)) {
                String marketId = decodeMarketOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(marketId,
                        new MarketOrderIDFamily(marketId));

                family.closeOrderIDs.add(clOrderId);
                idMap.put(marketId, family);
            }
        }

        //sort the related order ids        
        idMap.values().forEach((MarketOrderIDFamily family) -> {
            family.modifyStoplossOrderIDs
                    .sort(Comparator
                            .comparingLong(ordId -> getStoplossOrderSN(ordId)));
            
            family.modifyTakeProfitOrderIDs
                    .sort(Comparator
                            .comparingLong(ordId -> getTargetOrderSN(ordId)));
            
            family.closeOrderIDs
                    .sort(Comparator
                            .comparingLong(ordId -> getCloseOrderSN(ordId)));
        });

        return new LinkedList(idMap.values());
    }

    static public List<PendingOrderIDFamily> groupByPendingOrderIDFamily(List<String> order_ids) {

        Map<String, PendingOrderIDFamily> idMap = new LinkedHashMap();

        for (int i = 0; i < order_ids.size(); i++) {
            String clOrderId = order_ids.get(i);
            if (isPendingOrderID(clOrderId)) {
                var family = idMap.getOrDefault(clOrderId,
                        new PendingOrderIDFamily(clOrderId));

                idMap.put(clOrderId, family);
            } else if (isStoplossOrderID(clOrderId)) {
                String pendingId = decodePendingOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(pendingId,
                        new PendingOrderIDFamily(pendingId));

                family.modifyStoplossOrderIDs.add(clOrderId);
                idMap.put(pendingId, family);
            } else if (isTargetOrderID(clOrderId)) {
                String pendingId = decodePendingOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(pendingId,
                        new PendingOrderIDFamily(pendingId));

                family.modifyTakeProfitOrderIDs.add(clOrderId);
                idMap.put(pendingId, family);
            } else if (isEntryPriceOrderID(clOrderId)) {
                String pendingId = decodePendingOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(pendingId,
                        new PendingOrderIDFamily(pendingId));

                family.modifyEntryPriceOrderIDs.add(clOrderId);
                idMap.put(pendingId, family);
            } else if (isDeleteOrderID(clOrderId)) {
                String pendingId = decodePendingOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(pendingId,
                        new PendingOrderIDFamily(pendingId));

                family.deleteOrderIDs.add(clOrderId);
                idMap.put(pendingId, family);
            }
        }
        //sort the related order ids        
        idMap.values().forEach((PendingOrderIDFamily family) -> {
            family.modifyStoplossOrderIDs
                    .sort(Comparator
                            .comparingLong(ordId -> getStoplossOrderSN(ordId)));
            
            family.modifyTakeProfitOrderIDs
                    .sort(Comparator
                            .comparingLong(ordId -> getTargetOrderSN(ordId)));
            
            family.modifyEntryPriceOrderIDs
                    .sort(Comparator
                            .comparingLong(ordId -> getEntryPriceOrderSN(ordId)));
                        
            family.deleteOrderIDs
                    .sort(Comparator
                            .comparingLong(ordId -> getDeleteOrderSN(ordId)));
        });

        return new LinkedList(idMap.values());
    }

    static private boolean checkID(String clOrderID, String match_name) {
        String[] comma_split = clOrderID.split(COMMA_SEP);
        for (String token : comma_split) {
            String[] dash_split = token.split(DASH_SEP);
            String name = dash_split[0];
            if (name.equals(match_name)) {
                return true;
            }
        }
        return false;
    }

    static private String getValue(String clOrderID, String match_name) {
        String[] comma_split = clOrderID.split(COMMA_SEP);
        for (String token : comma_split) {
            String[] dash_split = token.split(DASH_SEP);
            String name = dash_split[0];
            String value = dash_split[1];
            if (name.equals(match_name)) {
                return value;
            }
        }
        return null;
    }

    static public boolean isMarketOrderID(String clOrderID) {
        return decodeMarketOrderIDFrom(clOrderID).equals(clOrderID);
    }

    static public boolean isPendingOrderID(String clOrderID) {
        return decodePendingOrderIDFrom(clOrderID).equals(clOrderID);
    }

    static public boolean isStoplossOrderID(String clOrderID) {
        return checkID(clOrderID, STR_MODIFY_STOPLOSS_ORDER_SN);
    }

    static public boolean isTargetOrderID(String clOrderID) {
        return checkID(clOrderID, STR_MODIFY_TAKE_PROFIT_ORDER_SN);
    }

    static public boolean isCloseOrderID(String clOrderID) {
        return checkID(clOrderID, STR_CLOSE_MARKET_ORDER_SN);
    }

    static public boolean isEntryPriceOrderID(String clOrderID) {
        return checkID(clOrderID, STR_MODIFY_ENTRY_PRICE_ORDER_SN);
    }

    static public boolean isModifyHedgeOrderID(String clOrderID) {
        return checkID(clOrderID, STR_MODIFY_HEDGE_ORDER_SN);
    }

    static public boolean isDeleteOrderID(String clOrderID) {
        return checkID(clOrderID, STR_DELETE_PENDING_ORDER_SN);
    }

    static public int getAccountNumber(String clOrdId) {
        String acc_no = getValue(clOrdId, STR_ACCCOUNT_NO);
        if (acc_no == null) {
            return -1;
        }
        return Integer.parseInt(acc_no);
    }

    static private long getStoplossOrderSN(String clOrdId) {
        String strSN = getValue(clOrdId, STR_MODIFY_STOPLOSS_ORDER_SN);
        return Long.parseLong(strSN);
    }

    static private long getTargetOrderSN(String clOrdId) {
        String strSN = getValue(clOrdId, STR_MODIFY_TAKE_PROFIT_ORDER_SN);
        return Long.parseLong(strSN);
    }

    static private long getEntryPriceOrderSN(String clOrdId) {
        String strSN = getValue(clOrdId, STR_MODIFY_ENTRY_PRICE_ORDER_SN);
        return Long.parseLong(strSN);
    }

    static private long getHedgeOrderSN(String clOrdId) {
        String strSN = getValue(clOrdId, STR_MODIFY_HEDGE_ORDER_SN);
        return Long.parseLong(strSN);
    }

    static private long getDeleteOrderSN(String clOrdId) {
        String strSN = getValue(clOrdId, STR_DELETE_PENDING_ORDER_SN);
        return Long.parseLong(strSN);
    }

    static private long getCloseOrderSN(String clOrdId) {
        String strSN = getValue(clOrdId, STR_CLOSE_MARKET_ORDER_SN);
        return Long.parseLong(strSN);
    }

    public static long getTime(String clOrdId) {
        String time = getValue(clOrdId, STR_TIME);
        return Long.parseLong(time);
    }

    static public long getMarketOrderTicket(String clOrdId) {
        String acc_no = getValue(clOrdId, STR_SEND_MARKET_ORDER_TICKET);
        if (acc_no == null) {
            return -1;
        }
        return Long.parseLong(acc_no);
    }

    static public long getPendingOrderTicket(String clOrdId) {
        String acc_no = getValue(clOrdId, STR_SEND_PENDING_ORDER_TICKET);
        if (acc_no == null) {
            return -1;
        }
        return Long.parseLong(acc_no);
    }

    static public String createMarketOrderID(int accountNumber, String request_identifier) throws SQLException {
        return STR_ACCCOUNT_NO + DASH_SEP + accountNumber + COMMA_SEP
                + STR_TIME + DASH_SEP + System.currentTimeMillis() + COMMA_SEP
                + STR_SEND_MARKET_ORDER_SN + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_SEND_MARKET_ORDER_REQUEST_ID + DASH_SEP + request_identifier + COMMA_SEP
                + STR_SEND_MARKET_ORDER_TICKET + DASH_SEP + System.currentTimeMillis();
    }

    static public String createPendingOrderID(int accountNumber, String request_identifier) throws SQLException {
        return STR_ACCCOUNT_NO + DASH_SEP + accountNumber + COMMA_SEP
                + STR_TIME + DASH_SEP + System.currentTimeMillis() + COMMA_SEP
                + STR_SEND_PENDING_ORDER_SN + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_SEND_PENDING_ORDER_REQUEST_ID + DASH_SEP + request_identifier + COMMA_SEP
                + STR_SEND_PENDING_ORDER_TICKET + DASH_SEP + System.currentTimeMillis();

    }

    static public String createModifyStoplossOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return marketOrPendingOrderID + COMMA_SEP
                + STR_MODIFY_STOPLOSS_ORDER_SN + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_MODIFY_STOPLOSS_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createModifyStoplossOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return OrderIDUtil.createModifyStoplossOrderID(order.getOrderID(), request_identifier);
    }

    static public String createModifyTargetOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return marketOrPendingOrderID + COMMA_SEP
                + STR_MODIFY_TAKE_PROFIT_ORDER_SN + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_MODIFY_TAKE_PROFIT_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createModifyTargetOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return OrderIDUtil.createModifyTargetOrderID(order.getOrderID(), request_identifier);
    }

    static public String createModifyEntryPriceOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return marketOrPendingOrderID + COMMA_SEP
                + STR_MODIFY_ENTRY_PRICE_ORDER_SN + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_MODIFY_ENTRY_PRICE_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createModifyEntryPriceOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return OrderIDUtil.createModifyEntryPriceOrderID(order.getOrderID(), request_identifier);
    }

    static public String createModifyHedgeOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return marketOrPendingOrderID + COMMA_SEP
                + STR_MODIFY_HEDGE_ORDER_SN + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_MODIFY_HEDGE_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createModifyHedgeOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return createModifyHedgeOrderID(order.getOrderID(), request_identifier);
    }

    static public String createDeleteOrderID(String pendingOrderID, String request_identifier) throws SQLException {
        return pendingOrderID + COMMA_SEP
                + STR_DELETE_PENDING_ORDER_SN + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_DELETE_PENDING_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createDeleteOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return createDeleteOrderID(order.getOrderID(), request_identifier);
    }

    static public String createCloseOrderID(String marketOrderID, String request_identifier) throws SQLException {
        return marketOrderID + COMMA_SEP
                + STR_CLOSE_MARKET_ORDER_SN + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_CLOSE_MARKET_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createCloseOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return createCloseOrderID(order.getOrderID(), request_identifier);
    }

    static private boolean isRelatedToMarketID(String market_id, String ord_id) {
        String decoded_id = decodeMarketOrderIDFrom(ord_id);
        return market_id.equals(decoded_id);
    }

    static String getMarketOrderRequestIdentifier(String orderID) {
        String val = getValue(orderID, STR_SEND_MARKET_ORDER_REQUEST_ID);
        return val;
    }

    static String getPendingOrderRequestIdentifier(String orderID) {
        String val = getValue(orderID, STR_SEND_PENDING_ORDER_REQUEST_ID);
        return val;
    }

    static String getModifyOrderRequestIdentifier(String orderID) {
        //first check if is take-profit modification
        String val = getValue(orderID, STR_MODIFY_TAKE_PROFIT_ORDER_REQUEST_ID);
        if (val == null) {
            //ok now check if is stoploss modification
            val = getValue(orderID, STR_MODIFY_STOPLOSS_ORDER_REQUEST_ID);
        }
        if (val == null) {
            //ok now check if is entry price modification for the case of pending order
            val = getValue(orderID, STR_MODIFY_ENTRY_PRICE_ORDER_REQUEST_ID);
        }
        if (val == null) {
            //ok now check if is hedge order modification for the case hedge account
            val = getValue(orderID, STR_MODIFY_HEDGE_ORDER_REQUEST_ID);
        }
        return val;
    }

    static String getDeleteOrderRequestIdentifier(String orderID) {
        String val = getValue(orderID, STR_DELETE_PENDING_ORDER_REQUEST_ID);
        return val;
    }

    static String getCloseOrderRequestIdentifier(String orderID) {
        String val = getValue(orderID, STR_CLOSE_MARKET_ORDER_REQUEST_ID);
        return val;
    }

}
