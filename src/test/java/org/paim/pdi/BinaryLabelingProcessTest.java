package org.paim.pdi;

import static org.junit.Assert.*;
import org.junit.Test;
import org.paim.commons.BinaryLabeling;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

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
     * Test the label size calculation
     */
    @Test
    public void testLabelSize() {
        BinaryLabelingProcess process = new BinaryLabelingProcess(buildTestImage());
        process.process();
        assertEquals(4, process.getSize(1));
        assertEquals(7, process.getSize(2));
        assertEquals(2, process.getSize(3));
    }

    /**
     * Test the label size calculation
     */
    @Test
    public void testMatrix() {
        BinaryLabelingProcess process = new BinaryLabelingProcess(buildTestImage());
        process.process();
        boolean label1[][] = new boolean[][]{
            {false, true, true, false, false, false, false, false, false, false},
            {false, true, true, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false}
        };
        boolean label2[][] = new boolean[][]{
            {false, false, false, false, false, false, false, true, false, true},
            {false, false, false, false, false, false, false, true, true, true},
            {false, false, false, false, false, false, false, true, false, true}
        };
        boolean label3[][] = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, true, true, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false}
        };
        assertArrayEquals(label1, process.getMatrix(1));
        assertArrayEquals(label2, process.getMatrix(2));
        assertArrayEquals(label3, process.getMatrix(3));
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
