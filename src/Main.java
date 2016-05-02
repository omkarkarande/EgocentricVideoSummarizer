import Cluster.*;
import ImageProcessing.ImageClustering;
import ImageProcessing.RGBClustering;
import Player.AVPlayer;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

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
        try {
            ArrayList<Cluster> matched = clustering.getClosest(args[2], clusters, 3);
            System.out.println(matched.size());
            Item bestMatch = clustering.getMatchWithinClusters(matched);

            System.out.println("BEST MATCH: " + bestMatch.getFrameNumber());
            System.out.println("Seconds: " + bestMatch.getFrameNumber() / 15);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
