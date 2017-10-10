/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Frei-chen edge detection process
 */
public class FreiChenProcess extends CardinalConvolutionProcess {

    public FreiChenProcess(Image image) {
        super(image);
    }

    @Override
    protected double[][][] getMasks() {
        double sq2 = Math.sqrt(2);
        return new double[][][]{
            {
                {1, sq2, 1},
                {0, 0, 0},
                {-1, -sq2, -1}
            },
            {
                {1, 0, -1},
                {sq2, 0, -sq2},
                {1, 0, -1}
            },
            {
                {0, -1, sq2},
                {1, 0, -1},
                {-sq2, 1, 0}
            },
            {
                {sq2, -1, 0},
                {-1, 0, 1},
                {0, 1, -sq2}
            },
            {
                {0, 1, 0},
                {-1, 0, -1},
                {0, 1, 0}
            },
            {
                {-1, 0, 1},
                {0, 0, 0},
                {1, 0, -1}
            },
            {
                {1, -2, 1},
                {-2, 4, -2},
                {1, -2, 1}
            },
            {
                {-2, 1, -2},
                {1, 4, 1},
                {-2, 1, -2}
            },
            {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
            },
        };
    }

    @Override
    protected double[] getMaskWeights() {
        double sq2 = Math.sqrt(2);
        return new double[] {
            1d / (2d * sq2),
            1d / (2d * sq2),
            1d / (2d * sq2),
            1d / (2d * sq2),
            1d / 2d,
            1d / 2d,
            1d / 6d,
            1d / 6d,
            1d / 3d,
        };
    }

    @Override
    protected int processGradients(double[] gradients) {
        double[] weights = getMaskWeights();
        for (int i = 0; i < gradients.length; i++) {
            gradients[i] = gradients[i] * weights[i];
            gradients[i] = gradients[i] * gradients[i];
        }
        double M = (gradients[0] + gradients[1]) + (gradients[2] + gradients[3]);
	double S = (gradients[4] + gradients[5]) + (gradients[6] + gradients[7]) + (gradients[8] + M);
	return (int) (Math.sqrt(M/S) * outputImage.getPixelValueRange().getHigher());
        
    }

}
