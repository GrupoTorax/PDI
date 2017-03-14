package org.torax.pdi;

import org.torax.commons.Histogram;
import org.torax.commons.Image;

/**
 * Process for computing the Histogram of an Image
 */
public class HistogramProcess extends PixelProcess<Histogram> {

    /** Histogram data */
    private int[] histogramData;

    /**
     * Creates a new Histogram process
     *
     * @param image
     */
    public HistogramProcess(Image image) {
        super(image);
        setInitializer(this::initHistogramData);
        setFinalizer(() -> {
            setOutput(new Histogram(histogramData, image.getPixelValueRange()));
        });
    }

    /**
     * Initializes the histogram
     */
    private void initHistogramData() {
        int length = (int) image.getPixelValueRange().getLength();
        histogramData = new int[length];
        for (int i = 0; i < length; i++) {
            histogramData[i] = 0;
        }
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        int index = value + getOffset();
        if (index >= 0 && index < histogramData.length) {
            histogramData[value + getOffset()]++;
        }
    }

    /**
     * Returns the offset
     *
     * @return int
     */
    private int getOffset() {
        return (int) image.getPixelValueRange().getLower() * -1;
    }

}
