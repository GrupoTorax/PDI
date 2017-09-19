package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Convolution process
 */
public abstract class SimpleConvolutionProcess extends ImageProcess<Image> {

    /** Gray scale image */
    private final Image outputImage;

    /**
     * Creates a new Opening process
     *
     * @param image
     */
    public SimpleConvolutionProcess(Image image) {
        super(image);
        outputImage = ImageFactory.buildEmptyImage(image);
        setFinalizer(() -> {
            setOutput(outputImage);
        });
    }

    @Override
    protected void processImage() {
        for (int channel = 0; channel < image.getChannelCount(); channel++) {
            for (int x = 1; x < image.getWidth() - 1; x++) {
                for (int y = 1; y < image.getHeight() - 1; y++) {
                    int[][] neighbours = new int[3][3];
                    for (int lx = 0; lx < 3; lx++) {
                        for (int ly = 0; ly < 3; ly++) {
                            neighbours[lx][ly] = image.get(channel, x + lx - 1, y + ly - 1);
                        }
                    }
                    int newValue = computeCenter(neighbours);
                    outputImage.set(channel, x, y, newValue);
                }
            }
        }
    }

    protected abstract int computeCenter(int[][] neighbours);

}
