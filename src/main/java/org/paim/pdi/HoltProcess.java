package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 *
 */
public class HoltProcess extends SkeletonProcess {

    private final Image resultImage;
    private Image processImage;

    public HoltProcess(Image image) {
        super(image);
        this.resultImage = ImageFactory.buildEmptyImage(image);
        this.processImage = new Image(image);
        setFinalizer(() -> {
            setOutput(resultImage);
        });
    }

    private boolean isHigher(int value) {
        return value == image.getPixelValueRange().getHigher();

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
        if (!isEdge(neighborhood)) {
            return pixels[1][1];
        }
        int n = pixels[1][0];
        int l = pixels[2][1];
        int s = pixels[1][2];
        int o = pixels[0][1];
        if (first) {
            if (isHigher(l) && isHigher(s) && (isHigher(n) || isHigher(o))) {
                return pixels[1][1];
            }
        } else {
            if (isHigher(o) && isHigher(n) && (isHigher(s) || isHigher(l))) {
                return pixels[1][1];
            }
        }
        return 0;
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
