package org.paim.pdi;

import static java.lang.Integer.sum;
import java.util.HashMap;
import java.util.Map;
import org.paim.commons.Image;

/**
 * Blur using the mode of the values
 */
public class ModeBlurProcess  extends SimpleConvolutionProcess {

    /**
     * Creates a new Blur using the mode of the values process
     *
     * @param image
     */
    public ModeBlurProcess(Image image) {
        super(image);
    }

    @Override
    protected int computeCenter(int[][] neighbours) {
        Map<Integer, Integer> occurances = new HashMap();
        for (int x = 0; x < neighbours.length; x++) {
            for (int y = 0; y < neighbours[x].length; y++) {
                int value = neighbours[x][y];
                if (!occurances.containsKey(value)) {
                    occurances.put(value, 0);
                }
                occurances.put(value, occurances.get(value) + 1);
            }
        }
        int largest = -1;
        for (Map.Entry<Integer, Integer> entry : occurances.entrySet()) {
            if (largest == -1 || entry.getValue() > occurances.get(largest)) {
                largest = entry.getKey();
            }
        }
        // If the mode only occurs once, there is no mode and so returns the original pixel
        if (occurances.get(largest) == 1) {
            return neighbours[1][1];
        }
        return largest;
    }    

}