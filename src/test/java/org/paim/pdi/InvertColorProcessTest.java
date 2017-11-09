package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class InvertColorProcess
 */
public class InvertColorProcessTest {

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
        InvertColorProcess process = new InvertColorProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {100, 90, 80, 70, 60},
                {100, 80, 76, 97, 10},
                {97, 77, 50, 62, 20}
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
        InvertColorProcess process = new InvertColorProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {100, 90, 80, 70, 60},
                {100, 80, 76, 97, 10},
                {97, 77, 50, 62, 20}
            },
            {
                {100, 90, 72, 68, 59},
                {100, 96, 74, 68, 2},
                {97, 94, 43, 69, 18}
            },
            {
                {98, 89, 76, 68, 67},
                {100, 78, 74, 69, 22},
                {97, 76, 43, 65, 40}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }

}
