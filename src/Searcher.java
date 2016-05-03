import Cluster.*;
import Configurations.Settings;
import ImageProcessing.RGBClustering;
import MediaWriter.RGBWriter;
import MediaWriter.WAVWriter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by omi on 5/2/16.
 */
public class Searcher {
    RGBClustering clustering;

    public Searcher(String RGBFile) {
        clustering = new RGBClustering(RGBFile);
    }

    public int search(String queryImageFile, int imageWidth, int imageHeight) throws Exception {
        ArrayList<Cluster> clusters = clustering.getClusters();
        ArrayList<Cluster> closest = clustering.getClosest(queryImageFile, imageWidth, imageHeight, clusters);
        Item bestMatch = clustering.getMatchWithinClusters(closest);
        if (bestMatch == null) {
            return -1;
        }
        return bestMatch.getFrameNumber();
    }


    //dump video and audio
    public void dumpSnippet(int frameNumber, String RGBFile, String WAVFile, String outputFile) throws Exception {
        int startFrame = (frameNumber - Settings.SEARCH_FRAMES_TO_WRITE) < 0 ? 0 : (frameNumber - Settings.SEARCH_FRAMES_TO_WRITE);
        int endFrame = (frameNumber + Settings.SEARCH_FRAMES_TO_WRITE) >= Settings.TOTAL_FRAMES ? Settings.TOTAL_FRAMES : (frameNumber + Settings.SEARCH_FRAMES_TO_WRITE);

        //write RGB file
        RGBWriter imageWriter = new RGBWriter(RGBFile);
        imageWriter.writeFrames(startFrame, endFrame, new File(outputFile + ".rgb"));

        //write WAV file
        WAVWriter audioWriter = new WAVWriter(WAVFile);
        audioWriter.writeFrames(startFrame, endFrame, new File(outputFile + ".wav"));
    }
}
