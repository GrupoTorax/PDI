package org.torax.pdi;

import org.torax.commons.BinaryLabeling;
import org.torax.commons.Image;

/**
 * Binary labeling process
 */
public class BinaryLabelingProcess extends PixelProcess<BinaryLabeling> {

    /** Initial label */
    private static final int INITIAL_LABEL = 2;

    /** Output */
    private final Image out;
    /** Index label */
    private int indexLabel;

    public BinaryLabelingProcess(Image image) {
        super(image);
        this.out = new Image(this.image);
        this.indexLabel = INITIAL_LABEL;
        setFinalizer(() -> {
            setOutput(new BinaryLabeling(out, indexLabel - INITIAL_LABEL));
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        if (out.get(channel, x, y) == 1) {
            compareLabel(channel, x, y, indexLabel++);
        }
    }

    /**
     * Returns the label size
     *
     * @param label
     * @return int
     */
    public int getSize(int label) {
        int compareLabel = label + 1;
        int size = 0;
        for (int y = 0; y < out.getHeight(); y++) {
            for (int x = 0; x < out.getWidth(); x++) {
                if (out.get(0, x, y) == compareLabel) {
                    size++;
                }
            }
        }
        return size;
    }

    /**
     * Returns a binary matrix
     *
     * @param label
     * @return boolean[][]
     */
    public boolean[][] getMatrix(int label) {
        boolean[][] mat = new boolean[out.getWidth()][out.getHeight()];
        int compareLabel = label + 1;
        for (int y = 0; y < out.getHeight(); y++) {
            for (int x = 0; x < out.getWidth(); x++) {
                if (out.get(0, x, y) == compareLabel) {
                    mat[x][y] = true;
                }
            }
        }
        return mat;
    }

    /**
     * Execute the label comparison
     *
     * @param channel
     * @param x
     * @param y
     * @param index
     */
    private void compareLabel(int channel, int x, int y, int index) {
        if (x < 0 || x >= out.getWidth() || y < 0 || y >= out.getHeight()) {
            return;
        }
        if (out.get(channel, x, y) == 1) {
            out.set(channel, x, y, index);
            compareLabel(channel, x - 1, y, index);
            compareLabel(channel, x, y - 1, index);
            compareLabel(channel, x, y + 1, index);
            compareLabel(channel, x + 1, y, index);
        }
    }

}
