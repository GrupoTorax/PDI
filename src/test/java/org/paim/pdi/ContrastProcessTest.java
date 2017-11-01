package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class ContrastProcess
 */
public class ContrastProcessTest {
    
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
        ContrastProcess process = new ContrastProcess(image, 1.5d);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {0, 0, 4, 19, 34},
                {0, 4, 10, 0, 100},
                {0, 9, 49, 31, 94}
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
        ContrastProcess process = new ContrastProcess(image, 3d);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {0, 0, 0, 0, 19},
                {0, 0, 0, 0, 100},
                {0, 0, 49, 13, 100}
            },
            {
                {0, 0, 0, 0, 22},
                {0, 0, 0, 0, 100},
                {0, 0, 70, 0, 100}
            },
            {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 100},
                {0, 0, 70, 4, 79}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }

}
