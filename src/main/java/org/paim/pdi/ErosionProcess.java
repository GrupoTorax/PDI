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
        int smallest = Integer.MAX_VALUE;
        for (int x = 0; x < neighbours.length; x++) {
            for (int y = 0; y < neighbours[x].length; y++) {
                if (neighbours[x][y] < smallest) {
                    smallest = neighbours[x][y];
                }
            }
        }
        return smallest;
    }

}
