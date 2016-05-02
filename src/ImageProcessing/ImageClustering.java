package ImageProcessing;

import MediaLoader.ImageLoader;
import Cluster.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.KNearest;
import org.opencv.ml.Ml;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by omi on 4/28/16.
 */
public class ImageClustering {

    private ImageLoader loader;
    private int BUCKETS = 28;
    private Cluster[] CLUSTERS;

    private String INPUT_FILE;

    public ImageClustering(String filename) {
        this.INPUT_FILE = filename;
        this.loader = new ImageLoader(filename);
        CLUSTERS = new Cluster[BUCKETS];
        for(int i = 0; i < BUCKETS; i++){
            CLUSTERS[i] = new Cluster(i);
        }
    }

    public void setBuckets(int buckets) {
        this.BUCKETS = buckets;
    }

    public void clusterImages() {
        //ArrayList<Integer> colors = new ArrayList<>();
        Mat images = new Mat(loader.getTotalFrames(), loader.getPixelsPerFrame(), CvType.CV_32F);
        for (int i = 0; i < loader.getTotalFrames(); i++) {
            byte[] bytes = loader.getNext();
            for (int j = 0; j < loader.getPixelsPerFrame(); j++) {
                int red = (bytes[j] & 0xff);
                int green = (bytes[loader.getPixelsPerFrame() + j] & 0xff);
                int blue = (bytes[2 * loader.getPixelsPerFrame() + j] & 0xff);
                double colorValue = (double) ((red << 16) | (green << 8) | (blue));
                images.put(i, j, colorValue);
            }
            //Color dominant = ColorExtractor.getDominantColor(bytes, loader.getWidth() * loader.getHeight());
            //images.put(i,0, ((dominant.getRed() & 0xff) << 16) | ((dominant.getGreen() & 0xff) << 8) | ((dominant.getBlue() & 0xff)));
            //colors.add(((dominant.getRed() & 0xff) << 16) | ((dominant.getGreen() & 0xff) << 8) | ((dominant.getBlue() & 0xff)));
        }

        /*
        for (int i = 0; i < loader.getTotalFrames(); i++) {
            for (int j = 0; j < loader.getPixelsPerFrame(); j++) {
                System.out.println(Arrays.toString(images.get(i, j)));
            }
            System.out.println("Next Image...");
        }*/

        //images.convertTo(images, CvType.CV_32F);

        Mat labels = new Mat();
        Mat clusters = new Mat();
        Core.kmeans(images, BUCKETS, labels, new TermCriteria(TermCriteria.EPS + TermCriteria.COUNT, 1, 0.0001), 1, Core.KMEANS_PP_CENTERS, clusters);
        System.out.println("Clustering done");
        //set cluster metrics
        for(int i = 0; i < BUCKETS; i++){
            CLUSTERS[i].setMetric(clusters.get(i,0)[0]);
        }

        for (int i = 0; i < loader.getTotalFrames(); i++){
            int cluster_ID = (int)labels.get(i, 0)[0];
            Item item = new Item(i);
            item.setAveragColor((int)images.get(i, 0)[0]);
            CLUSTERS[cluster_ID].put(item);
        }

        System.out.println("Clusters generated...");

        dumpClusters();

        //train knn
        /*
        KNearest knn = KNearest.create();
        System.out.println(knn.isTrained());
        knn.train(images, Ml.ROW_SAMPLE, labels);
        System.out.println(knn.isTrained());
        System.out.println("Model Trained..");
        */
        /*
        //read image
        try {
            BufferedImage image = ImageIO.read(new File("/home/omi/Documents/sampleImage.jpg"));
            byte[] bytes = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.out.println(bytes.length);
            Mat img = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            img.put(0, 0, bytes);
            img.convertTo(img, CvType.CV_32F);

            Mat results = new Mat();
            knn.findNearest(img, 3, results);

        } catch (IOException e) {
            e.printStackTrace();
        }

        */
    }


    private void dumpClusters(){

        for(int i = 0; i < BUCKETS; i++){
            Cluster current = CLUSTERS[i];
            ArrayList<Item> clusterItems = current.getClusterItems();
            ArrayList<Integer> frames = new ArrayList<>();

            for(Item item: clusterItems){
                frames.add(item.getFrameNumber());
            }

            ImageLoader reader = new ImageLoader(INPUT_FILE);
            File clusterDIR = new File("/home/omi/Documents/Clusters/Cluster" + i);
            clusterDIR.mkdir();


            int lastRead = -1;
            byte[] bytes;
            BufferedImage img;
            for (int j = 0; j < reader.getTotalFrames(); j++){
                if(frames.contains(j)){
                    if (lastRead == -1){
                        bytes = reader.getNext();
                        lastRead = j;
                    }else{
                        bytes = reader.getNext((long)(j - 1 - lastRead) * reader.getBytesPerFrame());
                        lastRead = j;
                    }

                    img = new BufferedImage(reader.getWidth(), reader.getHeight(), BufferedImage.TYPE_INT_RGB);
                    int ind = 0;
                    for(int y = 0; y < reader.getHeight(); y++){

                        for(int x = 0; x < reader.getWidth(); x++){

                            byte a = 0;
                            byte r = bytes[ind];
                            byte g = bytes[ind+reader.getPixelsPerFrame()];
                            byte b = bytes[ind+reader.getPixelsPerFrame()*2];

                            int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                            //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                            img.setRGB(x,y,pix);
                            ind++;
                        }
                    }

                    try {
                        ImageIO.write(img, "jpg", new File("/home/omi/Documents/Clusters/Cluster" + i + "/IMG" + j + ".jpg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }

}
