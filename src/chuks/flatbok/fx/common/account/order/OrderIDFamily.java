/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.common.account.order;

import chuks.flatbok.fx.common.account.persist.OrderDB;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
public class OrderIDFamily {

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

    private static String STR_DELETE_PENDING_ORDER_ID = "delete-pending-order-id";
    private static String STR_DELETE_PENDING_ORDER_REQUEST_ID = "delete-pending-order-request-id";

    private static String STR_CLOSE_MARKET_ORDER_ID = "close-market-order-id";
    private static String STR_CLOSE_MARKET_ORDER_REQUEST_ID = "close-market-order-request-id";

    private static String DASH_SEP = "_";
    private static String COMMA_SEP = "_";

    static public String decodeMarketOrderIDFrom(String relatedOrderID) {

    }

    static public List<RelatedIDs> groupByRelatedOrderID(List<String> order_ids) {
        ArrayList<RelatedIDs> relatedIDList = new ArrayList();

        for (int i = 0; i < order_ids.size(); i++) {

            String market_id = decodeMarketOrderIDFrom(order_ids.get(i));
            if (market_id == null) {
                continue;
            }

            List<String> st_ids = new ArrayList();
            List<String> tg_ids = new ArrayList();
            List<String> cl_ids = new ArrayList();

            //get its last stoploss, target and close order attached to it
            for (int k = 0; k < order_ids.size(); k++) {
                if (i == k) {
                    continue;
                }

                String ord_id = order_ids.get(k);

                if (!isRelatedToMarketID(market_id, ord_id)) {
                    continue;
                }

                if (isStoplossOrderID(ord_id)) {
                    st_ids.add(ord_id);
                } else if (isTargetOrderID(ord_id)) {
                    tg_ids.add(ord_id);
                } else if (isCloseOrderID(ord_id)) {
                    cl_ids.add(ord_id);
                }
            }

            arrangeByNewer(st_ids);
            arrangeByNewer(tg_ids);
            arrangeByNewer(cl_ids);

            RelatedIDs relatedID = new RelatedIDs(market_id, tg_ids, st_ids, cl_ids);
            relatedIDList.add(relatedID);

        }

        return relatedIDList;
    }
    static public boolean isRelatedID(String relatedOrderID, String match_name) {
        String[] comma_split =  relatedOrderID.split(COMMA_SEP);
        for (String token : comma_split) {
            String[] dash_split = token.split(DASH_SEP);
            String name = dash_split[0];
            if(name.equals(match_name)){
                return true;
            }
        }
        return false;
    }
    
    static public String getValue(String clOrderID, String match_name) {
        String[] comma_split =  clOrderID.split(COMMA_SEP);
        for (String token : comma_split) {
            String[] dash_split = token.split(DASH_SEP);
            String name = dash_split[0];
            String value = dash_split[1];
            if(name.equals(match_name)){
                return value;
            }
        }
        return null;
    }
        
    static public boolean isStoplossOrderID(String relatedOrderID) {
        return isRelatedID(relatedOrderID, STR_MODIFY_STOPLOSS_ORDER_ID);
    }

    static public boolean isTargetOrderID(String relatedOrderID) {
        return isRelatedID(relatedOrderID, STR_MODIFY_TARGET_ORDER_ID);
    }

    static public boolean isCloseOrderID(String relatedOrderID) {
        return isRelatedID(relatedOrderID, STR_CLOSE_MARKET_ORDER_ID);
    }

    static public boolean isDeleteOrderID(String relatedOrderID) {
        return isRelatedID(relatedOrderID, STR_DELETE_PENDING_ORDER_ID);
    }

    static public int getAccountNumberFromOrderID(String clOrdId) {
        String acc_no = getValue(clOrdId, STR_ACCCOUNT_NO);
        if(acc_no== null){
            return -1;
        }
        return Integer.parseInt(acc_no);
    }

    static public long getMarketOrderTicket(String clOrdId) {
        String acc_no = getValue(clOrdId, STR_SEND_MARKET_ORDER_TICKET);
        if(acc_no == null){
            return -1;
        }
        return Long.parseLong(acc_no);
    }

    static public long getPendingOrderTicket(String clOrdId) {
        String acc_no = getValue(clOrdId, STR_SEND_PENDING_ORDER_TICKET);
        if(acc_no == null){
            return -1;
        }
        return Long.parseLong(acc_no);
    }

    static public String createMarketOrderID(int accountNumber, String request_identifier) throws SQLException {
        return STR_ACCCOUNT_NO + DASH_SEP + accountNumber + COMMA_SEP
                + STR_SEND_MARKET_ORDER_ID + DASH_SEP + OrderDB.getNewID()+ COMMA_SEP
                + STR_SEND_MARKET_ORDER_REQUEST_ID + DASH_SEP + request_identifier+ COMMA_SEP
                + STR_SEND_MARKET_ORDER_TICKET + DASH_SEP + System.currentTimeMillis();
    }

    static public String createPendingOrderID(int accountNumber, String request_identifier) throws SQLException {
        return STR_ACCCOUNT_NO + DASH_SEP + accountNumber + COMMA_SEP
                + STR_SEND_PENDING_ORDER_ID + DASH_SEP + OrderDB.getNewID()+ COMMA_SEP
                + STR_SEND_PENDING_ORDER_REQUEST_ID + DASH_SEP + request_identifier+ COMMA_SEP
                + STR_SEND_PENDING_ORDER_TICKET + DASH_SEP + System.currentTimeMillis();

    }

    static public String createStoplossOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return  marketOrPendingOrderID + COMMA_SEP
                + STR_MODIFY_STOPLOSS_ORDER_ID + DASH_SEP + OrderDB.getNewID()+ COMMA_SEP
                + STR_MODIFY_STOPLOSS_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createStoplossOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return createStoplossOrderID(order.getOrderID(), request_identifier);
    }

    
    static public String createTargetOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return  marketOrPendingOrderID + COMMA_SEP
                + STR_MODIFY_TARGET_ORDER_ID + DASH_SEP + OrderDB.getNewID()+ COMMA_SEP
                + STR_MODIFY_TARGET_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createTargetOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return createTargetOrderID(order.getOrderID(), request_identifier);
    }
    
    static public String createModifyHedgeOrderID(String marketOrPendingOrderID, String request_identifier) throws SQLException {
        return  marketOrPendingOrderID + COMMA_SEP
                + STR__MODIFY_HEDGE_ORDER_ID + DASH_SEP + OrderDB.getNewID()+ COMMA_SEP
                + STR__MODIFY_HEDGE_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createModifyHedgeOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return createModifyHedgeOrderID(order.getOrderID(), request_identifier);
    }

    static public String createDeleteOrderID(String pendingOrderID, String request_identifier) throws SQLException {
        return  pendingOrderID + COMMA_SEP
                + STR_DELETE_PENDING_ORDER_ID + DASH_SEP + OrderDB.getNewID()+ COMMA_SEP
                + STR_DELETE_PENDING_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createDeleteOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return createDeleteOrderID(order.getOrderID(), request_identifier);
    }

    static public String createCloseOrderID(String marketOrderID, String request_identifier) throws SQLException {
        return  marketOrderID + COMMA_SEP
                + STR_CLOSE_MARKET_ORDER_ID + DASH_SEP + OrderDB.getNewID()+ COMMA_SEP
                + STR_CLOSE_MARKET_ORDER_REQUEST_ID + DASH_SEP + request_identifier;
    }

    static public String createCloseOrderID(ManagedOrder order, String request_identifier) throws SQLException {
        return createCloseOrderID(order.getOrderID(), request_identifier);
    }

    static private String extractUniqueSuffix(String relatedOrderID) {
        String[] split = relatedOrderID.split(SEPARATOR);
        if (split.length < 4) {
            return null;
        }
        return split[3];
    }

    static private boolean isRelatedToMarketID(String market_id, String ord_id) {
        String decoded_id = decodeMarketOrderIDFrom(ord_id);
        return market_id.equals(decoded_id);
    }

    static private void arrangeByNewer(List<String> ids) {
        ids.sort((String a, String b) -> {
            int suffix_a = Integer.parseInt(extractUniqueSuffix(a));
            int suffix_b = Integer.parseInt(extractUniqueSuffix(b));
            return Integer.compare(suffix_a, suffix_b);
        });
    }

    static String getMarketOrderRequestIdentifier(String orderID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static String getPendingOrderRequestIdentifier(String orderID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static String getModifyOrderRequestIdentifier(String orderID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static String getDeleteOrderRequestIdentifier(String orderID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static String getCloseOrderRequestIdentifier(String orderID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static class RelatedIDs {

        String marketOrderID;
        List<String> targetOrderIDs;
        List<String> stoplossOrderIDs;
        List<String> closeOrderIDs;

        private RelatedIDs(String market_id, List<String> tg_ids, List<String> st_ids, List<String> cl_ids) {
            marketOrderID = market_id;
            targetOrderIDs = tg_ids;
            stoplossOrderIDs = st_ids;
            closeOrderIDs = cl_ids;
        }

        public String getMarketOrderID() {
            return marketOrderID;
        }

        public List<String> getStoplossOrderIDList() {
            return stoplossOrderIDs;
        }

        public List<String> getTargetOrderIDList() {
            return targetOrderIDs;
        }

        public List<String> getCloseOrderIDList() {
            return closeOrderIDs;
        }
    }
}
