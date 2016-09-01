package org.torax.pdi;

import org.torax.commons.Image;
import org.torax.commons.ImageRegion;

/**
 *
 */
public abstract class ConvolutionProcess extends RegionProcess {

    public ConvolutionProcess(Image image) {
        super(image);
    }

    @Override
    protected void process(ImageRegion imageRegion) {
    }
    
}
