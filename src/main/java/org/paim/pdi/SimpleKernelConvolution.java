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
        double[][] kernel = getKernel();
        for (int channel = 0; channel < image.getChannelCount(); channel++) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    processKernel(kernel, channel, x, y);
                }
            }
        }
    }
    
    /**
     * Process the kernel
     * 
     * @param kernel
     * @param channel
     * @param x
     * @param y 
     */
    protected void processKernel(double[][] kernel, int channel, int x, int y) {
        double sumValue = 0;
        double valueKernel = 0;
        int center = kernel.length - 2;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                int xPos = image.limitX(x + (i - center));
                int yPos = image.limitY(y + (j - center));
                sumValue += image.get(channel, xPos, yPos) * kernel[i][j];
                valueKernel += kernel[i][j];
            }
        }
        if (valueKernel > 0) {
            sumValue /= valueKernel;
        }
        int value = image.getPixelValueRange().limit((int) Math.round(sumValue));
        outputImage.set(channel, x, y, value);
    }

    /**
     * Returns the kernel
     *
     * @return {@Code double[][]}
     */
    protected abstract double[][] getKernel();
}
