import ImageProcessing.Histogram;
import org.opencv.core.Mat;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by omi on 4/23/16.
 */
public class RGBLoader {

    public RGBLoader(String filename){
        loadRGB(filename);
    }

    private void loadRGB(String filename){
        int width = 480;
        int height = 270;

        File file = new File(filename);
        int totalFrames = (int)file.length() / (480 * 270 * 3);

        try {
            InputStream is = new FileInputStream(file);
            Histogram hist = new Histogram();
            List<Mat> referenceHist = null;
            List<Mat> currentHist;
            ArrayList<Double> differences = new ArrayList<>();
            for(int i = 0; i < totalFrames; i++){

                long len = width*height*3;
                byte[] bytes = new byte[(int)len];

                int offset = 0;
                int numRead;
                while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                    offset += numRead;
                }

                if (referenceHist == null){
                    referenceHist = hist.getHistogram(bytes, width, height);
                    continue;
                }else{
                    currentHist = hist.getHistogram(bytes, width, height);
                }

                differences.add(hist.getDifference(referenceHist, currentHist, 3));
                System.out.println(Collections.max(differences));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
