package Cluster;

import jdk.nashorn.internal.objects.annotations.ScriptClass;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omi on 4/28/16.
 */
public class Cluster {
    private int CLUSTER_ID;
    private double CLUSTER_METRIC = -1.0;
    private List<Mat> CLUSTER_HIST;
    private ArrayList<Item> CLUSTER_ITEMS;

    public Cluster(int ID) {
        this.CLUSTER_ID = ID;
        this.CLUSTER_ITEMS = new ArrayList<>();
    }

    public int getID() {
        return this.CLUSTER_ID;
    }

    public double getMetric() {
        return this.CLUSTER_METRIC;
    }

    public void setMetric(double metric) {
        this.CLUSTER_METRIC = metric;
    }

    public void setClusterHistogram(List<Mat> hist) {
        this.CLUSTER_HIST = hist;
    }

    public List<Mat> getClusterHistogram() {
        return this.CLUSTER_HIST;
    }

    public int getSize() {
        return this.CLUSTER_ITEMS.size();
    }

    public ArrayList<Item> getClusterItems() {
        return this.CLUSTER_ITEMS;
    }

    public void put(Item item) {
        this.CLUSTER_ITEMS.add(item);
        //updateMetric(item);
    }

    private void updateMetric(Item item) {
        if (this.CLUSTER_METRIC == -1.) {
            this.CLUSTER_METRIC = item.getAVGColor();
        } else {
            this.CLUSTER_METRIC = (this.CLUSTER_METRIC + item.getAVGColor()) / 2;
        }
    }

    public void calculateReferenceHistogram() {
        this.CLUSTER_HIST = new ArrayList<>();
        Mat r_hist = new Mat(256, 1, 5);
        Mat g_hist = new Mat(256, 1, 5);
        Mat b_hist = new Mat(256, 1, 5);

        for (int i = 0; i < this.CLUSTER_ITEMS.size(); i++) {
            List<Mat> itemHist = this.CLUSTER_ITEMS.get(i).getHistogram();
            Core.add(r_hist, itemHist.get(0), r_hist);
            Core.add(g_hist, itemHist.get(1), g_hist);
            Core.add(b_hist, itemHist.get(2), b_hist);
        }

        Scalar multiplier = new Scalar(1.0 / this.CLUSTER_ITEMS.size());
        Core.multiply(r_hist, multiplier, r_hist);
        Core.multiply(g_hist, multiplier, g_hist);
        Core.multiply(b_hist, multiplier, b_hist);

        this.CLUSTER_HIST.add(r_hist);
        this.CLUSTER_HIST.add(g_hist);
        this.CLUSTER_HIST.add(b_hist);

    }
}
