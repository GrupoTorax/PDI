package org.paim.pdi;

import org.junit.Test;
import org.paim.commons.Image;
import org.paim.commons.ImageRegion;
import org.paim.commons.Range;
import org.paim.commons.test.ImageAssert;

/**
 * Test of class RegionProcess
 */
public class RegionProcessTest {
    
    /**
     * Test of buildRegion method, of class RegionProcess
     */
    @Test
    public void testBuildRegion() {
        Image image = new Image(new int[][][] {
            {
                {1, 3, 1},
                {3, 9, 3},
                {1, 3, 1}
            }
        }, new Range<>(0, 100));
        RegionProcess instance = new RegionProcessImpl(image, 3);
        //
        ImageRegion expectedRegion = new ImageRegion(new int[][][] {
            {
                {0, 0, 0},
                {0, 1, 3},
                {0, 3, 9}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expectedRegion, instance.buildRegion(0, 0));
        //
        expectedRegion = new ImageRegion(new int[][][] {
            {
                {0, 0, 0},
                {3, 1, 0},
                {9, 3, 0}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expectedRegion, instance.buildRegion(0, 2));
        //
        expectedRegion = new ImageRegion(new int[][][] {
            {
                {9, 3, 0},
                {3, 1, 0},
                {0, 0, 0}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expectedRegion, instance.buildRegion(2, 2));
        //
        expectedRegion = new ImageRegion(new int[][][] {
            {
                {1, 3, 1},
                {3, 9, 3},
                {1, 3, 1}
            }
        }, new Range<>(0, 100));
        ImageAssert.assertImage(expectedRegion, instance.buildRegion(1, 1));
    }

    public class RegionProcessImpl extends RegionProcess {

        public RegionProcessImpl(Image image, int size) {
            super(image, size);
        }

        public void process(ImageRegion imageRegion) {
        }
    }
    
}
