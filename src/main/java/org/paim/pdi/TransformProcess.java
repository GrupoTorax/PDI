package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Transform image process
 */
public class TransformProcess extends PixelProcess<Image> {

    /** Result image */
    private final Image img;
    /** Kernel */
    private final double[][] kernel;
    
    /**
     * Creates a new transform image process
     * 
     * @param image
     * @param kernel 
     */
    public TransformProcess(Image image, double[][] kernel) {
        super(image);
        this.img = ImageFactory.buildEmptyImage(image);
        this.kernel = kernel;
        setFinalizer(() -> {
            setOutput(img);
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        int halfX = image.getWidth() / 2;
        int halfY = image.getHeight() / 2;
        int tmpX = x - halfX;
        int tmpY = y - halfY;
        int newX = (int) Math.round(tmpX * kernel[0][0] + tmpY * kernel[0][1] + 1 * kernel[0][2]);
        int newY = (int) Math.round(tmpX * kernel[1][0] + tmpY * kernel[1][1] + 1 * kernel[1][2]);
        newX += halfX;
        newY += halfY;
        // Pixel position is right
        if (newX < image.getWidth() && newY < image.getHeight() && newX >= 0 && newY >= 0) {
            img.set(channel, x, y, image.get(channel, newX, newY));
        }
    }

}
