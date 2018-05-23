/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;
import org.paim.commons.Range;

/**
 * Process for blending two images
 */
public class BlendProcess extends PixelProcess<Image> {

    /** Output image */
    private final Image outputImage;
    /** Secondary image */
    private final Image blendImage;
    /** Weight of the blend (0 = all left, 1 = all right) */
    private final float weight;
    
    /**
     * Creates a new brightness process
     *
     * @param image1
     * @param image2
     * @param weight
     */
    public BlendProcess(Image image1, Image image2, float weight) {
        super(image1);
        Image resultImage;
        if (image1 == null) {
            resultImage = ImageFactory.buildEmptyImage();
        } else {
            resultImage = ImageFactory.
                buildEmptyImage(image1.getChannelCount(),
                        image1.getWidth(),
                        image1.getHeight(),
                        image1.getPixelValueRange());
        }
        this.outputImage = resultImage;
        this.blendImage = image2;
        this.weight = weight;
        setFinalizer(() -> {
            setOutput(outputImage);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        if (blendImage == null || blendImage.getChannelCount() <= channel || blendImage.getWidth() <= x || blendImage.getHeight() <= y) {
            outputImage.set(channel, x, y, value);
            return;
        }
        int blendValue = blendImage.get(channel, x, y);
        Range<Integer> range = outputImage.getPixelValueRange();
        value = (int) (value * (1-weight));
        blendValue = (int) (blendValue * weight);
        outputImage.set(channel, x, y, range.limit(value + blendValue));
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
