package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class BrightnessProcess
 */
public class BrightnessProcessTest {
    
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
        }, new Range<>(0, 100));
        BrightnessProcess process = new BrightnessProcess(image, 3);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {3, 13, 23, 33, 43},
                {3, 23, 27, 6, 93},
                {6, 26, 53, 41, 83}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }

    /**
     * Test using three channels
     */
    @Test
    public void testThreeChannel() {
        Image image = new Image(new int[][][]{
            {
                {0, 10, 20, 30, 40},
                {0, 20, 24, 3, 90},
                {3, 23, 50, 38, 80}
            },
            {
                {0, 10, 28, 32, 41},
                {0, 4, 26, 32, 98},
                {3, 6, 57, 31, 82}
            },
            {
                {2, 11, 24, 32, 33},
                {0, 22, 26, 31, 78},
                {3, 24, 57, 35, 60}
            }
        }, new Range<>(0, 100));
        BrightnessProcess process = new BrightnessProcess(image, 3);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {3, 13, 23, 33, 43},
                {3, 23, 27, 6, 93},
                {6, 26, 53, 41, 83}
            },
            {
                {3, 13, 31, 35, 44},
                {3, 7, 29, 35, 100},
                {6, 9, 60, 34, 85}
            },
            {
                {5, 14, 27, 35, 36},
                {3, 25, 29, 34, 81},
                {6, 27, 60, 38, 63}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }

}
