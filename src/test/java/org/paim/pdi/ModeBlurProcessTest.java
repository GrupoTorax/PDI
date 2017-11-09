package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class ModeBlurProcess
 */
public class ModeBlurProcessTest {

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
        ModeBlurProcess process = new ModeBlurProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {0, 0, 20, 20, 40},
                {0, 0, 20, 3, 80},
                {3, 50, 50, 80, 80}
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
        ModeBlurProcess process = new ModeBlurProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {0, 0, 20, 20, 40},
                {0, 0, 20, 3, 80},
                {3, 50, 50, 80, 80}
            },
            {
                {0, 0, 32, 32, 41},
                {0, 0, 32, 32, 32},
                {3, 3, 6, 82, 82}
            },
            {
                {2, 2, 32, 32, 33},
                {0, 24, 24, 31, 33},
                {3, 3, 35, 35, 60}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }

}
