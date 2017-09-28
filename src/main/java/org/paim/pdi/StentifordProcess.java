package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Stentiford Process
 */
public class StentifordProcess extends SkeletonProcess {

    private final int STEP_1 = 1;
    private final int STEP_2 = 2;
    private final int STEP_3 = 3;
    private final int STEP_4 = 4;
    
    private final Image resultImage;
    private Image processImage;

    public StentifordProcess(Image image) {
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
     * @param step
     * @return int
     */
    public int calc(int[][] pixels, int step) {
        int[] neighborhood = neighborhood(pixels);
        if (!isConnected(neighborhood)) {
            return pixels[1][1];
        }
        int n = pixels[1][0];
        int l = pixels[2][1];
        int s = pixels[1][2];
        int o = pixels[0][1];
        int no = pixels[0][0];
        if (step == STEP_1) {
            if (!(!isHigher(n) && isHigher(s))) {
                return pixels[1][1];
            }
        }
        if (step == STEP_2) {
            if (!(!isHigher(no) && isHigher(l))) {
                return pixels[1][1];
            }
        }
        if (step == STEP_3) {
            if (!(!isHigher(s) & isHigher(n))) {
                return pixels[1][1];
            }
        }
        if (step == STEP_4) {
            if (!(!isHigher(l) && isHigher(o))) {
                return pixels[1][1];
            }
        }
        return 0;
    }

    @Override
    protected void processImage() {
        boolean change = true;
        int step = 0;
        while (change) {
            change = false;
            step++;
            for (int x = 1; x < processImage.getWidth() - 1; x++) {
                for (int y = 1; y < processImage.getHeight() - 1; y++) {
                    if (processImage.get(0, x, y) == image.getPixelValueRange().getHigher()) {
                        int[][] pixels = pixels(x, y, processImage);
                        int v = Math.max(Math.min(calc(pixels, step), image.getPixelValueRange().getHigher()), 0);
                        if (v != processImage.get(0, x, y)) {
                            change = true;
                        }
                        resultImage.set(0, x, y, v);
                    }
                }
            }
            processImage = new Image(resultImage);
            // The final step
            if (step == STEP_4) {
                step = 0;
            }
        }
    }

}

