package ImageProcessing;

import org.opencv.core.Mat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omi on 4/24/16.
 */
public class RGBSummarize {

    private static int WIDTH = 480;
    private static int HEIGHT = 270;
    private static int CHANNELS = 3;
    private static int BYTES_PER_FRAME = WIDTH * HEIGHT * CHANNELS;
    private static double MAX_DIFF = 259200;
    private static double THRESHOLD_PERCENT = 0.35;
    private static double THRESHOLD = THRESHOLD_PERCENT * MAX_DIFF;

    private String INPUT_FILE_NAME;
    private ArrayList<Integer> referenceFrames;

    public RGBSummarize(String filename) {
        this.INPUT_FILE_NAME = filename;
        referenceFrames = new ArrayList<>();
    }

    public ArrayList<Integer> processRGB() {
        //open the file for reading
        File file = new File(INPUT_FILE_NAME);
        int totalFrames = (int) file.length() / BYTES_PER_FRAME;

        try {
            //open an input stream
            InputStream is = new FileInputStream(file);
            Histogram hist = new Histogram();
            List<Mat> referenceHist = null;
            List<Mat> currentHist;

            for (int i = 0; i < totalFrames; i++) {
                byte[] bytes = new byte[(int) BYTES_PER_FRAME];

                int offset = 0;
                int numRead;
                while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }

                if (referenceHist == null) {
                    referenceHist = hist.getHistogram(bytes, WIDTH, HEIGHT);
                    referenceFrames.add(i);
                    continue;
                } else {
                    currentHist = hist.getHistogram(bytes, WIDTH, HEIGHT);
                }

                // add frame as reference frame is the threshold is crossed
                if (hist.getDifference(currentHist, referenceHist) >= THRESHOLD) {
                    referenceHist = currentHist;
                    referenceFrames.add(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return the frames as a list of integers
        return referenceFrames;
    }
}
