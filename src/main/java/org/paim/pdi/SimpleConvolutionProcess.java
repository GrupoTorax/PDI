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
                            int ix = x + lx - 1;
                            int iy = y + ly - 1;
                            if (ix < 0 || iy < 0 || ix >= image.getWidth() || iy >= image.getHeight()) {
                                neighbours[lx][ly] = getNearestNeighbour(channel, ix, iy);
                            } else {
                                neighbours[lx][ly] = image.get(channel, ix, iy);
                            }
                        }
                    }
                    int newValue = computeCenter(neighbours);
                    outputImage.set(channel, x, y, newValue);
                }
            }
        }
    }
    
    /**
     * Returns the nearest neighbour
     * 
     * @param channel
     * @param x
     * @param y
     * @return int
     */
    private int getNearestNeighbour(int channel, int x, int y) {
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x >= image.getWidth()) {
            x = image.getWidth() - 1;
        }
        if (y >= image.getHeight()) {
            y = image.getHeight() - 1;
        }
        return image.get(channel, x, y);
    }

    protected abstract int computeCenter(int[][] neighbours);

}
