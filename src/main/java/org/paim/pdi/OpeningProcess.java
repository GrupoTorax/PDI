package org.paim.pdi;

import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Opening process
 */
public class OpeningProcess extends ImageProcess<Image> {

    /** Gray scale image */
    private Image outputImage;

    /**
     * Creates a new Roberts process
     *
     * @param image
     */
    public OpeningProcess(Image image) {
        super(image);
        outputImage = ImageFactory.buildEmptyImage(image);
        setFinalizer(() -> {
            setOutput(outputImage);
        });
    }
    
    @Override
    protected void processImage() {
        ErosionProcess erosion = new ErosionProcess(image);
        erosion.process();
        DilationProcess dilation = new DilationProcess(erosion.getOutput());
        dilation.process();
        outputImage = dilation.getOutput();
    }
    
}
