package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Class responsible for the dilation process
 */
public class DilationProcess extends PixelProcess<Image> {

    /** The process kernel */
    private final int[][] kernel;
    /** The image result */
    private final Image imageResult;

    /**
     * Creates a new dilation process
     *
     * @param image
     */
    public DilationProcess(Image image) {
        super(image);
        this.kernel = buildKernel();
        this.imageResult = new Image(image);
        setFinalizer(() -> {
            setOutput(this.imageResult);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        int bigest = 0;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                if (kernel[i][j] == 0) {
                    continue;
                }
                int result = value + kernel[i][j];
                if (result > bigest) {
                    bigest = result;
                }
            }
        }
        if (bigest > 255) {
            bigest = 255;
        }
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                imageResult.set(channel, i, j, bigest);
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
