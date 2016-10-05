package org.torax.pdi;

import org.torax.commons.BinaryLabeling;
import org.torax.commons.Image;

/**
 * Banary labeling process
 */
public class BinaryLabelingProcess extends PixelProcess<BinaryLabeling> {

    /** Output */
    private final Image out;
    /** Index label */
    private int indexLabel;

    public BinaryLabelingProcess(Image image) {
        super(image);
        this.out = new Image(this.image);
        this.indexLabel = 2;
        setFinalizer(() -> {
            setOutput(new BinaryLabeling(out, indexLabel - 2));
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        if (out.get(channel, x, y) == 1) {
            compareLabel(channel, x, y, indexLabel++);
        }
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
        if (x < 0 || x >= out.getWidth() || 
                y < 0 || y >= out.getHeight()) {
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
