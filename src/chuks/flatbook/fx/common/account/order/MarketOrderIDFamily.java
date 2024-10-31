/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.account.order;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author user
 */
  public class MarketOrderIDFamily {

        String marketOrderID;
        List<String> modifyStoplossOrderIDs = Collections.synchronizedList(new LinkedList());
        List<String> modifyTakeProfitOrderIDs = Collections.synchronizedList(new LinkedList());
        List<String> closeOrderIDs = Collections.synchronizedList(new LinkedList());

        private MarketOrderIDFamily() {
        }
        
        public MarketOrderIDFamily(String marketOrderID) {
            this.marketOrderID = marketOrderID;
        }

        public String getMarketOrderID() {
            return marketOrderID;
        }

        public List<String> getModifyStoplossOrderIDs() {
            return modifyStoplossOrderIDs;
        }

        public List<String> getModifyTakeProfitOrderIDs() {
            return modifyTakeProfitOrderIDs;
        }

        public List<String> getCloseOrderIDs() {
            return closeOrderIDs;
        }
    }

