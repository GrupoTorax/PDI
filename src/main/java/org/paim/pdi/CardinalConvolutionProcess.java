package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Convolution process that uses cardinal masks (N, S, E, W, NE, etc)
 */
public abstract class CardinalConvolutionProcess extends ImageProcess<Image> {

    /** Gray scale image */
    private final Image outputImage;
    /** Masks */
    private double[][][] masks;

    /**
     * Creates a new Roberts process
     *
     * @param image
     */
    public CardinalConvolutionProcess(Image image) {
        super(image);
        outputImage = ImageFactory.buildEmptyImage(image);
        setFinalizer(() -> {
            setOutput(outputImage);
        });
    }

    /**
     * Returns the masks
     *
     * @return double[][][]
     */
    protected abstract double[][][] getMasks();

    @Override
    protected void processImage() {
        masks = getMasks();
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
        double[] gradients = new double[masks.length];
        for (int lx = 0; lx < 3; lx++) {
            for (int ly = 0; ly < 3; ly++) {
                int valor = image.get(channel, x + lx - 1, y + ly - 1);
                for (int i = 0; i < gradients.length; i++) {
                    gradients[i] += (double) valor * masks[i][lx][ly];
                }
            }
        }
        double gradiente = 0;
        for (int i = 0; i < gradients.length; i++) {
            gradiente = Math.max(gradients[i], gradiente);
        }
        return (int) gradiente;
    }

}
