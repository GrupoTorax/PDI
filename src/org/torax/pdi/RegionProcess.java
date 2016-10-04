package org.torax.pdi;

import org.torax.commons.Image;
import org.torax.commons.ImageFactory;
import org.torax.commons.ImageRegion;

/**
 *
 * @param <O> output type
 */
public abstract class RegionProcess<O> extends ImageProcess<O> {
    
    /** Mask size */
    protected final int regionSize;
    
    /**
     * Creates a new region process
     * 
     * @param image 
     * @param regionSize
     */
    public RegionProcess(Image image, int regionSize) {
        super(image);
        this.regionSize = regionSize;
    }

    @Override
    public void processImage() {
        
    }
    
    /**
     * Process the region
     * 
     * @param imageRegion 
     */
    protected abstract void process(ImageRegion imageRegion);
    

    /**
     * Builds the region
     * 
     * @param x
     * @param y
     * @return ImageRegion
     */
    protected ImageRegion buildRegion(int x, int y) {
        final int ignoredBorderLength = regionSize / 2;
        ImageRegion region = new ImageRegion(ImageFactory.buildEmptyImage(image.getChannelCount(), regionSize, regionSize, image.getPixelValueRange()));
        for (int lx = 0; lx < regionSize; lx++) {
            for (int ly = 0; ly < regionSize; ly++) {
                int imageX = x - ignoredBorderLength + lx;
                int imageY = y - ignoredBorderLength + ly;
                if (imageX < 0 || imageY < 0 || imageX >= image.getWidth() || imageY >= image.getHeight()) {
                    region.set(0, lx, ly, 0);
                } else {
                    region.set(0, lx, ly, image.get(0, imageX, imageY));
                }
            }
        }
        return region;
    }
    
}
