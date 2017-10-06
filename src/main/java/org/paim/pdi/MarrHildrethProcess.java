package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Marr and Hildreth process
 */
public class MarrHildrethProcess extends SimpleKernelConvolution {

    public MarrHildrethProcess(Image image) {
        super(image);
    }

    /**
     * Returns the kernel
     *
     * @return {@code double[][]}
     */
    @Override
    protected double[][] getKernel() {
        return new double[][]{
            {0, 0, -1, 0, 0},
            {0, -1, -2, -1, 0},
            {-1, -2, 16, -2, -1},
            {0, -1, -2, -1, 0},
            {0, 0, -1, 0, 0}
        };
    }

}
