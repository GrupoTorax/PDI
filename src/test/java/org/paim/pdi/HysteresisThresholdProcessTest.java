package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class HysteresisThresholdProcess
 */
public class HysteresisThresholdProcessTest {

    /**
     * Test using one channel
     */
    @Test
    public void testOneChannel() {
        Image image = new Image(new int[][][]{
            {
                {0, 10, 20, 30, 40},
                {0, 20, 24, 3, 90},
                {3, 23, 50, 38, 80}
            }
        }, new Range<>(0, 1));
        HysteresisThresholdProcess process = new HysteresisThresholdProcess(image, 10, 80);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {0, 10, 20, 0, 0},
                {0, 20, 24, 0, 0},
                {0, 23, 50, 0, 0}
            }
        }, new Range<>(0, 1));
        ImageAssert.assertImage(expected, process.getOutput());
    }

}
