package org.torax.pdi;

import org.torax.commons.Image;

/**
 * Process for applying a binary threshold on a image.
 * <p>
 * This process will return the lower boundary if the pixel is <b>lower than</b>
 * the threshold, and the higher boundary if the pixel is <b> higher than or
 * equal to</b> the boundary.
 */
public class ThresholdLimitProcess extends PixelProcess<Image> {

    /** Lower threshold */
    private final int lowerThreshold;
    /** Higher threshold */
    private final int higherThreshold;
    /** Lower replace value */
    private final int lowerReplaceValue;
    /** Higher replace value */
    private final int higherReplaceValue;

    /**
     * Creates a new threshold process
     *
     * @param image
     * @param lowerThreshold
     * @param higherThreshold
     * @param lowerReplaceValue
     * @param higherReplaceValue
     */
    public ThresholdLimitProcess(Image image, int lowerThreshold, int higherThreshold, int lowerReplaceValue, int higherReplaceValue) {
        super(image);
        this.lowerThreshold = lowerThreshold;
        this.higherThreshold = higherThreshold;
        this.lowerReplaceValue = lowerReplaceValue;
        this.higherReplaceValue = higherReplaceValue;
        setFinalizer(() -> {
            setOutput(image);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        image.set(channel, x, y, applyThreshold(value));
    }

    /**
     * Apply the threshold
     *
     * @param value
     * @return int
     */
    private int applyThreshold(int value) {
        if (value < lowerThreshold) {
            return lowerReplaceValue;
        } else {
            if (value > higherThreshold) {
                return higherReplaceValue;
            } else {
                return value;
            }
        }
    }

}
