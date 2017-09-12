package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Vertical mirroring process
 */
public class VerticalMirroringProcess extends TransformProcess {

    /**
     * Create a new vertical mirroring image process
     *
     * @param image
     */
    public VerticalMirroringProcess(Image image) {
        super(image, new double[][]{
            {1, 0, 0},
            {0, -1, 0},
            {0, 0, 1}
        });
    }

}
