package org.paim.pdi;

import org.paim.commons.BinaryImage;
import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Process for applying a binary threshold on a image.
 * <p>
 * This process will return the lower boundary if the pixel is <b>lower than</b>
 * the threshold, and the higher boundary if the pixel is <b> higher than or 
 * equal to</b> the boundary.
 */
public class ThresholdProcess extends PixelProcess<BinaryImage> {
    
    /** */
    private final BinaryImage out;
    /** Threshold */
    private final int threshold;

    /**
     * Creates a new threshold process
     * 
     * @param image
     * @param threshold 
     */
    public ThresholdProcess(Image image, int threshold) {
        super(image);
        this.threshold = threshold;
        this.out = new BinaryImage(ImageFactory.buildBinaryImage(
                image.getWidth(), 
                image.getHeight()
        ));
        setFinalizer(() -> {
            setOutput(out);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        boolean higher = isHigherThreshold(value);
        out.set(x, y, higher);
    }

    /**
     * Returns true if the threshold is higher
     * 
     * @param value
     * @return boolean
     */
    private boolean isHigherThreshold(int value) {
        return value >= threshold;
    }

}
