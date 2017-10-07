package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Rotate image process
 */
public class RotateProcess extends TransformProcess {

    /**
     * Create a new rotate image process
     *
     * @param image
     * @param angle
     */
    public RotateProcess(Image image, int angle) {
        super(image, new double[][]{
            {Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)), 0},
            {-Math.sin(Math.toRadians(angle)), Math.cos(Math.toRadians(angle)), 0},
            {0, 0, 1}
        });
    }

}
