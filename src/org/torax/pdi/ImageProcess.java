package org.torax.pdi;

import org.torax.commons.Image;

/**
 * Process that works on top of a image
 * 
 * @param <O> output type
 */
public abstract class ImageProcess<O> implements Process {

    /** Image */
    protected Image image;
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
    public void setOutput(O output) {
        this.output = output;
    }

    /**
     * Returns the initializer
     * 
     * @return Runnable
     */
    public Runnable getInitializer() {
        return initializer;
    }

    /**
     * Sets the initializer
     * 
     * @param initializer 
     */
    public void setInitializer(Runnable initializer) {
        this.initializer = initializer;
    }

    /**
     * Returns the finalizer
     * 
     * @return Runnable
     */
    public Runnable getFinalizer() {
        return finalizer;
    }

    /**
     * Sets the finalizer
     * 
     * @param finalizer 
     */
    public void setFinalizer(Runnable finalizer) {
        this.finalizer = finalizer;
    }
    
}
