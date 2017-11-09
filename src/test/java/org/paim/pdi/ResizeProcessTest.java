package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class ResizeProcess
 */
public class ResizeProcessTest {

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
        ResizeProcess process = new ResizeProcess(image, 2);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {20, 24, 24, 3, 3},
                {20, 24, 24, 3, 3},
                {23, 50, 50, 38, 38}
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
        ResizeProcess process = new ResizeProcess(image, 2);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {20, 24, 24, 3, 3},
                {20, 24, 24, 3, 3},
                {23, 50, 50, 38, 38}
            },
            {
                {4, 26, 26, 32, 32},
                {4, 26, 26, 32, 32},
                {6, 57, 57, 31, 31}
            },
            {
                {22, 26, 26, 31, 31},
                {22, 26, 26, 31, 31},
                {24, 57, 57, 35, 35}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }

}
