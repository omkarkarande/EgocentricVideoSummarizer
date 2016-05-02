package ImageProcessing;

import Configurations.Settings;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.opencv.core.Core.absdiff;
import static org.opencv.core.Core.sumElems;

/**
 * Created by omi on 4/23/16.
 * Generates and return a histogram based on the bufferedimage passed
 */
public class Histogram {

    private Mat IMAGE;
    private List<Mat> histograms;

    /*
    Constructor
     */
    public Histogram() {
    }

    /*
    Computes Histogram values for all channels in the image.
     */
    private void computeChannelHistograms() {
        //create a new empty list of Mats
        this.histograms = new ArrayList<>();
        //split input images into seperate channels
        Core.split(this.IMAGE, this.histograms);

        //Histogram Mat settings
        MatOfInt histogramSize = new MatOfInt(Settings.VALUES_PER_CHANNEL);
        MatOfFloat histogramRange = new MatOfFloat(0f, (float) Settings.VALUES_PER_CHANNEL);

        //array of Mats to store histogram for each channel
        Mat[] hists = new Mat[Settings.CHANNELS];
        for (int i = 0; i < Settings.CHANNELS; i++) {
            hists[i] = new Mat();
            //calculate histogram
            Imgproc.calcHist(Arrays.asList(this.histograms.get(i)), new MatOfInt(0), new Mat(), hists[i], histogramSize, histogramRange, Settings.HISTOGRAM_ACCUMULATE);
        }

        this.histograms.clear();
        for (int i = 0; i < Settings.CHANNELS; i++) {
            this.histograms.add(hists[i]);
        }
    }

    /*
    Takes in a BufferedImage and returns the Histogram
    Returns and List of Mats where each Mat is the histogram for the corresponding channel
     */
    public List<Mat> getHistogram(BufferedImage image) {
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        this.IMAGE = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        this.IMAGE.put(0, 0, pixels);
        computeChannelHistograms();
        return this.histograms;
    }

    /*
    Takes in a byte[] representing the image and returns the Histogram
    Returns and List of Mats where each Mat is the histogram for the corresponding channel
     */
    public List<Mat> getHistogram(byte[] image, int width, int height) {
        // load image into OpenCV Mat 8UC3 = 8 bits 3 channels
        this.IMAGE = new Mat(height, width, CvType.CV_8UC3);
        this.IMAGE.put(0, 0, image);
        computeChannelHistograms();
        return this.histograms;
    }

    /*
    Calculates the mean absolute difference between two histograms for all channels
     */
    public double getDifference(List<Mat> histogramA, List<Mat> histogramB) {
        double value = 0.0;
        for (int i = 0; i < Settings.CHANNELS; i++) {
            Mat diff = new Mat();
            absdiff(histogramA.get(i), histogramB.get(i), diff);
            value += sumElems(diff).val[0];
        }
        return value / 3.0;
    }
}
