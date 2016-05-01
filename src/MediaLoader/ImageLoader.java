package MediaLoader;


import java.io.*;

/**
 * Created by omi on 4/28/16.
 */
public class ImageLoader {

    private File IMAGE_FILE;
    private int TOTAL_FRAMES;
    private InputStream INPUT;

    private static int WIDTH = 480;
    private static int HEIGHT = 270;
    private static int CHANNELS = 3;
    private static int PIXELS_PER_FRAME = WIDTH * HEIGHT;
    private static int BYTES_PER_FRAME = WIDTH * HEIGHT * CHANNELS;
    public ImageLoader(String filename) {
        //open the file for reading
        this.IMAGE_FILE = new File(filename);
        this.TOTAL_FRAMES = (int) IMAGE_FILE.length() / BYTES_PER_FRAME;
        try {
            INPUT = new FileInputStream(this.IMAGE_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public byte[] getNext(long toSkip) {
        try {
            this.INPUT.skip(toSkip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getNext();
    }

    public byte[] getNext() {

        byte[] bytes = new byte[(int) BYTES_PER_FRAME];

        int offset = 0;
        int numRead;

        try {
            while (offset < bytes.length && (numRead = this.INPUT.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public int getTotalFrames() {
        return this.TOTAL_FRAMES;
    }

    public int getBytesPerFrame() {
        return this.BYTES_PER_FRAME;
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public int getHeight() {
        return this.HEIGHT;
    }

    public int getChanels() {
        return this.CHANNELS;
    }

    public int getPixelsPerFrame(){
        return this.PIXELS_PER_FRAME;
    }
}
