package org.torax.pdi;

import static org.junit.Assert.*;
import org.junit.Test;
import org.torax.commons.BinaryLabeling;
import org.torax.commons.Image;
import org.torax.commons.Range;
import org.torax.commons.test.ImageAssert;

/**
 * Unit tests of class BinaryLabelingProcess
 */
public class BinaryLabelingProcessTest {

    /**
     * Test on a image that has a single channel
     */
    @Test
    public void testSingleChannelImage() {
        Image expected = new Image(new int[][][]{
            {
                {0, 2, 2, 0, 0, 0, 0, 3, 0, 3},
                {0, 2, 2, 0, 4, 4, 0, 3, 3, 3},
                {0, 0, 0, 0, 0, 0, 0, 3, 0, 3},}
        }, new Range<>(-100, 100));
        BinaryLabelingProcess process = new BinaryLabelingProcess(buildTestImage());
        process.process();
        BinaryLabeling out = process.getOutput();
        ImageAssert.assertImage(expected, out.getImage());
        assertEquals(3, out.getSize());
    }

    /**
     * Creates the test image
     *
     * @return Image
     */
    private Image buildTestImage() {
        return new Image(new int[][][]{
            {
                {0, 1, 1, 0, 0, 0, 0, 1, 0, 1},
                {0, 1, 1, 0, 1, 1, 0, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 1},}
        }, new Range<>(-100, 100));
    }

}
