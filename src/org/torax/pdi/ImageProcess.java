package org.torax.pdi;

import org.torax.commons.Image;

/**
 *
 */
public abstract class ImageProcess implements Process {

    protected Image image;

    public ImageProcess(Image image) {
        this.image = image;
    }

}
