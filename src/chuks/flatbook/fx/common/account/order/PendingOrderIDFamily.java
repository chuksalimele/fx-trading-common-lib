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
    public class PendingOrderIDFamily {

        String pendingOrderID;
        List<String> modifyStoplossOrderIDs = Collections.synchronizedList(new LinkedList());
        List<String> modifyTakeProfitOrderIDs = Collections.synchronizedList(new LinkedList());
        List<String> modifyEntryPriceOrderIDs = Collections.synchronizedList(new LinkedList());
        List<String> deleteOrderIDs = Collections.synchronizedList(new LinkedList());

        private PendingOrderIDFamily() {
        }

        public PendingOrderIDFamily(String pendingOrderID) {
            this.pendingOrderID = pendingOrderID;
        }
        
        public String getPendingOrderID() {
            return pendingOrderID;
        }

        public List<String> getModifyStoplossOrderIDs() {
            return modifyStoplossOrderIDs;
        }

        public List<String> getModifyTakeProfitOrderIDs() {
            return modifyTakeProfitOrderIDs;
        }

        public List<String> getModifyEntryPriceOrderIDs() {
            return modifyEntryPriceOrderIDs;
        }

        public List<String> getDeleteOrderIDs() {
            return deleteOrderIDs;
        }

    }

