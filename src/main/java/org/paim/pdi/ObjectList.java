/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.paim.pdi;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Object list identified by the process
 */
public class ObjectList extends ArrayList<ExtractedObject> {

    /**
     * Creates a new Object list based on a collection
     *
     * @param collection
     */
    public ObjectList(Collection<ExtractedObject> collection) {
        super(collection);
    }

    /**
     * Sorts by size in decrescent order (Largest first)
     *
     * @return the same list
     */
    public ObjectList sortBySizeLargestFirst() {
        sort((ExtractedObject o1, ExtractedObject o2) -> {
            return o2.getSize() - o1.getSize();
        });
        return this;
    }

    @Override
    public ObjectList subList(int fromIndex, int toIndex) {
        return new ObjectList(super.subList(fromIndex, toIndex));
    }

    /**
     * Returns a subset of this list
     *
     * @param size
     * @return ObjectList
     */
    public ObjectList subList(int size) {
        return new ObjectList(super.subList(0, size));
    }

}
