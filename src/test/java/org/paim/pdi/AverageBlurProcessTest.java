package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class AverageBlurProcess
 */
public class AverageBlurProcessTest {

    /**
     * Test using one channel
     */
    @Test
    public void testAvaregeBlurOneChannel() {
        Image image = new Image(new int[][][]{
            {
                {0, 10, 20, 30, 40},
                {0, 20, 24, 3, 90},
                {3, 23, 50, 38, 80}
            }
        }, new Range<>(0, 100));
        AverageBlurProcess process = new AverageBlurProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {4, 11, 18, 33, 44},
                {6, 16, 24, 41, 54},
                {8, 21, 29, 50, 64}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }

    /**
     * Test using three channels
     */
    @Test
    public void testAvaregeBlurThreeChannel() {
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
        AverageBlurProcess process = new AverageBlurProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {4, 11, 18, 33, 44},
                {6, 16, 24, 41, 54},
                {8, 21, 29, 50, 64}
            },
            {
                {2, 11, 22, 39, 50},
                {2, 14, 25, 47, 59},
                {3, 18, 27, 55, 68}
            },
            {
                {5, 13, 23, 34, 42},
                {7, 18, 29, 41, 48},
                {9, 24, 34, 48, 55}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }

}
