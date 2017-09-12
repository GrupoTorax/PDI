package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Horizontal mirroring process
 */
public class HorizontalMirroringProcess extends TransformProcess {

    /**
     * Create a new horizontal mirroring image process
     *
     * @param image
     */
    public HorizontalMirroringProcess(Image image) {
        super(image, new double[][]{
            {-1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
        });
    }

}
