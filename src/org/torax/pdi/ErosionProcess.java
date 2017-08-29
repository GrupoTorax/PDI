package org.torax.pdi;

import org.torax.commons.Image;

/**
 * Class responsible for the erosion process
 */
public class ErosionProcess extends PixelProcess<Image> {

    /** The process kernel */
    private final int[][] kernel;
    /** The image result */
    private final Image imageResult;
    
    /**
     * Creates a new erosion process
     *
     * @param image
     */
    public ErosionProcess(Image image) {
        super(image);
        this.kernel = buildKernel();
        this.imageResult = new Image(image);
        setFinalizer(() -> {
            setOutput(imageResult);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        int less = Integer.MAX_VALUE;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                if (kernel[i][j] == 0) {
                    continue;
                }
                int result = value + kernel[i][j];
                if (result < less) {
                    less = result;
                }
            }
        }
        if (less == 10) {
            less = 0;
        }
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                imageResult.set(channel, i, j, less);
            }
        }
    }

    /**
     * Returns the erosion
     *
     * @return
     */
    private int[][] buildKernel() {
        return new int[][]{
            {0, 10, 0},
            {10, 10, 10},
            {0, 10, 0}
        };
    }

}
