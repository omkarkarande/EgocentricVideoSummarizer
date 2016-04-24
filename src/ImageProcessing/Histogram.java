package ImageProcessing;

import com.sun.org.apache.xpath.internal.SourceTree;
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
public class Histogram{

    private Mat IMAGE;

    private List<Mat> histograms;

    public Histogram(){
        System.out.println("Jewbudbujslxmk");
    }

    private void computeChannelHistograms(){
        this.histograms = new ArrayList<Mat>();
        Core.split(this.IMAGE, this.histograms);

        MatOfInt histogramSize = new MatOfInt(256);
        MatOfFloat histogramRange = new MatOfFloat(0f, 256f);

        boolean accumulate = false;
        Mat r_hist = new Mat();
        Mat g_hist = new Mat();
        Mat b_hist = new Mat();

        Imgproc.calcHist(Arrays.asList(this.histograms.get(0)), new MatOfInt(0), new Mat(), r_hist, histogramSize, histogramRange, accumulate);
        Imgproc.calcHist(Arrays.asList(this.histograms.get(1)), new MatOfInt(0), new Mat(), g_hist, histogramSize, histogramRange, accumulate);
        Imgproc.calcHist(Arrays.asList(this.histograms.get(2)), new MatOfInt(0), new Mat(), b_hist, histogramSize, histogramRange, accumulate);

        this.histograms.clear();
        this.histograms.add(r_hist);
        this.histograms.add(g_hist);
        this.histograms.add(b_hist);
    }

    public List<Mat> getHistogram(BufferedImage image){
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        this.IMAGE = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        this.IMAGE.put(0, 0, pixels);
        //Imgproc.cvtColor(this.IMAGE, this.IMAGE, Imgproc.COLOR_RGB2BGR);
        computeChannelHistograms();
        return this.histograms;
    }
    public List<Mat> getHistogram(byte[] image, int width, int height){
        // load image into OpenCV Mat 8UC3 = 8 bits 3 channels
        this.IMAGE = new Mat(height, width, CvType.CV_8UC3);
        this.IMAGE.put(0, 0, image);
        //Imgproc.cvtColor(this.IMAGE, this.IMAGE, Imgproc.COLOR_RGB2BGR);
        computeChannelHistograms();
        return this.histograms;
    }

    public double getDifference(List<Mat> histogramA, List<Mat> histogramB, int numChannels){
        double value = 0.0;

        for (int i = 0; i < numChannels; i++){
            Mat diff = new Mat();
            absdiff(histogramA.get(i), histogramB.get(i), diff);
            value += sumElems(diff).val[0];
        }

        return value / 3.0;
    }
}
