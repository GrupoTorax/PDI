package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Unit tests of class MedianBlurProcess
 */
public class MedianBlurProcessTest {

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
        MedianBlurProcess process = new MedianBlurProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {0, 10, 20, 30, 40},
                {3, 20, 23, 38, 40},
                {3, 23, 24, 50, 80}
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
        MedianBlurProcess process = new MedianBlurProcess(image);
        process.process();
        Image expected = new Image(new int[][][]{
            {
                {0, 10, 20, 30, 40},
                {3, 20, 23, 38, 40},
                {3, 23, 24, 50, 80}
            },
            {
                {0, 10, 28, 32, 41},
                {3, 6, 28, 32, 41},
                {3, 6, 31, 57, 82}
            },
            {
                {2, 11, 24, 32, 33},
                {3, 22, 26, 33, 35},
                {3, 24, 31, 57, 60}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expected, process.getOutput());
        
        long l = (System.currentTimeMillis());
        for (int i = 0; i < 100000; i++) {
            process.process();
            
        }
        System.out.println(System.currentTimeMillis() - l);
        
        
    }

}
