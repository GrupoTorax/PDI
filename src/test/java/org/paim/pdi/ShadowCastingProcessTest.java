package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class ShadowCastingProcess
 */
public class ShadowCastingProcessTest {

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
        ShadowCastingProcess process = new ShadowCastingProcess(image, 50, ShadowCastingProcess.Orientation.BOTTOM);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {50, 50, 50, 50, 50},
                {50, 50, 50, 50, 90},
                {50, 50, 50, 50, 80}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }

}
