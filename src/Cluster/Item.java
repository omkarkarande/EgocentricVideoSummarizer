package Cluster;

/**
 * Created by omi on 4/28/16.
 */
public class Item {
    private int AVG_RED;
    private int AVG_GREEN;
    private int AVG_BLUE;

    private int AVG_COLOR;

    private int FRAME_NUMBER;

    public Item(int frame){
        this.FRAME_NUMBER = frame;
    }

    public void setAverages(int red, int green, int blue){
        this.AVG_RED = red;
        this.AVG_GREEN = green;
        this.AVG_BLUE = blue;

        this.AVG_COLOR = 0xff000000 | ((this.AVG_RED & 0xff) << 16) | ((this.AVG_GREEN & 0xff) << 8) | (this.AVG_BLUE & 0xff);
    }

    public int getAVGColor(){
        return this.AVG_COLOR;
    }
    public int getFrameNumber(){
        return this.FRAME_NUMBER;
    }
}
