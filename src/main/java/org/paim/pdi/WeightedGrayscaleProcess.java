package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Process for converting a image to gray scale using the simple average
 */
public class WeightedGrayscaleProcess extends PixelProcess<Image> {

    /** Gray scale image */
    private final Image grayImage;
    /** Red weight */
    private final double redWeight;
    /** Blue weight */
    private final double greenWeight;
    /** Green weight */
    private final double blueWeight;

    /**
     * Creates a new gray scale process
     *
     * @param image
     * @param redWeight
     * @param greenWeight
     * @param blueWeight
     */
    public WeightedGrayscaleProcess(Image image, double redWeight, double greenWeight, double blueWeight) {
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
        this.redWeight = redWeight;
        this.greenWeight = greenWeight;
        this.blueWeight = blueWeight;
        setFinalizer(() -> {
            setOutput(grayImage);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        int grayValue = grayImage.get(Image.CHANNEL_GRAY, x, y);
        if (channel == Image.CHANNEL_RED) {
            value = (int) (value * redWeight);
        }
        if (channel == Image.CHANNEL_GREEN) {
            value = (int) (value * greenWeight);
        }
        if (channel == Image.CHANNEL_BLUE) {
            value = (int) (value * blueWeight);
        }
        grayImage.set(Image.CHANNEL_GRAY, x, y, grayValue + (value / 3));
    }

    
}
