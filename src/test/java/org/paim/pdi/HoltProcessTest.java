package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class HoltProcess
 */
public class HoltProcessTest {

    /**
     * Test using one channel
     */
    @Test
    public void testOneChannel() {
        Image image = new Image(new int[][][]{
            {
                {1, 1, 1, 0, 1},
                {1, 1, 1, 0, 1},
                {1, 1, 1, 0, 1}
            }
        }, new Range<>(0, 1));
        HoltProcess process = new HoltProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0}
            }
        }, new Range<>(0, 1));
        ImageAssert.assertImage(expected, process.getOutput());
    }

}
