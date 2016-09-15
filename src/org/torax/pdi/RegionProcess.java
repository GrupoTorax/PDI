package org.torax.pdi;

import org.torax.commons.Image;
import org.torax.commons.ImageRegion;

/**
 *
 */
public abstract class RegionProcess extends ImageProcess {
    
    public RegionProcess(Image image) {
        super(image);
    }

    @Override
    public void processImage() {
        
    }
    
    protected abstract void process(ImageRegion imageRegion);
    
}
