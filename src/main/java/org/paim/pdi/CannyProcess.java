package org.paim.pdi;

import java.util.List;
import org.paim.commons.Image;
import org.paim.commons.ImageFactory;
import static org.paim.pdi.GradientProcess.GradientOrientation.ANGLE_0;
import static org.paim.pdi.GradientProcess.GradientOrientation.ANGLE_135;
import static org.paim.pdi.GradientProcess.GradientOrientation.ANGLE_45;
import static org.paim.pdi.GradientProcess.GradientOrientation.ANGLE_90;

/**
 * Canny edge detector
 */
public class CannyProcess extends ImageProcess<Image> {
    
    /** Output image */
    private Image ret;

    /**
     * Creates a new canny edge detector process
     *
     * @param image
     */
    public CannyProcess(Image image) {
        super(image);
        this.ret = ImageFactory.buildEmptyImage(image);
        setFinalizer(() -> {
            setOutput(ret);
        });
    }

    @Override
    protected void processImage() {
        // 1º Apply Gaus
        GaussianBlurProcess gaussianBlurProcess = new GaussianBlurProcess(image, 1.4, 5);
        gaussianBlurProcess.process();
        // 2º Gradient
        GradientProcess gradientProcess = new GradientProcess(gaussianBlurProcess.getOutput());
        gradientProcess.process();
        // 3º Suppress non maximums
        nonMaximus(gradientProcess);
        // STEP 4 - Hysteresis Threshold
        HysteresisThresholdProcess hysteresisThresholdProcess = new HysteresisThresholdProcess(ret, 20, 100);
        hysteresisThresholdProcess.process();
        ret = hysteresisThresholdProcess.getOutput();
    }
    
    /**
     * Suppress non maximums
     * 
     * @param gradientProcess 
     */
    private void nonMaximus(GradientProcess gradientProcess) {
        int p = 0;
        int[][] gradients = gradientProcess.getOutput().getData()[0];
        int leftPixel = 0;
        int rightPixel = 0;
        List<GradientProcess.GradientOrientation> orients = gradientProcess.getOrientation();
        for (int x = 1; x < ret.getWidth() - 1; x++) {
            for (int y = 1; y < ret.getHeight() - 1; y++) {
                switch (orients.get(p)) {
                    case ANGLE_0:
                        leftPixel = gradients[y - 1][x];
                        rightPixel = gradients[y + 1][x];
                        break;
                    case ANGLE_45:
                        leftPixel = gradients[y - 1][x + 1];
                        rightPixel = gradients[y + 1][x - 1];
                        break;
                    case ANGLE_90:
                        leftPixel = gradients[y][x + 1];
                        rightPixel = gradients[y][x - 1];
                        break;
                    case ANGLE_135:
                        leftPixel = gradients[y + 1][x + 1];
                        rightPixel = gradients[y - 1][x - 1];
                        break;

                }
                // compare current pixels value with adjacent pixels
                if ((gradients[y][x] < leftPixel) || (gradients[y][x] < rightPixel)) {
                    ret.set(0, x, y, 0);
                } else {
                    ret.set(0, x, y, (int) (gradients[y][x] / gradientProcess.getMaxGradient() * 255));
                }
                p++;
            }
        }

    }

}
