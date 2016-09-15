package org.torax.pdi;

import org.junit.Test;
import static org.junit.Assert.*;
import org.torax.commons.Histogram;
import org.torax.commons.Image;
import org.torax.commons.Range;

/**
 * Tests of class Histogram
 */
public class HistogramProcessTest {
    
    /**
     * Test of process method, of class HistogramProcess
     */
    @Test
    public void testProcess() {
        HistogramProcess process = new HistogramProcess(buildTestImage());
        process.process();
        Histogram histogram = process.getOutput();
        assertEquals(1, histogram.get(-5));
        assertEquals(0, histogram.get(-4));
        assertEquals(1, histogram.get(-3));
        assertEquals(3, histogram.get(-2));
        assertEquals(2, histogram.get(-1));
        assertEquals(1, histogram.get(0));
        assertEquals(2, histogram.get(1));
        assertEquals(0, histogram.get(2));
        assertEquals(1, histogram.get(3));
        assertEquals(4, histogram.get(4));
        assertEquals(1, histogram.get(5));
        assertEquals(2, histogram.get(6));
        assertEquals(9, histogram.get(7));
        assertEquals(2, histogram.get(8));
        assertEquals(1, histogram.get(9));
    }
    
    /**
     * Test of process getValueWithLeastOccurences, of class HistogramProcess
     */
    @Test
    public void testGetValueWithLeastOccurences() {
        HistogramProcess process = new HistogramProcess(buildTestImage());
        process.process();
        Histogram histogram = process.getOutput();
        assertEquals(-100, histogram.getValueWithLeastOccurences());
        assertEquals(-100, histogram.getValueWithLeastOccurences(new Range<>(-100, 0)));
        assertEquals(2, histogram.getValueWithLeastOccurences(new Range<>(0, 9)));
        assertEquals(-3, histogram.getValueWithLeastOccurences(new Range<>(-3, 0)));
        assertEquals(0, histogram.getValueWithLeastOccurences(new Range<>(-2, 0)));
    }

    /**
     * Creates the test image
     * 
     * @return Image
     */
    private Image buildTestImage() {
        return new Image(new int[][][]{
            {
                {0, 1, 1, 3, 4, 4, 4, 4, 5, 6},
                {6, 7, 7, 7, 7, 7, 7, 7, 7, 7},
                {8, 8, 9, -1, -1, -2, -2, -2, -3, -5},
            }
        }, new Range<>(-100, 100));
    }
    
}
