package ImageProcessing;

import MediaLoader.ImageLoader;
import Cluster.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by omi on 5/1/16.
 */
public class RGBClustering {

    private static double MAX_DIFF = 259200;
    private static double THRESHOLD_PERCENT = 0.25;
    private static double CLUSTER_THRESHOLD = THRESHOLD_PERCENT * MAX_DIFF;
    private static int CHANNELS = 3;

    private Histogram hist;

    private ImageLoader loader;
    private ArrayList<Cluster> clusters;
    private Item queryImage;

    public RGBClustering(String fileName) {
        this.loader = new ImageLoader(fileName);
        this.clusters = new ArrayList<>();
        this.hist = new Histogram();
    }

    public ArrayList<Cluster> getClusters() {

        List<Mat> referenceHist = null;
        List<Mat> currentHist;


        int CLUSTER_ID = 0;
        Cluster cluster = new Cluster(CLUSTER_ID);

        for (int i = 0; i < loader.getTotalFrames(); i++) {
            byte[] bytes = loader.getNext();

            if (referenceHist == null) {
                referenceHist = hist.getHistogram(bytes, loader.getWidth(), loader.getHeight());

                Item item = new Item(i);
                item.setHistogram(referenceHist);
                cluster.put(item);
                //cluster.setClusterHistogram(referenceHist);

                System.out.println("Cluster: " + cluster.getID() + " --- " + item.getFrameNumber());
                continue;
            } else {
                currentHist = hist.getHistogram(bytes, loader.getWidth(), loader.getHeight());
            }

            // add frame as reference frame is the threshold is crossed
            if (hist.getDifference(currentHist, referenceHist) >= CLUSTER_THRESHOLD) {
                this.clusters.add(cluster);

                CLUSTER_ID += 1;
                referenceHist = currentHist;


                cluster = new Cluster(CLUSTER_ID);
                Item item = new Item(i);
                item.setHistogram(referenceHist);
                cluster.put(item);
                //cluster.setClusterHistogram(referenceHist);

                System.out.println("Cluster: " + cluster.getID() + " --- " + item.getFrameNumber());
            } else {
                Item item = new Item(i);
                item.setHistogram(currentHist);
                cluster.put(item);
            }

        }

        //get reference histograms for each cluster
        for (Cluster c: this.clusters){
            c.calculateReferenceHistogram();
        }


        return this.clusters;
    }

    public Cluster[] getClosest(String fileName, int K) throws Exception {

        Cluster[] closest = new Cluster[K];
        //BufferedImage image = ImageIO.read(new File(fileName));

        ImageLoader qLoader = new ImageLoader(fileName);
        qLoader.setWidth(1280);
        qLoader.setHeight(720);

        byte[] image = qLoader.getNext();

        BufferedImage img = new BufferedImage(qLoader.getWidth(), qLoader.getHeight(), BufferedImage.TYPE_INT_RGB);
        int ind = 0;
        for(int y = 0; y < qLoader.getHeight(); y++){

            for(int x = 0; x < qLoader.getWidth(); x++){

                byte a = 0;
                byte r = image[ind];
                byte g = image[ind+qLoader.getPixelsPerFrame()];
                byte b = image[ind+qLoader.getPixelsPerFrame()*2];

                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                img.setRGB(x,y,pix);
                ind++;
            }
        }

        ImageIO.write(img, "png", new File("/home/omi/Documents/sample.png"));

        List<Mat> imageHist = hist.getHistogram(image, 1280, 720);
        //SCALING
        imageHist = normalize(imageHist, 2.67, 2.67);

        //set item
        this.queryImage = new Item(-1);
        queryImage.setHistogram(imageHist);

        ArrayList<Double> differences = new ArrayList<>();
        for (int i = 0; i < this.clusters.size(); i++) {
            differences.add(hist.getDifference(imageHist, this.clusters.get(i).getClusterHistogram()));
        }

        //all clusters
        for (int i = 0; i < K; i++) {
            int minIndex = differences.indexOf(Collections.min(differences));
            closest[i] = this.clusters.get(minIndex);
            differences.set(minIndex, Double.MAX_VALUE);
        }

        return closest;
    }

    private List<Mat> normalize(List<Mat> histogram, double widthScaling, double heightScaling) {
        List<Mat> normalized = new ArrayList<>();
        Scalar multiplier = new Scalar(1.0 / (widthScaling * heightScaling));
        for (int i = 0; i < CHANNELS; i++) {
            Mat channelHist = histogram.get(i);
            Core.multiply(channelHist, multiplier, channelHist);
            normalized.add(channelHist);
        }

        return normalized;
    }

    public Item getMatchWithinClusters(Cluster[] clusters) {
        Item bestMatch = null;
        double difference = Double.MAX_VALUE;
        for (int i = 0; i < clusters.length; i++) {
            System.out.println("Cluster - " + clusters[i].getID() + " --- Reference Frame: " + clusters[i].getClusterItems().get(0).getFrameNumber());
            ArrayList<Item> items = clusters[i].getClusterItems();
            for (int j = 0; j < items.size(); j++) {
                double diff = hist.getDifference(items.get(j).getHistogram(), this.queryImage.getHistogram());
                if (diff < difference) {
                    difference = diff;
                    bestMatch = items.get(j);
                }
            }
        }
        return bestMatch;
    }

}
