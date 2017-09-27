package org.paim.pdi;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import org.paim.commons.BinaryImage;
import org.paim.commons.Image;
import org.paim.commons.ImageFactory;
import org.paim.commons.Point;

/**
 * The snake process
 */
public class SnakeProcess extends ImageProcess<BinaryImage> {

    private final int MAXLEN = 16;
    private final int THRESHOLD = 100;

    private final BinaryImage binaryImage;
    private final int steps;
    private final int alpha;
    private final int beta;
    private final int gamma;
    private final int delta;

    private double[][] e_uniformity = new double[3][3];
    private double[][] e_curvature = new double[3][3];
    private double[][] e_flow = new double[3][3];
    private double[][] e_inertia = new double[3][3];

    private int[][] gradient;
    private int[][] flow;
    
    public SnakeProcess(Image image, int steps, int alpha, int beta, int gamma) {
        super(image);
        this.steps = steps;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.delta = 1;
        this.binaryImage = ImageFactory.buildBinaryImage(image.getWidth(), image.getHeight());
        setFinalizer(() -> {
            setOutput(binaryImage);
        });
    }

    @Override
    protected void processImage() {
        List<Point> points = buildInitialPoints();

        buildGradient();
        

        for (int i = 0; i < steps; i++) {
            List newPoints = step(points, gradient, flow);
            if (newPoints == null) {
                break;
            }
            points = newPoints;
        }
        buildBinaryImage(points);
    }

    private void buildBinaryImage(List<Point> points) {
        int[] xPoints = points.stream().map((it) -> {
            return it.x;
        }).mapToInt((it) -> it).toArray();
        int[] yPoints = points.stream().map((it) -> {
            return it.y;
        }).mapToInt((it) -> it).toArray();
        Polygon polygon = new Polygon(xPoints, yPoints, points.size());
        for (int x = 0; x < binaryImage.getWidth(); x++) {
            for (int y = 0; y < binaryImage.getHeight(); y++) {
                if (polygon.contains(x, y)) {
                    binaryImage.set(x, y, true);
                }
            }
        }
    }

    private void  buildGradient() {
        int W = image.getWidth();
        int H = image.getHeight();

        int[][] clum = new int[W][H];
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (image.isGrayScale()) {
                    clum[x][y] = image.get(Image.CHANNELS_GRAYSCALE, x, y);
                } else {

                    int r = image.get(Image.CHANNEL_RED, x, y);
                    int g = image.get(Image.CHANNEL_GREEN, x, y);
                    int b = image.get(Image.CHANNEL_BLUE, x, y);
                    clum[x][y] = ((int) (0.299D * r + 0.587D * g + 0.114D * b));
                }
            }
        }

        gradient = new int[image.getWidth()][image.getHeight()];
        int maxgradient = 0;
        for (int y = 0; y < H - 2; y++) {
            for (int x = 0; x < W - 2; x++) {
                int p00 = clum[(x + 0)][(y + 0)];
                int p10 = clum[(x + 1)][(y + 0)];
                int p20 = clum[(x + 2)][(y + 0)];
                int p01 = clum[(x + 0)][(y + 1)];
                int p21 = clum[(x + 2)][(y + 1)];
                int p02 = clum[(x + 0)][(y + 2)];
                int p12 = clum[(x + 1)][(y + 2)];
                int p22 = clum[(x + 2)][(y + 2)];
                int sx = p20 + 2 * p21 + p22 - (p00 + 2 * p01 + p02);
                int sy = p02 + 2 * p12 + p22 - (p00 + 2 * p10 + p10);
                int snorm = (int) Math.sqrt(sx * sx + sy * sy);
                gradient[(x + 1)][(y + 1)] = snorm;
                maxgradient = Math.max(maxgradient, snorm);
            }
        }

        boolean[][] binarygradient = new boolean[W][H];
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (gradient[x][y] > THRESHOLD * maxgradient / 100) {
                    binarygradient[x][y] = true;
                } else {
                    gradient[x][y] = 0;
                }
            }
        }

       flow = new int[W][H];
        double[][] cdist = new ChamferDistance(ChamferDistance.chamfer5).compute(binarygradient, W, H);
        for (int y = 0; y < H; y++) {
          for (int x = 0; x < W; x++) {
            flow[x][y] = ((int)(5.0D * cdist[x][y]));
          }
        }
    }

    private List<Point> buildInitialPoints() {

        double halfWidth = image.getWidth() / 2;
        double halfHeight = image.getHeight() / 2;

        double radius = (halfWidth + halfHeight) / 2;
        double perimeter = 6.28D * radius;
        int nmb = (int) (perimeter / MAXLEN);
        List<Point> circle = new ArrayList<>();
        for (int i = 0; i < nmb; i++) {
            double x = halfWidth + (halfWidth - 2) * Math.cos(6.28D * i / nmb);
            double y = halfHeight + (halfHeight - 2) * Math.sin(6.28D * i / nmb);
            circle.add(new Point((int) x, (int) y));
        }
        return circle;
    }

    private List<Point> step(List<Point> points, int[][] gradient, int[][] flow) {
        List newPoints = new ArrayList(points.size());
        boolean changed = false;
        double snakelength = this.getsnakelength(points);
        for (int i = 0; i < points.size(); i++) {
            Point prev = points.get((i + points.size() - 1) % points.size());
            Point cur = points.get(i);
            Point next = points.get((i + 1) % points.size());

            int dy = -1;
            while (dy <= 1) {
                int dx = -1;
                while (dx <= 1) {
                    Point p = new Point(cur.x + dx, cur.y + dy);
                    this.e_uniformity[1 + dx][1 + dy] = this.f_uniformity(prev, next, p, snakelength, points.size());
                    this.e_curvature[1 + dx][1 + dy] = this.f_curvature(prev, p, next);
                    this.e_flow[1 + dx][1 + dy] = this.f_gflow(cur, p, flow);
                    this.e_inertia[1 + dx][1 + dy] = this.f_inertia(cur, p, gradient);
                    ++dx;
                }
                ++dy;
            }
            this.normalize(this.e_uniformity);
            this.normalize(this.e_curvature);
            this.normalize(this.e_flow);
            this.normalize(this.e_inertia);
            double emin = Double.MAX_VALUE;
            double e = 0.0;
            int x = 0;
            int y = 0;
            int dy2 = -1;
            while (dy2 <= 1) {
                int dx = -1;
                while (dx <= 1) {
                    e = 0.0;
                    e += this.alpha * this.e_uniformity[1 + dx][1 + dy2];
                    e += this.beta * this.e_curvature[1 + dx][1 + dy2];
                    e += this.gamma * this.e_flow[1 + dx][1 + dy2];
                    if ((e += this.delta * this.e_inertia[1 + dx][1 + dy2]) < emin) {
                        emin = e;
                        x = cur.x + dx;
                        y = cur.y + dy2;
                    }
                    ++dx;
                }
                ++dy2;
            }
            if (x < 1) {
                x = 1;
            }
            if (x >= image.getWidth() - 1) {
                x = image.getWidth() - 2;
            }
            if (y < 1) {
                y = 1;
            }
            if (y >= image.getHeight() - 1) {
                y = image.getHeight() - 2;
            }
            if (x != cur.x || y != cur.y) {
                changed = true;
            }
            newPoints.add(new Point(x, y));

        }

        if (changed) {
            return newPoints;
        }

        return null;
    }

    private void normalize(double[][] array3x3) {
        int j;
        double sum = 0.0;
        int i = 0;
        while (i < 3) {
            j = 0;
            while (j < 3) {
                sum += Math.abs(array3x3[i][j]);
                ++j;
            }
            ++i;
        }
        if (sum == 0.0) {
            return;
        }
        i = 0;
        while (i < 3) {
            j = 0;
            while (j < 3) {
                double[] arrd = array3x3[i];
                int n = j++;
                arrd[n] = arrd[n] / sum;
            }
            ++i;
        }
    }

    private double getsnakelength(List<Point> snake) {
        double length = 0.0;
        int i = 0;
        while (i < snake.size()) {
            Point cur = snake.get(i);
            Point next = snake.get((i + 1) % snake.size());
            length += this.distance2D(cur, next);
            ++i;
        }
        return length;
    }

    private double distance2D(Point A, Point B) {
        int ux = A.x - B.x;
        int uy = A.y - B.y;
        double un = ux * ux + uy * uy;
        return Math.sqrt(un);
    }

    private double f_uniformity(Point prev, Point next, Point p, double snakeLength, int snakeSize) {
        double un = this.distance2D(prev, p);
        double avg = snakeLength / (double) snakeSize;
        double dun = Math.abs(un - avg);
        return dun * dun;
    }

    private double f_curvature(Point prev, Point p, Point next) {
        int ux = p.x - prev.x;
        int uy = p.y - prev.y;
        double un = Math.sqrt(ux * ux + uy * uy);
        int vx = p.x - next.x;
        int vy = p.y - next.y;
        double vn = Math.sqrt(vx * vx + vy * vy);
        if (un == 0.0 || vn == 0.0) {
            return 0.0;
        }
        double cx = (double) (vx + ux) / (un * vn);
        double cy = (double) (vy + uy) / (un * vn);
        double cn = cx * cx + cy * cy;
        return cn;
    }

    private double f_gflow(Point cur, Point p, int[][] flow) {
        int dcur = flow[cur.x][cur.y];
        int dp = flow[p.x][p.y];
        double d = dp - dcur;
        return d;
    }

    private double f_inertia(Point cur, Point p, int[][] gradient) {
        double d = this.distance2D(cur, p);
        double g = gradient[cur.x][cur.y];
        double e = g * d;
        return e;
    }

    private static class ChamferDistance {

        public static final int[][] cheessboard = {
            {1, 0, 1},
            {1, 1, 1}};

        public static final int[][] chamfer3 = {
            {1, 0, 3},
            {1, 1, 4}};

        public static final int[][] chamfer5 = {
            {1, 0, 5},
            {1, 1, 7},
            {2, 1, 11}};

        public static final int[][] chamfer7 = {
            {1, 0, 14},
            {1, 1, 20},
            {2, 1, 31},
            {3, 1, 44}};

        public static final int[][] chamfer13 = {
            {1, 0, 68},
            {1, 1, 96},
            {2, 1, 152},
            {3, 1, 215},
            {3, 2, 245},
            {4, 1, 280},
            {4, 3, 340},
            {5, 1, 346},
            {6, 1, 413}};

        private int[][] chamfer = null;
        private int normalizer = 0;

        private int width = 0;
        private int height = 0;

        public ChamferDistance() {
            this(chamfer3);
        }

        public ChamferDistance(int[][] chamfermask) {
            chamfer = chamfermask;
            normalizer = chamfer[0][2];
        }

        private void testAndSet(double[][] output, int x, int y, double newvalue) {
            if ((x < 0) || (x >= width)) {
                return;
            }
            if ((y < 0) || (y >= height)) {
                return;
            }
            double v = output[x][y];
            if ((v >= 0.0D) && (v < newvalue)) {
                return;
            }
            output[x][y] = newvalue;
        }

        public double[][] compute(boolean[][] input, int width, int height) {
            this.width = width;
            this.height = height;
            double[][] output = new double[width][height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (input[x][y]) {
                        output[x][y] = 0.0D;
                    } else {
                        output[x][y] = -1.0D;
                    }
                }
            }
            for (int y = 0; y <= height - 1; y++) {
                for (int x = 0; x <= width - 1; x++) {
                    double v = output[x][y];
                    if (v >= 0.0D) {
                        for (int k = 0; k < chamfer.length; k++) {
                            int dx = chamfer[k][0];
                            int dy = chamfer[k][1];
                            int dt = chamfer[k][2];

                            testAndSet(output, x + dx, y + dy, v + dt);
                            if (dy != 0) {
                                testAndSet(output, x - dx, y + dy, v + dt);
                            }
                            if (dx != dy) {
                                testAndSet(output, x + dy, y + dx, v + dt);
                                if (dy != 0) {
                                    testAndSet(output, x - dy, y + dx, v + dt);
                                }
                            }
                        }
                    }
                }
            }
            for (int y = height - 1; y >= 0; y--) {
                for (int x = width - 1; x >= 0; x--) {
                    double v = output[x][y];
                    if (v >= 0.0D) {
                        for (int k = 0; k < chamfer.length; k++) {
                            int dx = chamfer[k][0];
                            int dy = chamfer[k][1];
                            int dt = chamfer[k][2];

                            testAndSet(output, x - dx, y - dy, v + dt);
                            if (dy != 0) {
                                testAndSet(output, x + dx, y - dy, v + dt);
                            }
                            if (dx != dy) {
                                testAndSet(output, x - dy, y - dx, v + dt);
                                if (dy != 0) {
                                    testAndSet(output, x + dy, y - dx, v + dt);
                                }
                            }
                        }
                    }
                }
            }
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    output[x][y] /= normalizer;
                }
            }
            return output;
        }
    }
}
