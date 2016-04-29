package Cluster;

import java.util.ArrayList;

/**
 * Created by omi on 4/28/16.
 */
public class Cluster {
    private int CLUSTER_ID;
    private double CLUSTER_METRIC = -1.;
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

    public int getSize() {
        return this.CLUSTER_ITEMS.size();
    }

    public ArrayList<Item> getClusterItems() {
        return this.CLUSTER_ITEMS;
    }

    public void put(Item item){
        this.CLUSTER_ITEMS.add(item);
        updateMetric(item);
    }

    private void updateMetric(Item item){
        if (this.CLUSTER_METRIC == -1.){
            this.CLUSTER_METRIC = (double)item.getAVGColor();
        }else{
            this.CLUSTER_METRIC = (this.CLUSTER_METRIC + item.getAVGColor()) / 2.0;
        }
    }
}
