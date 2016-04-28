package imageProcessing;


import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;


public class ImageProcessing {
    private InputStream videoIS;
    private int TOTAL_FRAMES;
    private int width;
    private int height;
    private int frameLength;
    private ArrayList<Integer> keyFrames;
    private double MAX_DIFF = 259200;
    private double THRESHOLD = 0.35 * MAX_DIFF;
    Histogram hist;

    public ImageProcessing(String fileName, int totalFrames, int width, int height) {
        try {
            videoIS = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.videoIS = videoIS;
        this.TOTAL_FRAMES = totalFrames;
        this.width = width;
        this.height = height;
        this.frameLength = width * height * 3;
        this.hist = new Histogram();
        this.keyFrames = new ArrayList<>();
    }

    public ArrayList<Integer> generateKeyFrames() {

        BufferedImage img;
        List<Mat> referenceHist = null;
        List<Mat> currentHist;
        for (int i = 0; i < TOTAL_FRAMES; i++) {

            byte[] bytes = new byte[(int) frameLength];
            int offset = 0;
            int numRead;
            try {
                while (offset < bytes.length && (numRead = videoIS.read(bytes, offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (referenceHist == null) {
                //referenceHist = new ArrayList<Mat>();
                referenceHist = hist.getHistogram(bytes, width, height);
                keyFrames.add(i);
                continue;
            } else {
                currentHist = hist.getHistogram(bytes, width, height);
            }

            if (hist.getDifference(currentHist, referenceHist, 3) >= THRESHOLD) {
                System.out.println("Threshold crossed.");
                referenceHist = currentHist;
                keyFrames.add(i);
            }
        }

        return keyFrames;
    }
}