package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Resize image process
 */
public class ResizeProcess extends TransformProcess {

    /**
     * Create a new resize image process
     * 
     * @param image
     * @param sizeX 
     * @param sizeY 
     */
    public ResizeProcess(Image image, double sizeX, double sizeY) {
        super(image, new double[][]{
            {1 / sizeX, 0, 0},
            {0, 1 / sizeY, 0},
            {0, 0, 1}
        });
    }

}
