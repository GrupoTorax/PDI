/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.paim.pdi;

import org.paim.commons.BinaryImage;
import org.paim.commons.Image;

/**
 * Extracted object
 */
public class ExtractedObject {

    /** Label of this object */
    private final int label;
    /** Matrix of this object */
    private final BinaryImage matrix;
    /** Size */
    private int size;
    /** Perimeter */
    private int perimeter;

    /**
     * Creates a new extracted object
     *
     * @param label
     * @param out
     */
    public ExtractedObject(int label, Image out) {
        this.label = label;
        this.matrix = new BinaryImage(out, label);
        this.size = 0;
        this.perimeter = -1;
    }

    /**
     * Returns the label of this object
     *
     * @return int
     */
    public int getLabel() {
        return label;
    }

    /**
     * Returns the matrix of this object
     *
     * @return BinaryImage
     */
    public BinaryImage getMatrix() {
        return matrix;
    }

    /**
     * Increments the size
     */
    public void incrementSize() {
        size++;
    }
    
    /**
     * Returns the size of this object (Area)
     *
     * @return int
     */
    public int getSize() {
        return size;
    }

    public int getPerimeter() {
        if (perimeter == -1) {
            perimeter = 0;
            for (int x = 0; x < matrix.getWidth(); x++) {
                for (int y = 0; y < matrix.getHeight(); y++) {
                    if (matrix.get(x, y)) {
                        if (x == 0 || y == 0 || x == matrix.getWidth() - 1 || y == matrix.getHeight() - 1) {
                            perimeter++;
                        } else {
                            if (!matrix.get(x, y - 1) || !matrix.get(x - 1, y) || !matrix.get(x + 1, y) || !matrix.get(x, y + 1)) {
                                perimeter++;
                            }
                        }
                    }
                }
            }
        }
        return perimeter;
    }

    public double getCircularity() {
        double p = getPerimeter();
        double a = getSize();
        double c = (p * p) / (4 * Math.PI * a);
        return c;
    }

    @Override
    public String toString() {
        return "ExtractedObject{" + "label=" + label + ", matrix=" + matrix + ", size=" + size + '}';
    }

}
