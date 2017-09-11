package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Class responsible for the dilation process
 */
public class DilationProcess extends SimpleConvolutionProcess {
   
    /**
     * Creates a new dilatation process
     *
     * @param image
     */
    public DilationProcess(Image image) {
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
