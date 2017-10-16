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
        this.ret = ImageFactory.buildEmptyImage(1,
                image.getWidth(), 
                image.getHeight(), 
                image.getPixelValueRange()
        );
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
        HysteresisThresholdProcess hysteresisThresholdProcess = new HysteresisThresholdProcess(ret, 25, 255);
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
                        leftPixel = gradients[x][y - 1];
                        rightPixel = gradients[x][y + 1];
                        break;
                    case ANGLE_45:
                        leftPixel = gradients[x + 1][y - 1];
                        rightPixel = gradients[x - 1][y + 1];
                        break;
                    case ANGLE_90:
                        leftPixel = gradients[x + 1][y];
                        rightPixel = gradients[x - 1][y];
                        break;
                    case ANGLE_135:
                        leftPixel = gradients[x + 1][y + 1];
                        rightPixel = gradients[x - 1][y - 1];
                        break;

                }
                // compare current pixels value with adjacent pixels
                if ((gradients[x][y] < leftPixel) || (gradients[x][y] < rightPixel)) {
                    ret.set(0, x, y, 0);
                } else {
                    ret.set(0, x, y, (int) ((double) gradients[x][y] / gradientProcess.getMaxGradient() * 255));
                }
                p++;
            }
        }

    }

}
