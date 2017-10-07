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
                {-sq2, 0, -sq2},
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
            1 / (2 * sq2),
            1 / (2 * sq2),
            1 / (2 * sq2),
            1 / (2 * sq2),
            1 / 2,
            1 / 2,
            1 / 6,
            1 / 6,
            1 / 3,
        };
    }

}
