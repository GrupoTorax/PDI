package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Convolution mask process
 */
public abstract class ConvolutionProcess extends ImageProcess<Image> {

    /** Mask size */
    protected int maskSize;

    /**
     * Creates a new convolution process
     *
     * @param image
     * @param maskSize
     */
    public ConvolutionProcess(Image image, int maskSize) {
        super(image);
        setInitializer(() -> {
            setOutput(ImageFactory.buildEmptyImage(this.image));
        });
        this.maskSize = maskSize;
    }

    @Override
    public void processImage() {
        double[] kernel = getKernel();
        int[][][] pass = applyConvolution(kernel);
        for (int channel = 0; channel < image.getChannelCount(); channel++) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    getOutput().set(channel, x, y, pass[channel][x][y]);
                }
            }
        }
    }

    /**
     * Apply the convolution
     *
     * @param kernel
     * @return {@code int[][][]}
     */
    private int[][][] applyConvolution(double[] kernel) {
        int width = image.getWidth();
        int height = image.getHeight();
        int ignoredBorderLength = kernel.length / 2;
        int[][][] pass = new int[image.getChannelCount()][width][height];
        for (int channel = 0; channel < image.getChannelCount(); channel++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int xPass, yPass;
                    if ((x < ignoredBorderLength) || (x > width - ignoredBorderLength - 1)) {
                        xPass = image.get(channel, x, y);
                    } else {
                        xPass = 0;
                        for (int i = 0; i < kernel.length; i++) {
                            xPass += image.get(channel, (x - ignoredBorderLength) + i, y) * kernel[i];
                        }
                    }
                    if ((y < ignoredBorderLength) || (y > height - ignoredBorderLength - 1)) {
                        yPass = image.get(channel, x, y);
                    } else {
                        yPass = 0;
                        for (int i = 0; i < kernel.length; i++) {
                            yPass += image.get(channel, x, (y - ignoredBorderLength) + i) * kernel[i];
                        }
                    }
                    pass[channel][x][y] = (xPass + yPass) / 2;
                }
            }
        }
        return pass;
    }

    /**
     * Returns the Kernel
     *
     * @return double[]
     */
    protected abstract double[] getKernel();

}
