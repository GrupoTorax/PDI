package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Test of class GaussianBlurProcess
 */
public class GaussianBlurProcessTest {
    
    @Test
    public void test3x3Image() {
        Image image = new Image(new int[][][] {
            {
                {10, 30, 10},
                {30, 90, 30},
                {10, 30, 10}
            }
        }, new Range<>(0, 100));
        GaussianBlurProcess blur = new GaussianBlurProcess(image, 1.1, 3);
        blur.process();
        Image expected = new Image(new int[][][] {
            {
                {10, 23, 10},
                {23, 54, 23},
                {10, 23, 10}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, blur.getOutput());
    }

}
