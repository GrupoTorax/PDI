package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Channel for splitting channels
 */
public class SplitChannelProcess extends ImageProcess<Image[]> {
    
    /** Output images */
    private final Image[] output;

    /**
     * Creates a new split channel process
     * 
     * @param image
     */
    public SplitChannelProcess(Image image) {
        super(image);
        this.output = new Image[4];
        for (int i = 0; i < output.length; i++) {
            output[i] = ImageFactory.buildEmptyImage(1, image.getWidth(), image.getHeight(), image.getPixelValueRange());
        }
        setOutput(output);
        setFinalizer(() -> {
            setOutput(output);
        });
    }

    @Override
    protected void processImage() {
        for (int channel = 0; channel < image.getChannelCount(); channel++) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    process(channel, x, y, image.get(channel, x, y));
                }    
            }
        }
    }
    
    /**
     * Process a pixel
     * 
     * @param channel
     * @param x
     * @param y
     * @param value
     */
    private void process(int channel, int x, int y, int value) {
        output[channel].set(0, x, y, value);
    }
    
}
