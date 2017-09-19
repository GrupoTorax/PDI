package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Abstract process that uses a mask for convolution
 */
public abstract class MaskConvolutionProcess extends ImageProcess<Image> {

    /** Gray scale image */
    private final Image outputImage;
    /** Mask */
    private double[][] mask;

    /**
     * Creates a new mask convolution process
     *
     * @param image
     */
    public MaskConvolutionProcess(Image image) {
        super(image);
        outputImage = ImageFactory.buildEmptyImage(image);
        setFinalizer(() -> {
            setOutput(outputImage);
        });
    }
    
    /**
     * Returns the mask
     * 
     * @return double[][]
     */
    public abstract double[][] getMask();

    @Override
    protected void processImage() {
        mask = getMask();
        for (int channel = 0; channel < image.getChannelCount(); channel++) {
            for (int x = 1; x < image.getWidth() - 1; x++) {
                for (int y = 1; y < image.getHeight() - 1; y++) {
                    int newValue = computePixel(channel, x, y);
                    outputImage.set(channel, x, y, newValue);
                }
            }
        }
    }

    /**
     * Computes the pixel
     *
     * @param channel
     * @param x
     * @param y
     * @return int
     */
    protected int computePixel(int channel, int x, int y) {
        double gradient = 0.0d;
        double gradientY = 0.0d;
        for (int lx = 0; lx < 3; lx++) {
            for (int ly = 0; ly < 3; ly++) {
                int value = image.get(channel, x + lx - 1, y + ly - 1);
                gradient += value * mask[lx][ly];
            }
        }
        gradient /= 9;
        return (int) Math.sqrt(Math.pow(gradient, 2) + Math.pow(gradientY, 2));
    }

}
