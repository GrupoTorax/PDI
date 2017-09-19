package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Roberts process for edge detection
 */
public class SobelProcess extends XYConvolutionProcess {

    /**
     * Creates a new Roberts process
     *
     * @param image
     */
    public SobelProcess(Image image) {
        super(image);
    }

    @Override
    public double[][] getXMask() {
        return new double[][] {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
    }

    @Override
    public double[][] getYMask() {
        return new double[][] {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
    }
    
}
