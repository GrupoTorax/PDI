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
    private final int THRESHOLD = 25;

    private final BinaryImage binaryImage;
    private final int steps;
    private final double alpha;
    private final double beta;
    private final double gamma;
    private final double delta;
    private final double[][] energyUniformity = new double[3][3];
    private final double[][] energyCurvature = new double[3][3];
    private final double[][] energyFlow = new double[3][3];
    private final double[][] energyInertia = new double[3][3];
    private int[][] gradient;
    private int[][] flow;

    public SnakeProcess(Image image, int steps, double alpha, double beta, double gamma) {
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

    /**
     * Creates a binary imagen from a list of points
     * 
     * @param points 
     */
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
    
    /**
     * Builds de gradient/flow matrix
     */
    private void buildGradient() {
        int width = image.getWidth();
        int height = image.getHeight();
        GradientProcess gradientProcess = new GradientProcess(image);
        gradientProcess.process();
        gradient = gradientProcess.getOutput().getData()[0];
        boolean[][] binarygradient = new boolean[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (gradient[x][y] > THRESHOLD * gradientProcess.getMaxGradient() / 100) {
                    binarygradient[x][y] = true;
                } else {
                    gradient[x][y] = 0;
                }
            }
        }
        flow = new int[width][height];
        double[][] cdist = new ChamferDistance().compute(binarygradient, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flow[x][y] = ((int) (5.0D * cdist[x][y]));
            }
        }
    }
    
    /**
     * Builds the snake initial points 
     * 
     * @return {@code List<Point>}
     */
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

    /**
     * Execute a point search, if the snake don't change, the method returns null
     * 
     * @param points
     * @param gradient
     * @param flow
     * @return {@code List<Point>}
     */
    private List<Point> step(List<Point> points, int[][] gradient, int[][] flow) {
        List newPoints = new ArrayList();
        boolean changed = false;
        double snakelength = this.getsnakelength(points);
        for (int i = 0; i < points.size(); i++) {
            Point prev = points.get((i + points.size() - 1) % points.size());
            Point cur = points.get(i);
            Point next = points.get((i + 1) % points.size());
            for (int dy = -1; dy < 2; dy++) {
                for (int dx = -1; dx < 2; dx++) {
                    Point p = new Point(cur.x + dx, cur.y + dy);
                    energyUniformity[1 + dx][1 + dy] = this.forceUniformity(prev, p, snakelength, points.size());
                    energyCurvature[1 + dx][1 + dy] = this.forceCurvature(prev, p, next);
                    energyFlow[1 + dx][1 + dy] = this.forceGflow(cur, p, flow);
                    energyInertia[1 + dx][1 + dy] = this.forceInertia(cur, p, gradient);
                }
            }
            normalize(energyUniformity);
            normalize(energyCurvature);
            normalize(energyFlow);
            normalize(energyInertia);
            double emin = Double.MAX_VALUE;
            int x = 0;
            int y = 0;
            for (int dy = -1; dy < 2; dy++) {
                for (int dx = -1; dx < 2; dx++) {
                    double e = 0.0;
                    e += alpha * energyUniformity[1 + dx][1 + dy];
                    e += beta * energyCurvature[1 + dx][1 + dy];
                    e += gamma * energyFlow[1 + dx][1 + dy];
                    if ((e += delta * energyInertia[1 + dx][1 + dy]) < emin) {
                        emin = e;
                        x = cur.x + dx;
                        y = cur.y + dy;
                    }
                }
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

    /**
     * Normalize the points
     * 
     * @param array3x3 
     */
    private void normalize(double[][] array3x3) {
        double sum = 0.0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sum += Math.abs(array3x3[i][j]);
            }
        }
        if (sum == 0.0) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array3x3[i][j] = array3x3[i][j] / sum;
            }
        }
    }

    /**
     * Returns the snake length
     * 
     * @param snake
     * @return double
     */
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

    /**
     * Calculate the distance between two points
     * 
     * @param a
     * @param b
     * @return double
     */
    private double distance2D(Point a, Point b) {
        int ux = a.x - b.x;
        int uy = a.y - b.y;
        double un = ux * ux + uy * uy;
        return Math.sqrt(un);
    }
    
    /**
     * Calculate the Uniformity force
     * 
     * @param prev
     * @param p
     * @param snakeLength
     * @param snakeSize
     * @return double
     */
    private double forceUniformity(Point prev, Point p, double snakeLength, int snakeSize) {
        double un = this.distance2D(prev, p);
        double avg = snakeLength / (double) snakeSize;
        double dun = Math.abs(un - avg);
        return dun * dun;
    }

    /**
     * Calculate the Curvature force
     * 
     * @param prev
     * @param p
     * @param next
     * @return double
     */
    private double forceCurvature(Point prev, Point p, Point next) {
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
        return cx * cx + cy * cy;
    }

    /**
     * Calculate de flow force
     * 
     * @param cur
     * @param p
     * @param flow
     * @return double
     */
    private double forceGflow(Point cur, Point p, int[][] flow) {
        int dcur = flow[cur.x][cur.y];
        int dp = flow[p.x][p.y];
        return dp - dcur;
    }

    /**
     * Calculate the inertial force 
     * 
     * @param cur
     * @param p
     * @param gradient
     * @return double
     */
    private double forceInertia(Point cur, Point p, int[][] gradient) {
        double d = this.distance2D(cur, p);
        double g = gradient[cur.x][cur.y];
        return g * d;
    }

    private static class ChamferDistance {

        private final int[][] chamfer = {
            {1, 0, 5},
            {1, 1, 7},
            {2, 1, 11}
        };

        private int width = 0;
        private int height = 0;

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
                    output[x][y] /= 5;
                }
            }
            return output;
        }
    }
}
