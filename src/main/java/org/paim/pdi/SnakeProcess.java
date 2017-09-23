package org.paim.pdi;

import org.paim.commons.BinaryImage;
import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * The snake process
 */
public class SnakeProcess extends ImageProcess<BinaryImage>  {
    
    private final BinaryImage binaryImage;
    private final int steps;
    private final int alpha;
    private final int beta;
    private final int gamma;
    
    public SnakeProcess(Image image, int steps, int alpha, int beta, int gamma) {
        super(image);
        this.steps = steps;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.binaryImage = ImageFactory.buildBinaryImage(image.getWidth(), image.getHeight());
        setFinalizer(() -> {
            setOutput(binaryImage);
        });
    }

    @Override
    protected void processImage() {
        for (int i = 0; i < steps; i++) {
            if (!step()) {
                break;
            }
        }
        for (int x = 100; x < 200; x++) {
            for (int y = 100; y < 200; y++) {
                binaryImage.set(x, y, true);
            }
        }
    }

    private boolean step() {
        return false;
    }
    
}
