package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class ThresholdProcess
 */
public class ThresholdProcessTest {
    
//    /**
//     * Test on a image that has a single channel
//     */
//    @Test
//    public void testSingleChannelImage() {
//        Image image = new Image(new int[][][] {
//            {
//                {0, 10, 20, 30, 40, 48, 49, 50, 51, 52, 60, 70, 80, 90, 100},
//            }
//        }, new Range<>(0, 100));
//        ThresholdProcess process = new ThresholdProcess(image, 50);
//        process.process();
//        Image output = process.getOutput();
//        ImageAssert.assertImage(new Image(new int[][][] {
//            {
//                {0, 0, 0, 0, 0, 0, 0, 100, 100, 100, 100, 100, 100, 100, 100},
//            }
//        }, new Range<>(0, 100)), output);
//    }

}
