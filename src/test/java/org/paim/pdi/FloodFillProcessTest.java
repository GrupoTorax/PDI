package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Color;
import org.paim.commons.Image;
import org.paim.commons.Point;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

public class FloodFillProcessTest {
    
    @Test
    public void testSomeMethod() {
        Image image = new Image(new int[][][]{
            {
                {0, 10, 0, 0, 0},
                {0, 10, 10, 0, 10},
                {0, 10, 0, 0, 0}
            }
        }, new Range<>(0, 100));
        FloodFillProcess process = new FloodFillProcess(image, new Point(1, 3), new Color(255));
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {0, 10, 255, 255, 255},
                {0, 10, 10, 255, 10},
                {0, 10, 255, 255, 255}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
    }
    
}
