/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The CappedMap class is a customized LinkedHashMap that maintains a fixed number of entries.
 * When the map reaches its maximum size, the oldest entry (based on insertion order) is automatically
 * removed to allow new entries. This class can be useful in applications that require a
 * bounded cache or where memory usage needs to be controlled by retaining only a limited number
 * of entries.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * Usage Example:
 * <pre>
 *     CappedMap<String, Integer> cappedMap = new CappedMap<>(5); // Maximum of 5 entries
 *     cappedMap.put("one", 1);
 *     cappedMap.put("two", 2);
 *     // Adding more entries beyond max size will remove the oldest (insertion-order) entry
 * </pre>
 *
 * This map maintains insertion-order by default, meaning the entries are removed in the
 * order they were inserted. To change to access-order, modify the third argument in the 
 * LinkedHashMap constructor to 'true' instead of 'false'.
 *
 * Author: user
 */
public class CappedMap<K, V> extends LinkedHashMap<K, V> {

    /** The maximum number of entries this map can hold */
    private final int maxSize;

    /**
     * Constructs a CappedMap with a specified maximum size.
     * 
     * @param maxSize the maximum number of entries this map can contain; 
     *                once this limit is exceeded, the oldest entry is removed.
     */
    public CappedMap(int maxSize) {
        // false indicates insertion-order (change to true for access-order)
        super(maxSize, 0.75f, false);
        this.maxSize = maxSize;
    }

    /**
     * Determines if the oldest entry should be removed from the map.
     * This method is called by LinkedHashMap after each insertion.
     *
     * @param eldest the eldest entry in the map, which would be removed if this method returns true
     * @return true if the size of the map exceeds maxSize, signaling that the eldest entry should be removed
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
