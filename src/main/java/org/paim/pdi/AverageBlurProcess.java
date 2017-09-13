package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Blur using the average of the values
 */
public class AverageBlurProcess  extends SimpleConvolutionProcess {

    /**
     * Creates a new Blur using the average of the values process
     *
     * @param image
     */
    public AverageBlurProcess(Image image) {
        super(image);
    }

    @Override
    protected int computeCenter(int[][] neighbours) {
        int sum = 0;
        for (int x = 0; x < neighbours.length; x++) {
            for (int y = 0; y < neighbours[x].length; y++) {
                sum += neighbours[x][y];
            }
        }
        return sum / 9;
    }    

}