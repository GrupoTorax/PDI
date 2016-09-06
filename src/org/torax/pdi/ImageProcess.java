package org.torax.pdi;

import org.torax.commons.Image;

/**
 * Process that works on top of a image
 */
public abstract class ImageProcess implements Process {

    /** Image */
    protected Image image;

    /**
     * Creates a new image process
     * 
     * @param image 
     */
    public ImageProcess(Image image) {
        this.image = image;
    }

}
