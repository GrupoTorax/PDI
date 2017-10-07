package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Roberts process for edge detection
 */
public class RobertsProcess extends XYConvolutionProcess {

    /**
     * Creates a new Roberts process
     *
     * @param image
     */
    public RobertsProcess(Image image) {
        super(image);
    }

    @Override
    public double[][] getXMask() {
        return new double[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 0}};
    }

    @Override
    public double[][] getYMask() {
        return new double[][]{{-1, 0, 0}, {0, 1, 0}, {0, 0, 0}};
    }
    
}
