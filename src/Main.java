import Cluster.*;
import ImageProcessing.ImageClustering;
import ImageProcessing.RGBClustering;
import Player.AVPlayer;
import org.opencv.core.Core;

import java.util.ArrayList;

/**
 * Created by omi on 4/23/16.
 */
public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //Summarizer summarizer = new Summarizer(args[0], args[1]);
        //summarizer.summarize("/home/omi/Documents/summarized");
        //ImageClustering ic = new ImageClustering(args[0]);
        //ic.clusterImages();

        RGBClustering clustering = new RGBClustering(args[0]);
        ArrayList<Cluster> clusters = clustering.getClusters();
        /*
        for (int i = 0; i < clusters.size(); i++) {
            Cluster current = clusters.get(i);
            System.out.println(current.getID() + " - " + current.getSize());
        }*/

        try {
            Cluster[] matched = clustering.getClosest(args[2], 3);
            for (int i = 0; i < matched.length; i++) {
                System.out.println("\nMATCHED CLUSTER: " + matched[i].getID());
                System.out.println(matched[i].getClusterItems().get(0).getFrameNumber());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
