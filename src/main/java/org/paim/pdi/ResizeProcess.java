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
     * @param size 
     */
    public ResizeProcess(Image image, double size) {
        super(image, new double[][]{
            {1 / size, 0, 0},
            {0, 1 / size, 0},
            {0, 0, 1}
        });
    }

}
