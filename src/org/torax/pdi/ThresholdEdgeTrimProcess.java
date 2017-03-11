package org.torax.pdi;

import org.torax.commons.Image;

/**
 * Process for trimming the edges of an image based on a threshold
 */
public class ThresholdEdgeTrimProcess extends ImageProcess<Image> {

    /** Threshold */
    private final int threshold;
    /** Trim orientation */
    private final Orientation orientation;

    /**
     * Creates a new threshold edge trim process
     *
     * @param image
     * @param threshold
     */
    public ThresholdEdgeTrimProcess(Image image, int threshold, Orientation orientation) {
        super(image);
        this.threshold = threshold;
        this.orientation = orientation;
        setFinalizer(() -> {
            setOutput(this.image);
        });
    }

    @Override
    public void processImage() {
        for (int channel = 0; channel < image.getChannelCount(); channel++) {
            boolean rowColEmpty = false;
            if (orientation == Orientation.BOTTOM) {
                for (int y = image.getHeight() / 2; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        if (rowColEmpty) {
                            image.set(channel, x, y, 0);
                            continue;
                        }
                        if (image.get(channel, x, y) > threshold) {
                            break;
                        }
                        if (x == (image.getWidth() - 1)) {
                            rowColEmpty = true;
                        }

                    }
                }
            }
        }
    }

    /**
     * Trim orientation
     */
    public static enum Orientation {

        BOTTOM,
        // TOP, #Not implemented
        // LEFT, #Not implemented
        // RIGHT #Not implemented

    }

}
