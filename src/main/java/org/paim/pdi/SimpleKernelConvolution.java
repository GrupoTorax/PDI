package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Simple kernel convolution
 */
public abstract class SimpleKernelConvolution extends ImageProcess<Image> {
    
    /** Output image */
    private final Image outputImage;

    public SimpleKernelConvolution(Image image) {
        super(image);
        outputImage = ImageFactory.buildEmptyImage(image);
        setFinalizer(() -> {
            setOutput(outputImage);
        });
    }

    @Override
    public void processImage() {
        int[][] kernel = getKernel();
        int center = kernel.length - 2;
        for (int channel = 0; channel < image.getChannelCount(); channel++) {
            for (int x = center; x < image.getWidth() - center; x++) {
                for (int y = center; y < image.getHeight() - center; y++) {
                    processKernel(kernel, center, channel, x, y);
                }
            }
        }
    }
    
    /**
     * Process the kernel
     * 
     * @param kernel
     * @param center
     * @param channel
     * @param x
     * @param y 
     */
    protected void processKernel(int[][] kernel, int center, int channel, int x, int y) {
        int value = 0;
        int valueKernel = 0;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                value += image.get(channel, x + (i - center), y + (j - center)) * kernel[i][j];
                valueKernel += kernel[i][j];
            }
        }
        if (valueKernel > 0) {
            value /= valueKernel;
        }
        if (value > image.getPixelValueRange().getHigher()) {
            value = image.getPixelValueRange().getHigher();
        }
        if (value < image.getPixelValueRange().getLower()) {
            value = image.getPixelValueRange().getLower();
        }
        outputImage.set(channel, x, y, value);
    }

    /**
     * Returns the kernel
     *
     * @return {@Code int[][]}
     */
    protected abstract int[][] getKernel();
}
