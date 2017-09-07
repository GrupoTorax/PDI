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
            {size, 0, 0},
            {0, size, 0},
            {0, 0, 1}
        });
    }

}
