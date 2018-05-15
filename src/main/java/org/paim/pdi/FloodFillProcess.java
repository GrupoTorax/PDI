/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.paim.pdi;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static java.lang.ProcessBuilder.Redirect.to;
import static java.time.Period.between;
import java.util.Set;
import java.util.Stack;
import static java.util.regex.Pattern.matches;
import static java.util.stream.Stream.of;
import static javafx.beans.binding.Bindings.and;
import static javafx.beans.binding.Bindings.equal;
import static javafx.scene.input.KeyCode.N;
import static javafx.scene.input.KeyCode.Q;
import static javafx.scene.paint.Color.color;
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
        setFinalizer(() -> {
            setOutput(outputImage);
        });
    }

    @Override
    public void processImage() {
        targetColor = image.getColor(seed.x, seed.y);
        processPoint(seed);
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
        if (!outputImage.getColor(point.x, point.y).equals(targetColor)) {
            return;
        }
        Point node = point;
        Point w = node;
        Point e = node;
        while (image.inBounds(w) && outputImage.getColor(w.x, w.y).equals(targetColor)) {
            w = w.west();
        }
        while (image.inBounds(e) && outputImage.getColor(e.x, e.y).equals(targetColor)) {
            e = e.east();
        }
        for (int x = w.x + 1; x < e.x; x++) {
            outputImage.setColor(x, node.y, replacementColor);
        }
        for (int x = w.x + 1; x < e.x; x++) {
            processPoint(new Point(x, node.y).north());
            processPoint(new Point(x, node.y).south());
        }
    }

}
