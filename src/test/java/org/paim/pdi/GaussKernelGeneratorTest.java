package org.paim.pdi;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests of class GaussKernelGenerator
 */
public class GaussKernelGeneratorTest {

    /**
     * Test build kernel 2D
     */
    @Test
    public void testBuildKernel2D() {
        double[][] expecteds = {
            {0.09043853147283216, 0.11985293468149652, 0.09043853147283216},
            {0.11985293468149652, 0.15883413538268532, 0.11985293468149652},
            {0.09043853147283216, 0.11985293468149652, 0.09043853147283216}
        };
        Assert.assertArrayEquals(expecteds, GaussKernelGenerator.buildKernel2D(1.3, 3));
    }

}
