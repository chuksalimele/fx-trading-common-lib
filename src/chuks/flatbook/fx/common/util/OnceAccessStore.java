/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 * @param <X>
 * @param <Y>
 */
public class OnceAccessStore<X, Y> {

    Map<X, Y> storeMap = Collections.synchronizedMap(new LinkedHashMap());
    List<X> storeList = Collections.synchronizedList(new LinkedList());

    public void addItem(X item) {
        storeList.add(item);
    }

    public void put(X key, Y value) {
        storeMap.put(key, value);
    }

    public X getListItemAndDelete(X item) {

        for (int i = 0; i < storeList.size(); i++) {
            Object obj = storeList.get(i);
            if (storeList.get(i).equals(item)) {
                storeList.remove(i);
                return (X) obj;
            }
        }

        return null;
    }

    public Y getMappedItemAndDelete(X key) {
        return storeMap.remove(key); //removes and returns the value the key is mapped to
    }
}
