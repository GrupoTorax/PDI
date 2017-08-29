package org.torax.pdi;

import org.junit.Test;
import org.torax.commons.Image;
import org.torax.commons.Range;
import org.torax.commons.test.ImageAssert;

/**
 * Unit tests of class ErosionProcess
 */
public class ErosionProcessTest {
    
    /**
     * Test on a image that has a single channel
     */
    @Test
    public void testSingleChannelImage() {
        Image image = new Image(new int[][][] {
            {
                {0, 30, 30, 30, 0},
                {0, 30,  0, 30, 0},
                {0, 30,  0, 30, 0},
                {0,  0,  0,  0, 0},
            }
        }, new Range<>(0, 100));
        ErosionProcess process = new ErosionProcess(image);
        process.process();
        Image output = process.getOutput();
        ImageAssert.assertImage(new Image(new int[][][] {
            {
                {0, 0,  0, 30, 0},
                {0, 0,  0, 30, 0},
                {0, 0,  0, 30, 0},
                {0, 0,  0,  0, 0},
            }
        }, new Range<>(0, 100)), output);
    }

}

