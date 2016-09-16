package org.torax.pdi;

import org.torax.commons.Image;

/**
 * Process that works on top of a image
 * 
 * @param <O> output type
 */
public abstract class ImageProcess<O> implements Process {

    /** Image */
    protected final Image image;
    /** Output of the process */
    private O output;
    /** Process initializer */
    private Runnable initializer;
    /** Process finalizer */
    private Runnable finalizer;

    /**
     * Creates a new image process
     * 
     * @param image 
     */
    public ImageProcess(Image image) {
        this.image = image;
    }

    @Override
    public final void process() {
        if (initializer != null) {
            initializer.run();
        }
        processImage();
        if (finalizer != null) {
            finalizer.run();
        }
    }

    /**
     * Process the image
     */
    protected abstract void processImage();
    
    /**
     * Returns the output
     * 
     * @return O
     */
    public O getOutput() {
        return output;
    }

    /**
     * Sets the output
     * 
     * @param output 
     */
    protected void setOutput(O output) {
        this.output = output;
    }

    /**
     * Sets the initializer
     * 
     * @param initializer 
     */
    protected void setInitializer(Runnable initializer) {
        this.initializer = initializer;
    }

    /**
     * Sets the finalizer
     * 
     * @param finalizer 
     */
    protected void setFinalizer(Runnable finalizer) {
        this.finalizer = finalizer;
    }
    
}
