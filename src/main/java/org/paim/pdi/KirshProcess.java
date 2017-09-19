package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Edge detection using the Kirsh process
 */
public class KirshProcess extends CardinalConvolutionProcess {

    public KirshProcess(Image image) {
        super(image);
    }

    @Override
    protected double[][][] getMasks() {
        return new double[][][]{
            {
                {5, 5, 5},
                {-3, 0, -3},
                {-3, -3, -3}

            },
            {
                {-3, 5, 5},
                {-3, 0, 5},
                {-3, -3, -3}

            },
            {
                {-3, -3, 5},
                {-3, 0, 5},
                {-3, -3, 5}

            },
            {
                {-3, -3, -3},
                {-3, 0, 5},
                {-3, 5, 5}

            },
            {
                {-3, -3, -3},
                {-3, 0, -3},
                {5, 5, 5}

            },
            {
                {-3, -3, -3},
                {5, 0, -3},
                {5, 5, -3}

            },
            {
                {5, -3, -3},
                {5, 0, -3},
                {5, -3, -3}
            },
            {
                {5, 5, -3},
                {5, 0, -3},
                {-3, -3, -3}

            }
        };
    }

    @Override
    protected int computePixel(int channel, int x, int y) {
        int newValue = super.computePixel(channel, x, y);
        return newValue * 256 / 3840;
    }

}
