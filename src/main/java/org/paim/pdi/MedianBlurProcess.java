package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Blur using the Median of the values
 */
public class MedianBlurProcess  extends SimpleConvolutionProcess {

    /**
     * Creates a new Blur using the Median of the values process
     *
     * @param image
     */
    public MedianBlurProcess(Image image) {
        super(image);
    }

    @Override
    protected int computeCenter(int[][] neighbours) {
        int[] values = new int[9];
        for (int x = 0; x < neighbours.length; x++) {
            for (int y = 0; y < neighbours[x].length; y++) {
                int value = neighbours[x][y];
                insert(values, value);
            }
        }
        return values[values.length / 2];
    }
    
    /**
     * Insert a value in a ordered array
     * 
     * @param values
     * @param newValue 
     */
    private void insert(int[] values, int newValue) {
        for (int i = 0; i < values.length; i++) {
            // If the number should go in the current position
            if (i == values.length - 1 ||  newValue > values[i]) {
                // Relocate
                for (int j = values.length - 2; j >= i; j--) {
                    values[j + 1] = values[j];
                }
                values[i] = newValue;
                break;
            }
        }
    }

}