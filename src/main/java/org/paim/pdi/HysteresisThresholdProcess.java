package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Hysteresis Threshold
 */
public class HysteresisThresholdProcess extends SimpleConvolutionProcess {
    
    /** Low Threshold*/
    private int lowThreshold;
    /** High Threshold */ 
    private int highThreshold;

    /**
     * Creates a new Hysteresis Threshold Process
     * 
     * @param image
     * @param lowThreshold
     * @param highThreshold 
     */
    public HysteresisThresholdProcess(Image image, int lowThreshold, int highThreshold) {
        super(image);
    }

    @Override
    protected int computeCenter(int[][] neighbours) {
        if (neighbours[1][1] > highThreshold || neighbours[1][1] < lowThreshold) {
            return 0;
        }
        // check 8 neighboring pixels
        if ((neighbours[1][0] < highThreshold)
                && (neighbours[1][2] < highThreshold)
                && (neighbours[0][0] < highThreshold)
                && (neighbours[0][1] < highThreshold)
                && (neighbours[0][2] < highThreshold)
                && (neighbours[2][0] < highThreshold)
                && (neighbours[2][1] < highThreshold)
                && (neighbours[2][2] < highThreshold)) {
            return 0;
        }
        return neighbours[1][1];
    }

}
