package org.torax.pdi;

import org.junit.Test;
import org.torax.commons.Image;
import org.torax.commons.Range;
import org.torax.commons.test.ImageAssert;

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
