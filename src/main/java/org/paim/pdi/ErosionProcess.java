package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Class responsible for the erosion process
 */
public class ErosionProcess extends SimpleConvolutionProcess {

    /**
     * Creates a new erosion process
     *
     * @param image
     */
    public ErosionProcess(Image image) {
        super(image);
    }

    @Override
    protected int computeCenter(int[][] neighbours) {
        int largest = 0;
        for (int x = 0; x < neighbours.length; x++) {
            for (int y = 0; y < neighbours[x].length; y++) {
                if (neighbours[x][y] > largest) {
                    largest = neighbours[x][y];
                }
            }
        }
        return largest;
    }

}
