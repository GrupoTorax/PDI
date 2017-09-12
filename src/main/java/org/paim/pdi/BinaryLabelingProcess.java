package org.paim.pdi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.paim.commons.BinaryImage;
import org.paim.commons.BinaryLabeling;
import org.paim.commons.Bounds;
import org.paim.commons.Image;
import org.paim.commons.ImageFactory;

/**
 * Binary labeling process
 */
public class BinaryLabelingProcess extends PixelProcess<BinaryLabeling> {

    /** Initial label */
    private static final int INITIAL_LABEL = 2;
    /** Output */
    private final Image out;
    /** Index label */
    private int indexLabel;

    /**
     * Creates a new BinaryLabeling process
     * 
     * @param image 
     */
    public BinaryLabelingProcess(Image image) {
        super(image);
        this.out = new Image(this.image);
        this.indexLabel = INITIAL_LABEL;
        setFinalizer(() -> {
            setOutput(new BinaryLabeling(out, indexLabel - INITIAL_LABEL));
        });
    }

    @Override
    protected void process(int channel, int x, int y, int value) {
        if (out.get(channel, x, y) == 1) {
            compareLabel(channel, x, y, indexLabel++);
        }
    }

    /**
     * Returns the label size
     *
     * @param label
     * @return int
     */
    public int getSize(int label) {
        int compareLabel = label + 1;
        int size = 0;
        for (int y = 0; y < out.getHeight(); y++) {
            for (int x = 0; x < out.getWidth(); x++) {
                if (out.get(0, x, y) == compareLabel) {
                    size++;
                }
            }
        }
        return size;
    }

    /**
     * Returns the last label used
     *
     * @return int
     */
    public int getLastLabel() {
        return indexLabel - INITIAL_LABEL;
    }
    
    /**
     * Returns a binary matrix
     *
     * @param label
     * @return boolean[][]
     */
    public boolean[][] getMatrix(int label) {
        boolean[][] mat = new boolean[out.getWidth()][out.getHeight()];
        int compareLabel = label + 1;
        for (int y = 0; y < out.getHeight(); y++) {
            for (int x = 0; x < out.getWidth(); x++) {
                if (out.get(0, x, y) == compareLabel) {
                    mat[x][y] = true;
                }
            }
        }
        return mat;
    }

    /**
     * Execute the label comparison
     *
     * @param channel
     * @param x
     * @param y
     * @param index
     */
    private void compareLabel(int channel, int x, int y, int index) {
        if (x < 0 || x >= out.getWidth() || y < 0 || y >= out.getHeight()) {
            return;
        }
        if (out.get(channel, x, y) == 1) {
            out.set(channel, x, y, index);
            compareLabel(channel, x - 1, y, index);
            compareLabel(channel, x, y - 1, index);
            compareLabel(channel, x, y + 1, index);
            compareLabel(channel, x + 1, y, index);
        }
    }
    
    /**
     * Returns the objects extracted by this process
     * 
     * @return ObjectList
     */
    public ObjectList getExtractedObjects() {
        Map<Integer, ExtractedObject> map = new HashMap<>();
        for (int y = 0; y < out.getHeight(); y++) {
            for (int x = 0; x < out.getWidth(); x++) {
                int label = out.get(0, x, y);
                if (label < INITIAL_LABEL) {
                    continue;
                }
                ExtractedObject object = map.get(label);
                if (object == null) {
                    object = new ExtractedObject(label);
                    map.put(label, object);
                }
                object.size++;
                object.matrix.set(x, y, true);
            }
        }
        return new ObjectList(map.values());
    }
    
    /**
     * Object list identified by the process
     */
    public class ObjectList extends ArrayList<ExtractedObject> {

        /**
         * Creates a new Object list based on a collection
         * @param collection 
         */
        public ObjectList(Collection<ExtractedObject> collection) {
            super(collection);
        }
        
        /**
         * Sorts by size in decrescent order (Largest first)
         * 
         * @return the same list
         */
        public ObjectList sortBySizeLargestFirst() {
            sort((ExtractedObject o1, ExtractedObject o2) -> {
                return o2.size - o1.size;
            });
            return this;
        }

        @Override
        public ObjectList subList(int fromIndex, int toIndex) {
            return new ObjectList(super.subList(fromIndex, toIndex));
        }

        /**
         * Returns a subset of this list
         * 
         * @param size
         * @return ObjectList
         */
        public ObjectList subList(int size) {
            return new ObjectList(super.subList(0, size));
        }
        
    }
    
    /**
     * Extracted object
     */
    public class ExtractedObject {
        
        /** Label of this object */
        private final int label;
        /** Matrix of this object */
        private final BinaryImage matrix;
        /** Size */
        private int size;
        /** Perimeter */
        private int perimeter;
        /**
         * Creates a new extracted object
         * 
         * @param label
         */
        public ExtractedObject(int label) {
            this.label = label;
            this.matrix = new BinaryImage(ImageFactory.buildBinaryImage(out.getWidth(), out.getHeight()));
            this.size = 0;
            this.perimeter = -1;
        }

        /**
         * Returns the label of this object
         * 
         * @return int
         */
        public int getLabel() {
            return label;
        }

        /**
         * Returns the matrix of this object
         * 
         * @return BinaryImage
         */
        public BinaryImage getMatrix() {
            return matrix;
        }

        /**
         * Returns the size of this object (Area)
         * 
         * @return int
         */
        public int getSize() {
            return size;
        }
        
        public int getPerimeter() {
            if (perimeter == -1) {
                perimeter = 0;
                for (int x = 0; x < matrix.getWidth(); x++) {
                    for (int y = 0; y < matrix.getHeight(); y++) {
                        if(matrix.get(x, y)) {
                            if (x == 0 || y == 0 || x == matrix.getWidth() - 1 || y == matrix.getHeight() - 1) {
                                perimeter++;
                            } else {
                                if(!matrix.get(x, y - 1) || !matrix.get(x - 1, y) || !matrix.get(x + 1, y) || !matrix.get(x, y + 1)) {
                                    perimeter++;
                                }
                            }
                        }
                    }
                }
            }
            return perimeter;
        }
        
        public double getCircularity() {
            double p = getPerimeter();
            double a = getSize();
            double c = (p * p) / (4 * Math.PI * a);
            return c;
        }

        @Override
        public String toString() {
            return "ExtractedObject{" + "label=" + label + ", matrix=" + matrix + ", size=" + size + '}';
        }

        
        
    }

}
