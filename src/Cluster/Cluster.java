package Cluster;

import org.opencv.core.Mat;

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
}
