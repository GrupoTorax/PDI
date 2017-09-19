package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Convolution process with X and Y masks
 */
public abstract class XYConvolutionProcess extends ImageProcess<Image> {

    /** Gray scale image */
    private final Image outputImage;
    /** X Mask */
    private double[][] xMask;
    /** Y Mask */
    private double[][] yMask;

    /**
     * Creates a new Roberts process
     *
     * @param image
     */
    public XYConvolutionProcess(Image image) {
        super(image);
        outputImage = ImageFactory.buildEmptyImage(image);
        setFinalizer(() -> {
            setOutput(outputImage);
        });
    }
    
    /**
     * Returns the X mask
     * 
     * @return double[][]
     */
    public abstract double[][] getXMask();
    
    /**
     * Returns the Y mask
     * 
     * @return double[][]
     */
    public abstract double[][] getYMask();

    @Override
    protected void processImage() {
        xMask = getXMask();
        yMask = getYMask();
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
        double gradientX = 0.0d;
        double gradientY = 0.0d;
        for (int lx = 0; lx < 3; lx++) {
            for (int ly = 0; ly < 3; ly++) {
                int value = image.get(channel, x + lx - 1, y + ly - 1);
                gradientX += value * xMask[lx][ly];
                gradientY += value * yMask[lx][ly];
            }
        }
        gradientX /= 9;
        gradientY /= 9;
        return (int) Math.sqrt(Math.pow(gradientX, 2) + Math.pow(gradientY, 2));
    }

}
