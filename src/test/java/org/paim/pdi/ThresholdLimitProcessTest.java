package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 *
 * @author Pichau
 */
public class ThresholdLimitProcessTest {
    
    /**
     * Test on a image that has a single channel
     */
    @Test
    public void testSingleChannelImage() {
        Image image = new Image(new int[][][] {
            {
                { 0,  5, 10, 15, 20},
                {25, 30, 35, 40, 45},
                {50, 55, 60, 65, 70},
                {75, 80, 85, 90, 100},
            }
        }, new Range<>(0, 100));
        ThresholdLimitProcess process = new ThresholdLimitProcess(image, 25, 50, 0, 99);
        process.process();
        Image output = process.getOutput();
        ImageAssert.assertImage(new Image(new int[][][] {
            {
                { 0,  0,  0,  0,  0},
                {25, 30, 35, 40, 45},
                {50, 99, 99, 99, 99},
                {99, 99, 99, 99, 99},
            }
        }, new Range<>(0, 100)), output);
    }
    
    /**
     * Test on a image that has a single channel
     */
    @Test
    public void testSingleChannelImageWithMiddleReplaceValue() {
        Image image = new Image(new int[][][] {
            {
                { 0,  5, 10, 15, 20},
                {25, 30, 35, 40, 45},
                {50, 55, 60, 65, 70},
                {75, 80, 85, 90, 100},
            }
        }, new Range<>(0, 100));
        ThresholdLimitProcess process = new ThresholdLimitProcess(image, 25, 50, 0, 99, 50);
        process.process();
        Image output = process.getOutput();
        ImageAssert.assertImage(new Image(new int[][][] {
            {
                { 0,  0,  0,  0,  0},
                {50, 50, 50, 50, 50},
                {50, 99, 99, 99, 99},
                {99, 99, 99, 99, 99},
            }
        }, new Range<>(0, 100)), output);
    }
    
}
