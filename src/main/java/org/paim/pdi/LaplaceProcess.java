package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Edge detection using the Laplace method
 */
public class LaplaceProcess extends MaskConvolutionProcess {

    /**
     * Creates a new Edge detection using the Laplace method
     *
     * @param image
     */
    public LaplaceProcess(Image image) {
        super(image);
    }

    @Override
    public double[][] getMask() {
        return new double[][] {{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
    }

}