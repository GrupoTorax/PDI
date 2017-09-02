package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;
import org.paim.commons.Range;

/**
 * Process for inverting the image colors
 */
public class InvertColorProcess extends PixelProcess<Image> {

    /** Inverted image */
    private final Image invertedImage;

    /**
     * Creates a new invert colors process
     *
     * @param image
     */
    public InvertColorProcess(Image image) {
        super(image);
        Image resultImage;
        if (image == null) {
            resultImage = ImageFactory.buildEmptyImage();
        } else {
            resultImage = ImageFactory.
                buildEmptyImage(image.getChannelCount(),
                        image.getWidth(),
                        image.getHeight(),
                        image.getPixelValueRange());
        }
        this.invertedImage = resultImage;
        setFinalizer(() -> {
            setOutput(invertedImage);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        Range<Integer> range = invertedImage.getPixelValueRange();
        int normalizedValue = value - range.getLower();
        int newValue = range.getLower() + ((int)range.getLength() + normalizedValue * -1);
        invertedImage.set(channel, x, y, newValue);
    }

    
}
