package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Process for converting a image to gray scale using the simple average
 */
public class WeightedGrayscaleProcess extends ImageProcess<Image> {

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
    public void processImage() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.isRGB()) {
                    int color = (int) (image.get(Image.CHANNEL_RED, x, y) * redWeight + 
                            image.get(Image.CHANNEL_GREEN, x, y) * greenWeight + 
                            image.get(Image.CHANNEL_BLUE, x, y) * blueWeight + 0.5);
                    grayImage.set(Image.CHANNEL_GRAY, x, y, color);
                } else {
                    grayImage.set(Image.CHANNEL_GRAY, x, y, image.get(Image.CHANNEL_GRAY, x, y));
                }
            }
        }
    }

}
