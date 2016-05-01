package ImageProcessing;

import MediaLoader.ImageLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by omi on 4/24/16.
 */
public class RGBSummarize {

    private static double MAX_DIFF = 259200;
    private static double THRESHOLD_PERCENT = 0.35;
    private static double THRESHOLD = THRESHOLD_PERCENT * MAX_DIFF;

    private ImageLoader loader;
    private ArrayList<Integer> referenceFrames;

    private static double CLUSTER_THRESHOLD = 0.25 * MAX_DIFF;

    public RGBSummarize(String filename) {
        loader = new ImageLoader(filename);
        referenceFrames = new ArrayList<>();
    }

    public ArrayList<Integer> processRGB() {

        Histogram hist = new Histogram();
        List<Mat> referenceHist = null;
        List<Mat> currentHist;

        for (int i = 0; i < loader.getTotalFrames(); i++) {
            byte[] bytes = loader.getNext();

            if (referenceHist == null) {
                referenceHist = hist.getHistogram(bytes, loader.getWidth(), loader.getHeight());
                referenceFrames.add(i);
                continue;
            } else {
                currentHist = hist.getHistogram(bytes, loader.getWidth(), loader.getHeight());
            }

            // add frame as reference frame is the threshold is crossed
            if (hist.getDifference(currentHist, referenceHist) >= THRESHOLD) {
                referenceHist = currentHist;
                referenceFrames.add(i);
            }

        }
        //return the frames as a list of integers
        return referenceFrames;
    }

}
