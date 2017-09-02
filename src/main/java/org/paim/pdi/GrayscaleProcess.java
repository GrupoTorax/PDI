package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Process for converting a image to gray scale
 */
public class GrayscaleProcess extends PixelProcess<Image> {

    /** Gray scale image */
    private final Image grayImage;

    /**
     * Creates a new gray scale process
     *
     * @param image
     */
    public GrayscaleProcess(Image image) {
        super(image);
        Image resultImage;
        if (image == null) {
            resultImage = ImageFactory.buildEmptyImage();
        } else {
            resultImage = ImageFactory.
                buildEmptyImage(Image.CHANNELS_GRAYSCALE,
                        image.getWidth(),
                        image.getHeight(),
                        image.getPixelValueRange());
        }
        this.grayImage = resultImage;
        setFinalizer(() -> {
            setOutput(grayImage);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        int grayValue = grayImage.get(Image.CHANNEL_GRAY, x, y);
        grayImage.set(Image.CHANNEL_GRAY, x, y, grayValue + (value / 3));
    }

    
}
