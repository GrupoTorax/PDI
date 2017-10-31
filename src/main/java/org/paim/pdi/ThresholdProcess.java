package org.paim.pdi;

import org.paim.commons.BinaryImage;
import org.paim.commons.Image;

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
        this.out = new BinaryImage(image);
        setFinalizer(() -> {
            setOutput(out);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        out.set(channel, x, y, applyThreshold(value));
    }

    /**
     * Apply the threshold
     * 
     * @param value
     * @return 
     */
    private int applyThreshold(int value) {
        if (value < threshold) {
            return image.getPixelValueRange().getLower();
        } else {
            return image.getPixelValueRange().getHigher();
        }
    }

}
