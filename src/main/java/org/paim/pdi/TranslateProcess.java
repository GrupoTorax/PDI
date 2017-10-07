package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Translate process
 */
public class TranslateProcess extends TransformProcess {

    /**
     * Create a new translate image process
     * 
     * @param image
     * @param x 
     * @param y 
     */
    public TranslateProcess(Image image, int x, int y) {
        super(image, new double[][]{
            {1, 0, -x},
            {0, 1, -y},
            {0, 0, 1}
        });
    }

}
