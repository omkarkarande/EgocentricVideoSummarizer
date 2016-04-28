package ImageProcessing;

import org.opencv.core.Mat;
import sun.plugin2.os.windows.FLASHWINFO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by omi on 4/24/16.
 */
public class RGBSummarize {


    private int WIDTH;
    private int HEIGHT;
    private double MAX_DIFF = 259200;
    private double THRESHOLD = 0.35 * MAX_DIFF;
    private String filename;
    private ArrayList<Integer> referenceFrames;

    public RGBSummarize(String filename){
        this.filename = filename;
        referenceFrames = new ArrayList<>();
    }

    public ArrayList<Integer> processRGB(){
        this.WIDTH = 480;
        this.HEIGHT = 270;

        File file = new File(filename);
        int totalFrames = (int)file.length() / (480 * 270 * 3);

        try {
            InputStream is = new FileInputStream(file);
            Histogram hist = new Histogram();
            List<Mat> referenceHist = null;
            List<Mat> currentHist;
            BufferedImage img;
            for(int i = 0; i < totalFrames; i++){
                img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                long len = WIDTH*HEIGHT*3;
                byte[] bytes = new byte[(int)len];

                int offset = 0;
                int numRead;
                while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                    offset += numRead;
                }

                //Generate Buffered Image
                int ind = 0;
                for(int y = 0; y < HEIGHT; y++){

                    for(int x = 0; x < WIDTH; x++){

                        byte a = 0;
                        byte r = bytes[ind];
                        byte g = bytes[ind+HEIGHT*WIDTH];
                        byte b = bytes[ind+HEIGHT*WIDTH*2];

                        int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                        img.setRGB(x,y,pix);
                        ind++;
                    }
                }

                if (referenceHist == null){
                    referenceHist = hist.getHistogram(bytes, WIDTH, HEIGHT);
                    referenceFrames.add(i);
                    continue;
                }else{
                    currentHist = hist.getHistogram(bytes, WIDTH, HEIGHT);
                }

                if (hist.getDifference(currentHist, referenceHist, 3) >= THRESHOLD){
                    System.out.println("Threshold crossed.");
                    referenceHist = currentHist;
                    referenceFrames.add(i);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return referenceFrames;
    }

    /*
    public void dumpFrames(ArrayList<Integer> referenceFrames) throws Exception{
        BufferedImage img;
        int currentRefIndex = 0;
        int referenceFrame = referenceFrames.get(currentRefIndex);
        Set<Integer> writtenFrames = new LinkedHashSet<>();

        int imageID = 1;
        for(int i = 0; i < TOTAL_FRAMES; i++){
            img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            long len = WIDTH*HEIGHT*3;
            byte[] bytes = new byte[(int)len];

            int offset = 0;
            int numRead;
            while (offset < bytes.length && (numRead=FRAMES.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }

            if (i >= referenceFrame - 15 && i <= referenceFrame + 15 && !writtenFrames.contains(i)){
                //Generate Buffered Image
                int ind = 0;
                for(int y = 0; y < HEIGHT; y++){

                    for(int x = 0; x < WIDTH; x++){

                        byte a = 0;
                        byte r = bytes[ind];
                        byte g = bytes[ind+HEIGHT*WIDTH];
                        byte b = bytes[ind+HEIGHT*WIDTH*2];

                        int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                        //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                        img.setRGB(x,y,pix);
                        ind++;
                    }
                }
                ImageIO.write(img, "jpg", new File("/home/omi/Documents/samples/sample" + imageID + ".jpg"));
                imageID += 1;
                System.out.println("Writing " + i);
            }

            if (i == referenceFrame + 15){
                currentRefIndex += 1;
                if (currentRefIndex < referenceFrames.size()){
                    referenceFrame = referenceFrames.get(currentRefIndex);
                }
            }

        }
    }
    */
}
