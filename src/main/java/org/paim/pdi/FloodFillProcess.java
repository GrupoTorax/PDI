/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.paim.pdi;

import java.util.Stack;
import org.paim.commons.Color;
import org.paim.commons.Image;
import org.paim.commons.ImageFactory;
import org.paim.commons.Point;

/**
 * Flood Fill process
 */
public class FloodFillProcess extends ImageProcess<Image> {
    
    /** Seed point for the fill */
    private final Point seed;
    /** Replacement color */
    private final Color replacementColor;
    /** Output image */
    private final Image outputImage;
    /** Processing stack */
    private final Stack<Point> stack;
    /** Target color */
    private Color targetColor;
    
    /**
     * Creates a new process that iterates over the image pixels
     * 
     * @param image 
     * @param seed 
     * @param replacement 
     */
    public FloodFillProcess(Image image, Point seed, Color replacement) {
        super(image);
        Image resultImage;
        if (image == null) {
            resultImage = ImageFactory.buildEmptyImage();
        } else {
            resultImage = new Image(image);
        }
        if (seed == null) {
            seed = Point.CENTER;
        }
        if (replacement == null) {
            int[] color = new int[image.getChannelCount()];
            for (int i = 0; i < color.length; i++) {
                color[i] = image.getPixelValueRange().getHigher();
            }
            replacement = new Color(color);
        }
        this.outputImage = resultImage;
        this.seed = seed.compute(image);
        this.replacementColor = replacement;
        this.stack = new Stack<>();
        setFinalizer(() -> {
            stack.clear();
            setOutput(outputImage);
        });
    }

    @Override
    public void processImage() {
        targetColor = image.getColor(seed.x, seed.y);
        stack.add(seed);
        while (!stack.empty()) {
            processPoint(stack.pop());
        }
    }
    
    /**
     * Process a point
     * 
     * @param point
     */
    private void processPoint(Point point) {
        if (!image.inBounds(point)) {
            return;
        }
        if (outputImage.getColor(point.x, point.y).equals(targetColor)) {
            outputImage.setColor(point.x, point.y, replacementColor);
            stack.add(point.east());
            stack.add(point.west());
            stack.add(point.north());
            stack.add(point.south());
        }
    }

}
