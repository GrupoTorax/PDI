package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Closing process
 */
public class ClosingProcess extends ImageProcess<Image> {

    /** Gray scale image */
    private Image outputImage;

    /**
     * Creates a new Closing process
     *
     * @param image
     */
    public ClosingProcess(Image image) {
        super(image);
        outputImage = ImageFactory.buildEmptyImage(image);
        setFinalizer(() -> {
            setOutput(outputImage);
        });
    }
    
    @Override
    protected void processImage() {
        DilationProcess dilation = new DilationProcess(image);
        dilation.process();
        ErosionProcess erosion = new ErosionProcess(dilation.getOutput());
        erosion.process();
        outputImage = erosion.getOutput();
    }
    
}
