package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class VerticalMirroringProcess
 */
public class VerticalMirroringProcessTest {

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
        VerticalMirroringProcess process = new VerticalMirroringProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {40, 30, 20, 10, 0},
                {90, 3, 24, 20, 0},
                {80, 38, 50, 23, 3}
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
        VerticalMirroringProcess process = new VerticalMirroringProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {40, 30, 20, 10, 0},
                {90, 3, 24, 20, 0},
                {80, 38, 50, 23, 3}
            },
            {
                {41, 32, 28, 10, 0},
                {98, 32, 26, 4, 0},
                {82, 31, 57, 6, 3}
            },
            {
                {33, 32, 24, 11, 2},
                {78, 31, 26, 22, 0},
                {60, 35, 57, 24, 3}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }

}
