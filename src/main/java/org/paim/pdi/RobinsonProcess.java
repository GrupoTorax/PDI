package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Edge detection using the Robinson process
 */
public class RobinsonProcess extends CardinalConvolutionProcess {

    public RobinsonProcess(Image image) {
        super(image);
    }

    @Override
    protected double[][][] getMasks() {
        return new double[][][]{
            {
                {1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1}

            },
            {
                {0, 1, 2},
                {-1, 0, 1},
                {-2, -1, 0}

            },
            {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}

            },
            {
                {-2, -1, 0},
                {-1, 0, 1},
                {0, 1, 2}

            },
            {
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}

            },
            {
                {0, -1, -2},
                {1, 0, -1},
                {2, 1, 0}

            },
            {
                {1, 0, -1},
                {2, 0, -2},
                {1, 0, -1}
            },
            {
                {2, 1, 0},
                {1, 0, -1},
                {0, -1, -2}

            }
        };
    }

    @Override
    protected int computePixel(int channel, int x, int y) {
        int newValue = super.computePixel(channel, x, y);
        return newValue * 256 / 1024;
    }

}
