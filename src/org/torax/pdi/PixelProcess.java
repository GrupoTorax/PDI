package org.torax.pdi;

import org.torax.commons.Image;

/**
 *
 */
public abstract class PixelProcess extends ImageProcess {
    
    public PixelProcess(Image image) {
        super(image);
    }

    @Override
    public void process() {
        process(0);
    }
    
    protected abstract void process(int pixel);
    
}
