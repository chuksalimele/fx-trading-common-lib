package chuks.flatbook.fx.common.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * CappedList is a custom implementation of a list with a fixed capacity.
 * When the list reaches its maximum capacity, adding a new element
 * will automatically remove the oldest (first) element.
 *
 * @param <E> the type of elements in this list
 *
 * Usage Example:
 * <pre>
 *     CappedList<String> cappedList = new CappedList<>(3);
 *     cappedList.add("one");
 *     cappedList.add("two");
 *     cappedList.add("three");
 *     cappedList.add("four"); // "one" is removed, list contains ["two", "three", "four"]
 * </pre>
 */
public class CappedList<E> extends ArrayList<E> {

    /** The maximum capacity of the list */
    private final int maxSize;

    /**
     * Constructs a CappedList with a specified maximum capacity.
     * 
     * @param maxSize the maximum number of elements this list can contain
     */
    public CappedList(int maxSize) {
        super(maxSize);
        this.maxSize = maxSize;
    }

    /**
     * Adds an element to the end of the list. If the list size exceeds maxSize,
     * the oldest element (first element) is removed.
     *
     * @param element the element to be added to the list
     * @return true if the list changed as a result of the call
     */
    @Override
    public boolean add(E element) {
        // If adding the new element would exceed the max size, remove the oldest element
        if (size() >= maxSize) {
            remove(0);
        }
        return super.add(element);
    }
    
    /**
     * Adds all of the elements in the specified collection to this list.
     * If the resulting size exceeds maxSize, the oldest elements are removed.
     *
     * @param elements the collection of elements to add
     * @return true if the list changed as a result of the call
     */
    @Override
    public boolean addAll(Collection<? extends E> elements) {
        boolean modified = false;
        for (E element : elements) {
            modified |= add(element); // Use add() to ensure capacity is maintained
        }
        return modified;
    }
}
