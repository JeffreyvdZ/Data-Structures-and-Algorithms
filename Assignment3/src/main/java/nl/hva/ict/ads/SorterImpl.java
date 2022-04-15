package nl.hva.ict.ads;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SorterImpl<E> implements Sorter<E> {

    /**
     * Sorts all items by selection or insertion sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array
     * @param items
     * @param comparator
     * @return  the items sorted in place
     */
    public List<E> selInsSort(List<E> items, Comparator<E> comparator) {
        int n = items.size();
        // Loop as long as there are items.
        for (int i = 1; i < n; i++) {
            // Examine each item and compare it to items on its left
            for (int j = i; j > 0 && less(items.get(j), items.get(j - 1), comparator); j--) {
                //Insert the item in the correct position in the array.
                exch(items, j, j - 1);
            }
        }
        //Return the sorted list of items.
        return items;
    }

    // is firstItem < secondItem?
    private boolean less(E firstItem, E secondItem, Comparator<E> comparator) {
        if (firstItem == secondItem) return false;
        return comparator.compare(firstItem, secondItem) < 0;
    }

    // swap a.get(firstIndex) and a.get(secondIndex)
    private void exch(List<E> a, int firstIndex, int secondIndex) {
        E swap = a.get(firstIndex);
        a.set(firstIndex, a.get(secondIndex));
        a.set(secondIndex, swap);
    }

    /**
     * Sorts all items by quick sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array
     * @param items
     * @param comparator
     * @return  the items sorted in place
     */
    public List<E> quickSort(List<E> items, Comparator<E> comparator) {
        // sort the complete list of items from position 0 till size-1, encluding position size
        Collections.shuffle(items);
        this.quickSortPart(items, 0, items.size()-1, comparator);
        return items;
    }

    /**
     * Sorts all items between index positions 'from' and 'to' inclusive by quick sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array or other positions in items
     *
     * @param items
     * @param comparator
     * @return  the items sorted in place
     */
    private void quickSortPart(List<E> items, int from, int to, Comparator<E> comparator) {
        // quick sort the sublist of items between index positions 'from' and 'to' inclusive
        if (to <= from) return;
        int j = partition(items, from, to, comparator);
        quickSortPart(items, from, j-1, comparator);
        quickSortPart(items, j+1, to, comparator);
    }


    private int partition(List<E> items, int low, int high, Comparator<E> comparator) {
        int i = low;
        int j = high + 1;
        E itemLow = items.get(low);
        while (true) {

            // Scan i from left to right as long as i is smaller than the partitioning item.
            // When i is bigger than the partitioning item the loop will break
            while (less(items.get(++i), itemLow, comparator)) {
                if (i == high) break;
            }

            // Scan j from right to left as long as j is greater than the partitioning item.
            // Once j is less than the partitioning item the loop stops.
            while (less(itemLow, items.get(--j), comparator)) {
                if (j == low) break;
            }
            // if the pointers cross, break the loop
            if (i >= j) break;

            // Once the code reaches here we know that there was an item 'i'
            // which was bigger than the partitioning item and an item 'j' which was smaller than
            // the partitioning item. Which means those two items are out of place so
            // we need to swap them with eachother.
            // exchange items i and j
            exch(items, i, j);
        }
        // Partitioning process is complete, because the pointers have crossed
        // and we put the partitioning item at 'j' so that everything greater
        // than j is greater than the partitioning item and everything less than
        // j is less than the partitioning item.
        exch(items, low, j);
        return j;
    }

    /**
     * Identifies the lead collection of numTops items according to the ordening criteria of comparator
     * and organizes and sorts this lead collection into the first numTops positions of the list
     * with use of (zero-based) heapSwim and heapSink operations.
     * The remaining items are kept in the tail of the list, in arbitrary order.
     * Items are sorted 'in place' without use of an auxiliary list or array or other positions in items
     * @param numTops       the size of the lead collection of items to be found and sorted
     * @param items
     * @param comparator
     * @return              the items list with its first numTops items sorted according to comparator
     *                      all other items >= any item in the lead collection
     */
    public List<E> topsHeapSort(int numTops, List<E> items, Comparator<E> comparator) {
        // check 0 < numTops <= items.size()
        if (numTops <= 0) return items;
        else if (numTops > items.size()) return quickSort(items, comparator);

        // the lead collection of numTops items will be organised into a (zero-based) heap structure
        // in the first numTops list positions using the reverseComparator for the heap condition.
        // that way the root of the heap will contain the worst item of the lead collection
        // which can be compared easily against other candidates from the remainder of the list
        Comparator<E> reverseComparator = comparator.reversed();

        // initialise the lead collection with the first numTops items in the list
        for (int heapSize = 2; heapSize <= numTops; heapSize++) {
            // repair the heap condition of items[0..heapSize-2] to include new item items[heapSize-1]
            heapSwim(items, heapSize, reverseComparator);
        }

        // insert remaining items into the lead collection as appropriate
        for (int i = numTops; i < items.size(); i++) {
            // loop-invariant: items[0..numTops-1] represents the current lead collection in a heap data structure
            //  the root of the heap is the currently trailing item in the lead collection,
            //  which will lose its membership if a better item is found from position i onwards
            E item = items.get(i);
            E worstLeadItem = items.get(0);
            if (comparator.compare(item, worstLeadItem) < 0) {
                // item < worstLeadItem, so shall be included in the lead collection
                items.set(0, item);
                // demote worstLeadItem back to the tail collection, at the orginal position of item
                items.set(i, worstLeadItem);
                // repair the heap condition of the lead collection
                heapSink(items, numTops, reverseComparator);
            }
        }

        // the first numTops positions of the list now contain the lead collection
        // the reverseComparator heap condition applies to this lead collection
        // now use heapSort to realise full ordening of this collection
        for (int i = numTops-1; i > 0; i--) {
            // loop-invariant: items[i+1..numTops-1] contains the tail part of the sorted lead collection
            // position 0 holds the root item of a heap of size i+1 organised by reverseComparator
            // this root item is the worst item of the remaining front part of the lead collection

            // Swapping the first index with the last one
            exch(items, 0, i);

            // after the swap we need sink the new first item to the designated position
            heapSink(items, i, reverseComparator);
        }
        // alternatively we can realise full ordening with a partial quicksort:
        // quickSortPart(items, 0, numTops-1, comparator);

        return items;
    }

    /**
     * Repairs the zero-based heap condition for items[heapSize-1] on the basis of the comparator
     * all items[0..heapSize-2] are assumed to satisfy the heap condition
     * The zero-bases heap condition says:
     *                      all items[i] <= items[2*i+1] and items[i] <= items[2*i+2], if any
     * or equivalently:     all items[i] >= items[(i-1)/2]
     * @param items
     * @param heapSize
     * @param comparator
     */
    private void heapSwim(List<E> items, int heapSize, Comparator<E> comparator) {
        // we set the childindex to the last item added to the heap
        int childIndex = heapSize-1;
        // check if the childindex is not the highest element in the heap
        while(childIndex > 0){
            // we get the parent of the childindex, by subtracting it by 1 and dividing it by 2
            int parentIndex = (childIndex -1) /2;
            // if the parent is smaller than the child we will break
            if(less(items.get(parentIndex), items.get(childIndex), comparator)) break;
            // if the parent is bigger we will swap the 2 indexes.
            exch(items, childIndex, parentIndex);
            // we set the childIndex to the parentIndex to go further up the heap and do the same process again.
            childIndex = parentIndex;
        }
    }

    /**
     * Repairs the zero-based heap condition for its root items[0] on the basis of the comparator
     * all items[1..heapSize-1] are assumed to satisfy the heap condition
     * The zero-bases heap condition says:
     *                      all items[i] <= items[2*i+1] and items[i] <= items[2*i+2], if any
     * or equivalently:     all items[i] >= items[(i-1)/2]
     * @param items
     * @param heapSize
     * @param comparator
     */
    private void heapSink(List<E> items, int heapSize, Comparator<E> comparator) {
        // parentIndex will start at the top of the heap
        int parentIndex = 0;

        // Check if it has a child
        while(2 * parentIndex + 1 < heapSize) {
            // We set the childIndex
            int childIndex = 2 * parentIndex + 1;
            // checks if it has a second child and if it is lower.
            if (2 * parentIndex + 2 < heapSize && less(items.get(2 * parentIndex + 2), items.get(childIndex), comparator)) {
                // if the second child is lower, we will set this to the childindex
                childIndex = 2 * parentIndex + 2;
            }
            // we will check if the parent is lower than the child.
            if (less(items.get(parentIndex), items.get(childIndex), comparator)) break;
            // if the parentis bigger than the child, we will swap the 2
            exch(items, parentIndex, childIndex);
            // we set the childIndex to the parentIndex to go further down the heap and do the same process again.
            parentIndex = childIndex;
        }
    }

}
