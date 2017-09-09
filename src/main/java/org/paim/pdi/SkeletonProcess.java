package org.paim.pdi;

import org.paim.commons.Image;

/**
 * Skeleton process
 */
public abstract class SkeletonProcess extends ImageProcess<Image> {

    public SkeletonProcess(Image image) {
        super(image);
    }
    
    /**
     * Returns neighborhood
     * 
     * @param pixels
     * @return int[]
     */
    protected int[] neighborhood(int[][] pixels) {
         int p2 = pixels[1][0] / 255;
        int p3 = pixels[2][0] / 255;
        int p4 = pixels[2][1] / 255;
        int p5 = pixels[2][2] / 255;
        int p6 = pixels[1][2] / 255;
        int p7 = pixels[0][2] / 255;
        int p8 = pixels[0][1] / 255;
        int p9 = pixels[0][0] / 255;
        return new int[]{p2, p3, p4, p5, p6, p7, p8, p9};
    }
    
    /**
     * Returns true if it is an edge
     * 
     * @param neighborhood
     * @return boolean
     */
    protected boolean isEdge(int[] neighborhood) {
        int np = neighborhood[0] + neighborhood[1] + neighborhood[2] + 
                neighborhood[3] + neighborhood[4] + neighborhood[5] + 
                neighborhood[6] + neighborhood[7];
        int sp = (neighborhood[0] < neighborhood[1] ? 1 : 0)
                + (neighborhood[1] < neighborhood[2] ? 1 : 0)
                + (neighborhood[2] < neighborhood[3] ? 1 : 0)
                + (neighborhood[3] < neighborhood[4] ? 1 : 0)
                + (neighborhood[4] < neighborhood[5] ? 1 : 0)
                + (neighborhood[5] < neighborhood[6] ? 1 : 0)
                + (neighborhood[6] < neighborhood[7] ? 1 : 0)
                + (neighborhood[7] < neighborhood[0] ? 1 : 0);
        return (np >= 2 && np <= 6) && sp == 1;
    }
    
}
