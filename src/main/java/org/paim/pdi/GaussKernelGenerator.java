package org.paim.pdi;

import java.util.ArrayList;
import java.util.List;

/**
 * Gauss kernel generator
 */
public class GaussKernelGenerator {

    /** Default sample count */
    public static final int DEFAULT_SAMPLE_COUNT = 100;

    /**
     * Builds a Gauss kernel with the number of samples being
     * {@link DEFAULT_SAMPLE_COUNT}
     *
     * @param sigma
     * @param kernelSize
     * @return double[]
     */
    public static double[] buildKernel(double sigma, int kernelSize) {
        return buildKernel(sigma, kernelSize, DEFAULT_SAMPLE_COUNT);
    }

    /**
     * Builds a Gauss kernel with the number of samples being
     * {@link DEFAULT_SAMPLE_COUNT}
     *
     * @param sigma
     * @param kernelSize
     * @return double[]
     */
    public static double[][] buildKernel2D(double sigma, int kernelSize) {
        double[] temp = buildKernel(sigma, kernelSize, DEFAULT_SAMPLE_COUNT);
        double[][] kernel = new double[kernelSize][kernelSize];
        int x = 0;
        int y = 0;
        for (int i = 0; i < kernelSize; i++) {
            for (int j = 0; j < kernelSize; j++) {
                kernel[x][y] = temp[i] * temp[j];
                y++;
            }
            y = 0;
            x++;
        }
        return kernel;
    }
    
    /**
     * Builds a Gauss kernel with the number of samples being
     * 
     * @param sigma
     * @param size
     * @return {@code double[][]}
     */
    public static double[][] buildSimpleKernel2D(double sigma, int size) {
        int r = size / 2;
        double[][] kernel = new double[size][size];
        // compute kernel
        double sum = 0;
        for (int y = -r, i = 0; i < size; y++, i++) {
            for (int x = -r, j = 0; j < size; x++, j++) {
                kernel[i][j] = function2D(x, y, sigma);
                sum += kernel[i][j];
            }
        }
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                kernel[i][j] /= sum;
            }
        }
        return kernel;
    }
    
    public static double function2D(double x, double y, double sigma) {
        return Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
    }

    /**
     * Builds a Gauss kernel
     *
     * @param sigma
     * @param kernelSize
     * @param sampleCount
     * @return double[]
     */
    public static double[] buildKernel(double sigma, int kernelSize, double sampleCount) {
        double samplesPerBin = Math.ceil(sampleCount / kernelSize);
        // need an even number of intervals for simpson integration => odd number of samples
        if (samplesPerBin % 2 == 0) {
            ++samplesPerBin;
        }
        double weightSum = 0;
        double kernelLeft = -Math.floor(kernelSize / 2);
        List<Double> allSamples = new ArrayList<>();
        // now sample kernel taps and calculate tap weights
        for (int tap = 0; tap < kernelSize; ++tap) {
            double left = kernelLeft - 0.5 + tap;
            List<Double[]> tapSamples = calcSamplesForRange(left, left + 1, samplesPerBin, sigma);
            double tapWeight = integrateSimphson(tapSamples);
            allSamples.add(tapWeight);
            weightSum += tapWeight;
        }
        double[] sampleArray = new double[allSamples.size()];
        // renormalize kernel and round to 6 decimals
        for (int i = 0; i < allSamples.size(); ++i) {
            sampleArray[i] = allSamples.get(i) / weightSum;
        }
        return sampleArray;
    }

    private static List<Double[]> calcSamplesForRange(double minInclusive, double maxInclusive, double samplesPerBin, double sigma) {
        return sampleInterval(minInclusive,
                maxInclusive,
                samplesPerBin, sigma);
    }

    private static List<Double[]> sampleInterval(double minInclusive, double maxInclusive, double sampleCount, double sigma) {
        List<Double[]> result = new ArrayList<>();
        double stepSize = (maxInclusive - minInclusive) / (sampleCount - 1);
        for (int s = 0; s < sampleCount; ++s) {
            double x = minInclusive + s * stepSize;
            double y = gaussianDistribution(x, sigma);
            result.add(new Double[]{x, y});
        }
        return result;
    }

    private static double integrateSimphson(List<Double[]> samples) {
        double result = samples.get(0)[1] + samples.get(samples.size() - 1)[1];
        for (int s = 1; s < samples.size() - 1; ++s) {
            double sampleWeight = (s % 2 == 0) ? 2.0 : 4.0;
            result += sampleWeight * samples.get(s)[1];
        }
        double h = (samples.get(samples.size() - 1)[0] - samples.get(0)[0]) / (samples.size() - 1);
        return result * h / 3.0;
    }

    private static double gaussianDistribution(double x, double sigma) {
        double n = 1.0 / (Math.sqrt(2 * Math.PI) * sigma);
        return Math.exp(-x * x / (2 * sigma * sigma)) * n;
    }

}
