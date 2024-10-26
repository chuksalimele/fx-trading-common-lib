/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.common.account.order;

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
        List<String> modifyTargetOrderIDs = Collections.synchronizedList(new LinkedList());
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

        public List<String> getModifyStoplossIDOrderIDs() {
            return modifyStoplossOrderIDs;
        }

        public List<String> getModifyTargetIDOrderIDs() {
            return modifyTargetOrderIDs;
        }

        public List<String> getModifyEntryPriceIDlossOrderIDs() {
            return modifyEntryPriceOrderIDs;
        }

        public List<String> getDeleteIDOrderIDs() {
            return deleteOrderIDs;
        }

    }

