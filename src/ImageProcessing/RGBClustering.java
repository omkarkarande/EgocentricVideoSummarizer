package ImageProcessing;

import MediaLoader.ImageLoader;
import Cluster.*;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by omi on 5/1/16.
 */
public class RGBClustering {

    private static double MAX_DIFF = 259200;
    private static double THRESHOLD_PERCENT = 0.25;
    private static double CLUSTER_THRESHOLD = THRESHOLD_PERCENT * MAX_DIFF;

    private Histogram hist;

    private ImageLoader loader;
    private ArrayList<Cluster> clusters;

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
                cluster.put(item);
                cluster.setClusterHistogram(referenceHist);
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
                cluster.put(item);
                cluster.setClusterHistogram(referenceHist);
            } else {
                Item item = new Item(i);
                cluster.put(item);
            }

        }

        return this.clusters;
    }

    public Cluster[] getClosest(String fileName, int K) throws Exception {

        Cluster[] closest = new Cluster[K];
        BufferedImage image = ImageIO.read(new File(fileName));

        List<Mat> imageHist = hist.getHistogram(image);
        ArrayList<Double> differences = new ArrayList<>();
        for (int i = 0; i < this.clusters.size(); i++) {
            differences.add(hist.getDifference(imageHist, this.clusters.get(i).getClusterHistogram()));
        }

        //all clusters
        for(int i = 0; i < this.clusters.size(); i++){
            System.out.println(this.clusters.get(i).getID() + " - " + differences.get(i));
        }
        for (int i = 0; i < K; i++){
            int minIndex = differences.indexOf(Collections.min(differences));
            closest[i] = this.clusters.get(minIndex);
            differences.remove(minIndex);
        }

        return closest;
    }
}
