package ImageProcessing;

import MediaLoader.ImageLoader;
import Cluster.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by omi on 4/28/16.
 */
public class ImageClustering {

    private ImageLoader loader;
    private int THRESHOLD = 50;
    private ArrayList<Cluster> clusters;

    public ImageClustering(String filename) {
        this.loader = new ImageLoader(filename);
        clusters = new ArrayList<>();
    }

    public void setThreshold(int threshold) {
        this.THRESHOLD = threshold;
    }

    public void clusterImages() {

        for (int i = 0; i < loader.getTotalFrames(); i++) {
            byte[] bytes = loader.getNext();
            int[] averages = getAverages(bytes);

            Item imageFrame = new Item(i);
            imageFrame.setAverages(averages[0], averages[1], averages[2]);
            System.out.println(Arrays.toString(averages));
        }

    }

    //return an int array of size 3
    private int[] getAverages(byte[] bytes) {
        int[] averages = new int[3];
        int frame_size = loader.getWidth() * loader.getHeight();

        //get averages
        for (int i = 0; i < 3; i++) {
            for (int j = i * frame_size; j < (i + 1) * frame_size; j++) {
                averages[i] += (int) bytes[j];
            }
            averages[i] /= frame_size;
        }

        return averages;
    }


}
