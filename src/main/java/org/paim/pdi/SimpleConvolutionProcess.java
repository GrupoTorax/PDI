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
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int[][] neighbours = new int[3][3];
                    for (int lx = 0; lx < 3; lx++) {
                        for (int ly = 0; ly < 3; ly++) {
                            int ix = image.limitX(x + lx - 1);
                            int iy = image.limitY(y + ly - 1);
                            neighbours[lx][ly] = image.get(channel, ix, iy);
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
