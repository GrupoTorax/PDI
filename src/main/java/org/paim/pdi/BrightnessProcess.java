package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;
import org.paim.commons.Range;

/**
 * Process for adjusting the brightness of the image
 */
public class BrightnessProcess extends PixelProcess<Image> {

    /** Output image */
    private final Image outputImage;
    /** Brightness */
    private final int brightness;

    /**
     * Creates a new brightness process
     *
     * @param image
     * @param brightness
     */
    public BrightnessProcess(Image image, Integer brightness) {
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
        this.brightness = brightness == null ? 0 : brightness;
        this.outputImage = resultImage;
        setFinalizer(() -> {
            setOutput(outputImage);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        Range<Integer> range = outputImage.getPixelValueRange();
        outputImage.set(channel, x, y, range.limit((int) (value + brightness)));
    }

    /**
     * Returns the output image
     * 
     * @return Image
     */
    public Image getImage() {
        return super.getOutput();
    }
    
}
