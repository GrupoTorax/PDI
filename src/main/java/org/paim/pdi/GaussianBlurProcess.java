package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Gaussian Blur process
 */
public class GaussianBlurProcess extends ConvolutionProcess {

    /** Sigma */
    private final double sigma;
    
    /**
     * Creates a new Gaussian blur process
     * 
     * @param image 
     * @param sigma 
     * @param maskSize 
     */
    public GaussianBlurProcess(Image image, double sigma, int maskSize) {
        super(image, maskSize);
        this.sigma = sigma;
    }

    @Override
    protected double[] getKernel() {
        return GaussKernelGenerator.buildKernel(sigma, maskSize);
    }

}
