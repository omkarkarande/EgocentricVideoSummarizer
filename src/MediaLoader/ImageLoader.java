package MediaLoader;


import Configurations.Settings;

import java.io.*;

/**
 * Created by omi on 4/28/16.
 */
public class ImageLoader {

    private File IMAGE_FILE;
    private int TOTAL_FRAMES;
    private InputStream INPUT;

    public ImageLoader(String filename) {
        //open the file for reading
        this.IMAGE_FILE = new File(filename);
        this.TOTAL_FRAMES = (int) IMAGE_FILE.length() / Settings.BYTES_PER_FRAME;
        try {
            INPUT = new FileInputStream(this.IMAGE_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //skip the stream ahead
    public void skip(long toSkip) {
        try {
            this.INPUT.skip(toSkip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //skip the stream ahead and return the next frame
    public byte[] getNext(long toSkip) {
        try {
            this.INPUT.skip(toSkip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getNext();
    }

    //retur the next frame
    public byte[] getNext() {

        byte[] bytes = new byte[Settings.BYTES_PER_FRAME];

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

    //return the next frame with custom width and height
    public byte[] getNext(int width, int height, int channels) {

        byte[] bytes = new byte[width * height * channels];

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
        return Settings.BYTES_PER_FRAME;
    }

    public int getWidth() {
        return Settings.WIDTH;
    }

    public int getHeight() {
        return Settings.HEIGHT;
    }

    public int getChanels() {
        return Settings.CHANNELS;
    }

    public int getPixelsPerFrame() {
        return Settings.PIXELS_PER_FRAME;
    }
}
