/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Edge detection using the Prewitt process
 */
public class PrewittProcess extends XYConvolutionProcess {

    /**
     * Creates a new Prewitt process
     *
     * @param image
     */
    public PrewittProcess(Image image) {
        super(image);
    }

    @Override
    public double[][] getXMask() {
        return new double[][]{{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
    }

    @Override
    public double[][] getYMask() {
        return new double[][]{{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};
    }

}
