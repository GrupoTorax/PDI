package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Gaussian Blur process
 */
public class GaussianBlurProcess extends SimpleKernelConvolution {

    /** Sigma */
    private final double sigma;
    /** The mask size */
    private final int maskSize;

    /**
     * Creates a new Gaussian blur process
     *
     * @param image
     * @param sigma
     * @param maskSize
     */
    public GaussianBlurProcess(Image image, double sigma, int maskSize) {
        super(image);
        this.sigma = sigma;
        this.maskSize = maskSize;
    }

    @Override
    protected double[][] getKernel() {
        return GaussKernelGenerator.buildKernel2D(sigma, maskSize);
    }

}
