package Cluster;

/**
 * Created by omi on 4/28/16.
 */
public class Item {

    private int AVG_COLOR;
    private int FRAME_NUMBER;

    public Item(int frame){
        this.FRAME_NUMBER = frame;
    }

    public void setAveragColor(int avg_color) {
        this.AVG_COLOR = avg_color;
    }

    public int getAVGColor(){
        return this.AVG_COLOR;
    }
    public int getFrameNumber(){
        return this.FRAME_NUMBER;
    }
}
