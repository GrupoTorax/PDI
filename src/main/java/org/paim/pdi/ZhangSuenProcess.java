package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Zhang Suen Process
 */
public class ZhangSuenProcess extends SkeletonProcess {

    /** The result imagen */
    private final Image resultImage;
    /** The imagen in process */
    private Image processImage;

    public ZhangSuenProcess(Image image) {
        super(image);
        this.resultImage = ImageFactory.buildEmptyImage(image);
        this.processImage = new Image(image);
        setFinalizer(() -> {
            setOutput(resultImage);
        });
    }
    
    /**
     * calculate the pixel value
     * 
     * @param pixels
     * @param first
     * @return int
     */
    public int calc(int[][] pixels, boolean first) {
        int[] neighborhood = neighborhood(pixels);
        if (isEdge(neighborhood)) {
            if (first) {
                if ((neighborhood[0] * neighborhood[2] * neighborhood[4] == 0) && 
                        (neighborhood[2] * neighborhood[4] * neighborhood[6] == 0)) {
                    return 0;
                }
            } else {
                if ((neighborhood[6] * neighborhood[0] * neighborhood[2] == 0) && 
                        (neighborhood[0] * neighborhood[6] * neighborhood[4] == 0)) {
                    return 0;
                }
            }
        }
        return pixels[1][1];
    }
    
    @Override
    protected void processImage() {
        boolean change = true;
        boolean firstStep = false;
        while (change) {
            change = false;
            firstStep = !firstStep;
            for (int x = 1; x < processImage.getWidth() - 1; x++) {
                for (int y = 1; y < processImage.getHeight() - 1; y++) {
                    if (processImage.get(0, x, y) == image.getPixelValueRange().getHigher()) {
                        int[][] pixels = pixels(x, y, processImage);
                        int v = Math.max(Math.min(calc(pixels, firstStep), 255), 0);
                        if (v != processImage.get(0, x, y)) {
                            change = true;
                        }
                        resultImage.set(0, x, y, v);
                    }
                }
            }
            processImage = new Image(resultImage);
        }
    }

}
