package org.torax.pdi;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.torax.commons.Image;
import org.torax.commons.ImageRegion;
import org.torax.commons.Range;
import org.torax.commons.test.ImageAssert;

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
