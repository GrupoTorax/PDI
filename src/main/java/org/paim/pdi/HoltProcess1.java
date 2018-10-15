package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Holt process
 */

// https://github.com/alruiz/fpreader/blob/68e540278f71f3fb03be16f901fa8188a84dcb29/%20fpreader/src/br/com/teoni/fpreader/imageprocessing/Thinning.java
public class HoltProcess1 extends SkeletonProcess {

    private final Image resultImage;
    private Image processImage;

    public HoltProcess1(Image image) {
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

    /**
     * calculate the pixel value
     *
     * @param pixels
     * @return int
     */
    public int staircase(int[][] pixels) {
        int[] neighborhood = neighborhood(pixels);
        if (!isEdge(neighborhood)) {
            return pixels[1][1];
        }
        int no = pixels[0][0];
        int n = pixels[1][0];
        int ne = pixels[2][0];
        int l = pixels[2][1];
        int o = pixels[0][1];
        int so = pixels[0][2];
        int s = pixels[1][2];
        int se = pixels[2][2];
        int c = pixels[1][1];

        if (isHigher(c) && isHigher(n) && ((isHigher(l) && !isHigher(ne) && !isHigher(so) && (!isHigher(o) || !isHigher(s))) || (isHigher(o) && !isHigher(so) && !isHigher(se) && (!isHigher(l) || !isHigher(s))) )) {
            return pixels[1][1];    
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
                        int v = Math.max(Math.min(calc(pixels, firstStep), image.getPixelValueRange().getHigher()), 0);
                        if (v != processImage.get(0, x, y)) {
                            change = true;
                        }
                        resultImage.set(0, x, y, v);
                    }
                }
            }
            processImage = new Image(resultImage);
        }
        for (int x = 1; x < processImage.getWidth() - 1; x++) {
            for (int y = 1; y < processImage.getHeight() - 1; y++) {
                if (processImage.get(0, x, y) == image.getPixelValueRange().getHigher()) {
                    int[][] pixels = pixels(x, y, processImage);
                    int v = Math.max(Math.min(staircase(pixels), image.getPixelValueRange().getHigher()), 0);
                    resultImage.set(0, x, y, v);
                }
            }
        }
    }

}
