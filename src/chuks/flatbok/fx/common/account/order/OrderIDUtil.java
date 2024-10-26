/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.common.account.order;

import chuks.flatbok.fx.common.account.persist.OrderDB;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 ** The class is responsible for market order ID and Pending Order ID and its
 * related orders which are * Stoploss , Target and Close orders. * Below is the
 * structure of the ID
 *
 * account-no_[value], send-market-order-id_[value],
 * send-market-order-ticket_[value], send-market-order-request-id_[value],
 * send-pending-order-id_[value], send-pending-order-ticket_[value],
 * send-pending-order-request-id_[value], modify-stoploss-order-id_[value],
 * modify-stoploss-order-request-id_[value], modify-target-order-id_[value],
 * modify-target-order-request-id_[value], delete-pending-order-id_[value],
 * delete-pending-order-request-id_[value], close-market-order-id_[value],
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

    private static String STR_ACCCOUNT_NO = "account-no";

    private static String STR_SEND_MARKET_ORDER_ID = "send-market-order-id";
    private static String STR_SEND_MARKET_ORDER_REQUEST_ID = "send-market-order-request-id";
    private static String STR_SEND_MARKET_ORDER_TICKET = "send-market-order-ticket";

    private static String STR_SEND_PENDING_ORDER_ID = "send-pending-order-id";
    private static String STR_SEND_PENDING_ORDER_REQUEST_ID = "send-pending-order-request-id";
    private static String STR_SEND_PENDING_ORDER_TICKET = "send-pending-order-ticket";

    private static String STR_MODIFY_STOPLOSS_ORDER_ID = "modify-stoploss-order-id";
    private static String STR_MODIFY_STOPLOSS_ORDER_REQUEST_ID = "modify-stoploss-order-request-id";

    private static String STR_MODIFY_TARGET_ORDER_ID = "modify-target-order-id";
    private static String STR_MODIFY_TARGET_ORDER_REQUEST_ID = "modify-target-order-request-id";

    private static String STR_MODIFY_ENTRY_PRICE_ORDER_ID = "modify-entry-price-order-id";
    private static String STR_MODIFY_ENTRY_PRICE_ORDER_REQUEST_ID = "modify-entry-price-order-request-id";

    private static String STR_MODIFY_HEDGE_ORDER_ID = "modify-hedge-order-id";
    private static String STR_MODIFY_HEDGE_ORDER_REQUEST_ID = "modify-hedge-order-request-id";

    private static String STR_DELETE_PENDING_ORDER_ID = "delete-pending-order-id";
    private static String STR_DELETE_PENDING_ORDER_REQUEST_ID = "delete-pending-order-request-id";

    private static String STR_CLOSE_MARKET_ORDER_ID = "close-market-order-id";
    private static String STR_CLOSE_MARKET_ORDER_REQUEST_ID = "close-market-order-request-id";

    private static String DASH_SEP = "_";
    private static String COMMA_SEP = "_";

    static private String decodeBaseOrderIDFrom(String clOrderID, String match) {

        String[] comma_split = clOrderID.split(COMMA_SEP);
        StringBuilder str = new StringBuilder();
        for (String token : comma_split) {
            str.append(token);
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
                var family = idMap.getOrDefault(clOrderId,
                        new MarketOrderIDFamily(marketId));
                
                family.modifyStoplossOrderIDs.add(clOrderId);
                idMap.put(marketId, family);
            } else if (isTargetOrderID(clOrderId)) {
                String marketId = decodeMarketOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(clOrderId,
                        new MarketOrderIDFamily(marketId));
                
                family.modifyTargetOrderIDs.add(clOrderId);
                idMap.put(marketId, family);
            } else if (isCloseOrderID(clOrderId)) {
                String marketId = decodeMarketOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(clOrderId,
                        new MarketOrderIDFamily(marketId));
                
                family.closeOrderIDs.add(clOrderId);
                idMap.put(marketId, family);
            }
        }

        return new LinkedList(idMap.values());
    }

    static public List<PendingOrderIDFamily> groupByPendinOrderIDFamily(List<String> order_ids) {

        Map<String, PendingOrderIDFamily> idMap = new LinkedHashMap();

        for (int i = 0; i < order_ids.size(); i++) {
            String clOrderId = order_ids.get(i);
            if (isMarketOrderID(clOrderId)) {
                var family = idMap.getOrDefault(clOrderId,
                        new PendingOrderIDFamily(clOrderId));
                
                idMap.put(clOrderId, family);
            } else if (isStoplossOrderID(clOrderId)) {
                String marketId = decodeMarketOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(clOrderId,
                        new PendingOrderIDFamily(marketId));
                
                family.modifyStoplossOrderIDs.add(clOrderId);
                idMap.put(marketId, family);
            } else if (isTargetOrderID(clOrderId)) {
                String marketId = decodeMarketOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(clOrderId,
                        new PendingOrderIDFamily(marketId));
                
                family.modifyTargetOrderIDs.add(clOrderId);
                idMap.put(marketId, family);
            }  else if (isEntryPriceOrderID(clOrderId)) {
                String marketId = decodeMarketOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(clOrderId,
                        new PendingOrderIDFamily(marketId));
                
                family.modifyEntryPriceOrderIDs.add(clOrderId);
                idMap.put(marketId, family);
            } else if (isDeleteOrderID(clOrderId)) {
                String marketId = decodeMarketOrderIDFrom(clOrderId);
                var family = idMap.getOrDefault(clOrderId,
                        new PendingOrderIDFamily(marketId));
                
                family.deleteOrderIDs.add(clOrderId);
                idMap.put(marketId, family);
            }
        }

        return new LinkedList(idMap.values());
    }

    static public boolean checkID(String clOrderID, String match_name) {
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

    static public String getValue(String clOrderID, String match_name) {
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
        return checkID(clOrderID, STR_MODIFY_STOPLOSS_ORDER_ID);
    }

    static public boolean isTargetOrderID(String clOrderID) {
        return checkID(clOrderID, STR_MODIFY_TARGET_ORDER_ID);
    }

    static public boolean isCloseOrderID(String clOrderID) {
        return checkID(clOrderID, STR_CLOSE_MARKET_ORDER_ID);
    }

    static public boolean isEntryPriceOrderID(String clOrderID) {
        return checkID(clOrderID, STR_MODIFY_ENTRY_PRICE_ORDER_ID);
    }

    static public boolean isModifyHedgeOrderID(String clOrderID) {
        return checkID(clOrderID, STR_MODIFY_HEDGE_ORDER_ID);
    }

    static public boolean isDeleteOrderID(String clOrderID) {
        return checkID(clOrderID, STR_DELETE_PENDING_ORDER_ID);
    }

    static public int getAccountNumberFromOrderID(String clOrdId) {
        String acc_no = getValue(clOrdId, STR_ACCCOUNT_NO);
        if (acc_no == null) {
            return -1;
        }
        return Integer.parseInt(acc_no);
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
                + STR_SEND_MARKET_ORDER_ID + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_SEND_MARKET_ORDER_REQUEST_ID + DASH_SEP + request_identifier + COMMA_SEP
                + STR_SEND_MARKET_ORDER_TICKET + DASH_SEP + System.currentTimeMillis();
    }

    static public String createPendingOrderID(int accountNumber, String request_identifier) throws SQLException {
        return STR_ACCCOUNT_NO + DASH_SEP + accountNumber + COMMA_SEP
                + STR_SEND_PENDING_ORDER_ID + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_SEND_PENDING_ORDER_REQUEST_ID + DASH_SEP + request_identifier + COMMA_SEP
                + STR_SEND_PENDING_ORDER_TICKET + DASH_SEP + System.currentTimeMillis();

    }

    static public String createModifyStoplossOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return marketOrPendingOrderID + COMMA_SEP
                + STR_MODIFY_STOPLOSS_ORDER_ID + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_MODIFY_STOPLOSS_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createModifyStoplossOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return OrderIDUtil.createModifyStoplossOrderID(order.getOrderID(), request_identifier);
    }

    static public String createModifyTargetOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return marketOrPendingOrderID + COMMA_SEP
                + STR_MODIFY_TARGET_ORDER_ID + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_MODIFY_TARGET_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createModifyTargetOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return OrderIDUtil.createModifyTargetOrderID(order.getOrderID(), request_identifier);
    }

    static public String createModifyEntryPriceOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return marketOrPendingOrderID + COMMA_SEP
                + STR_MODIFY_ENTRY_PRICE_ORDER_ID + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_MODIFY_ENTRY_PRICE_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createModifyEntryPriceOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return OrderIDUtil.createModifyEntryPriceOrderID(order.getOrderID(), request_identifier);
    }

    static public String createModifyHedgeOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return marketOrPendingOrderID + COMMA_SEP
                + STR_MODIFY_HEDGE_ORDER_ID + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_MODIFY_HEDGE_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createModifyHedgeOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return createModifyHedgeOrderID(order.getOrderID(), request_identifier);
    }

    static public String createDeleteOrderID(String pendingOrderID, String request_identifier) throws SQLException {
        return pendingOrderID + COMMA_SEP
                + STR_DELETE_PENDING_ORDER_ID + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
                + STR_DELETE_PENDING_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createDeleteOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return createDeleteOrderID(order.getOrderID(), request_identifier);
    }

    static public String createCloseOrderID(String marketOrderID, String request_identifier) throws SQLException {
        return marketOrderID + COMMA_SEP
                + STR_CLOSE_MARKET_ORDER_ID + DASH_SEP + OrderDB.getNewID() + COMMA_SEP
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
        //first check if is target modification
        String val = getValue(orderID, STR_MODIFY_TARGET_ORDER_REQUEST_ID);
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
