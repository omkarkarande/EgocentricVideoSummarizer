import ImageProcessing.Histogram;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by omi on 4/23/16.
 */
public class RGBLoader {

    private double MAX_DIFF = 259200;
    private double THRESHOLD = 0.35 * MAX_DIFF;

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
            BufferedImage img;
            for(int i = 0; i < totalFrames; i++){
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                long len = width*height*3;
                byte[] bytes = new byte[(int)len];

                int offset = 0;
                int numRead;
                while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                    offset += numRead;
                }

                //Generate Buffered Image
                int ind = 0;
                for(int y = 0; y < height; y++){

                    for(int x = 0; x < width; x++){

                        byte a = 0;
                        byte r = bytes[ind];
                        byte g = bytes[ind+height*width];
                        byte b = bytes[ind+height*width*2];

                        int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                        //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                        img.setRGB(x,y,pix);
                        ind++;
                    }
                }

                if (referenceHist == null){
                    referenceHist = hist.getHistogram(bytes, width, height);
                    ImageIO.write(img, "jpg", new File("/home/omi/Documents/samples/sample" + i + ".jpg"));
                    continue;
                }else{
                    currentHist = hist.getHistogram(bytes, width, height);
                }

                if (hist.getDifference(currentHist, referenceHist, 3) >= THRESHOLD){
                    System.out.println("Threshold crossed.");
                    referenceHist = currentHist;
                    ImageIO.write(img, "jpg", new File("/home/omi/Documents/samples/sample" + i + ".jpg"));
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
