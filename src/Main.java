import Cluster.*;
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

        Summarizer summarizer = new Summarizer(args[0], args[1]);
        summarizer.summarize("/home/omi/Documents/summarized");

        AVPlayer player = new AVPlayer("/home/omi/Documents/summarized.rgb", "/home/omi/Documents/summarized.wav");
    }
}
