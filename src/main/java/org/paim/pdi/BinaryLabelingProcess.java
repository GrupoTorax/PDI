package org.paim.pdi;

import java.util.HashMap;
import java.util.Map;
import org.paim.commons.BinaryLabeling;
import org.paim.commons.Image;

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

    /**
     * Creates a new BinaryLabeling process
     * 
     * @param image 
     */
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
     * Returns the last label used
     *
     * @return int
     */
    public int getLastLabel() {
        return indexLabel - INITIAL_LABEL;
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
    
    /**
     * Returns the objects extracted by this process
     * 
     * @return ObjectList
     */
    public ObjectList getExtractedObjects() {
        Map<Integer, ExtractedObject> map = new HashMap<>();
        for (int y = 0; y < out.getHeight(); y++) {
            for (int x = 0; x < out.getWidth(); x++) {
                int label = out.get(0, x, y);
                if (label < INITIAL_LABEL) {
                    continue;
                }
                ExtractedObject object = map.get(label);
                if (object == null) {
                    object = new ExtractedObject(label, out);
                    map.put(label, object);
                }
                object.incrementSize();
            }
        }
        return new ObjectList(map.values());
    }
    
}
