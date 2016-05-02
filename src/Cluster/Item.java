package Cluster;

import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by omi on 4/28/16.
 */
public class Item {

    private int AVG_COLOR;
    private int FRAME_NUMBER;
    private List<Mat> HISTOGRAM;

    public Item(int frame) {
        this.FRAME_NUMBER = frame;
    }

    public void setAveragColor(int avg_color) {
        this.AVG_COLOR = avg_color;
    }

    public void setHistogram(List<Mat> histogram) {
        this.HISTOGRAM = histogram;
    }

    public List<Mat> getHistogram() {
        return this.HISTOGRAM;
    }

    public int getAVGColor() {
        return this.AVG_COLOR;
    }

    public int getFrameNumber() {
        return this.FRAME_NUMBER;
    }
}
