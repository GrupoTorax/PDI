package org.torax.pdi;

import org.torax.commons.Image;

/**
 * Process for casting a "shadow" on the image, based on a threshold
 */
public class ShadowCastingProcess extends ImageProcess<Image> {

    /** Threshold */
    private final int threshold;
    /** Trim orientation */
    private final Orientation[] orientations;

    /**
     * Creates a new shadow casting process
     *
     * @param image
     * @param threshold
     * @param orientations
     */
    public ShadowCastingProcess(Image image, int threshold, Orientation... orientations) {
        super(image);
        this.threshold = threshold;
        this.orientations = orientations;
        setFinalizer(() -> {
            setOutput(this.image);
        });
    }

    @Override
    public void processImage() {
        for (int channel = 0; channel < image.getChannelCount(); channel++) {
            for (Orientation orientation : orientations) {
                if (orientation == Orientation.RIGHT) {
                    for (int y = 0; y < image.getHeight(); y++) {
                        for (int x = 0; x < image.getWidth(); x++) {
                            if (image.get(channel, x, y) > threshold) {
                                break;
                            }
                            image.set(channel, x, y, threshold);
                        }
                    }
                    continue;
                }
                if (orientation == Orientation.LEFT) {
                    for (int y = 0; y < image.getHeight(); y++) {
                        for (int x = (image.getWidth() - 1); x > -1; x--) {
                            if (image.get(channel, x, y) > threshold) {
                                break;
                            }
                            image.set(channel, x, y, threshold);
                        }
                    }
                    continue;
                }
                if (orientation == Orientation.BOTTOM) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        for (int y = 0; y < image.getHeight(); y++) {
                            if (image.get(channel, x, y) > threshold) {
                                break;
                            }
                            image.set(channel, x, y, threshold);
                        }
                    }
                }
                if (orientation == Orientation.TOP) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        for (int y = (image.getHeight() - 1); y > -1; y--) {
                            if (image.get(channel, x, y) > threshold) {
                                break;
                            }
                            image.set(channel, x, y, threshold);
                        }
                    }
                    continue;
                }
            }
        }
    }

    /**
     * Shadow orientation
     */
    public static enum Orientation {

        BOTTOM,
        TOP,
        LEFT,
        RIGHT

    }

}
