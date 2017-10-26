package org.paim.pdi;

import java.util.ArrayList;
import java.util.List;
import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Create a gradient from a image
 */
public class GradientProcess extends ImageProcess<Image> {

    /** Gradient image */
    private final Image gradient;
    /** Max gradient */
    private int maxgradient = 0;
    /** Orientation */
    private final List<GradientOrientation> orientation;

    /**
     * Creates a new gradient process
     *
     * @param image
     */
    public GradientProcess(Image image) {
        super(image);
        this.gradient = ImageFactory.buildEmptyImage(
                Image.CHANNELS_GRAYSCALE, 
                image.getWidth(), 
                image.getHeight(), image.getPixelValueRange()
        );
        this.orientation = new ArrayList<>();
        setFinalizer(() -> {
            setOutput(gradient);
        });
    }

    @Override
    protected void processImage() {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] clum = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image.getPixelValueRange().isBinary()) {
                    clum[x][y] = image.get(Image.CHANNEL_GRAY, x, y) == 1 ? 255 : 0;
                } else {
                    if (image.isGrayScale()) {
                        clum[x][y] = image.get(Image.CHANNEL_GRAY, x, y);
                    } else {
                        int r = image.get(Image.CHANNEL_RED, x, y);
                        int g = image.get(Image.CHANNEL_GREEN, x, y);
                        int b = image.get(Image.CHANNEL_BLUE, x, y);
                        clum[x][y] = ((int) (0.299D * r + 0.587D * g + 0.114D * b));
                    }
                }
            }
        }
        for (int y = 0; y < height - 2; y++) {
            for (int x = 0; x < width - 2; x++) {
                int p00 = clum[(x + 0)][(y + 0)];
                int p10 = clum[(x + 1)][(y + 0)];
                int p20 = clum[(x + 2)][(y + 0)];
                int p01 = clum[(x + 0)][(y + 1)];
                int p21 = clum[(x + 2)][(y + 1)];
                int p02 = clum[(x + 0)][(y + 2)];
                int p12 = clum[(x + 1)][(y + 2)];
                int p22 = clum[(x + 2)][(y + 2)];
                int sx = p20 + 2 * p21 + p22 - (p00 + 2 * p01 + p02);
                int sy = p02 + 2 * p12 + p22 - (p00 + 2 * p10 + p10);
                int snorm = (int) Math.sqrt(sx * sx + sy * sy);
                gradient.set(0, (x + 1), (y + 1), snorm);
                maxgradient = Math.max(maxgradient, snorm);
                GradientOrientation gorientation = GradientOrientation.ANGLE_0;
                if (sx > 0) {
                    double div = (double) sy / sx;
                    double ori = 0;
                    // handle angles of the 2nd and 4th quads
                    if (div < 0) {
                        ori = 180 - Math.atan(-div) * 180d;
                    } // handle angles of the 1st and 3rd quads
                    else {
                        ori = Math.atan(div) * 180d;
                    }
                    // get closest angle from 0, 45, 90, 135 set
                    if (ori < 22.5) {
                        gorientation = GradientOrientation.ANGLE_0;
                    } else if (ori < 67.5) {
                        gorientation = GradientOrientation.ANGLE_45;
                    } else if (ori < 112.5) {
                        gorientation = GradientOrientation.ANGLE_90;
                    } else if (ori < 157.5) {
                        gorientation = GradientOrientation.ANGLE_135;
                    } 
                }
                orientation.add(gorientation);
            }
        }
    }

    /**
     * Returns the max gradient
     *
     * @return int
     */
    public int getMaxGradient() {
        return maxgradient;
    }

    /**
     * Returns the gradient orientation
     *
     * @return
     */
    public List<GradientOrientation> getOrientation() {
        return orientation;
    }

    public static enum GradientOrientation {

        ANGLE_0,
        ANGLE_45,
        ANGLE_90,
        ANGLE_135,

    }

}
