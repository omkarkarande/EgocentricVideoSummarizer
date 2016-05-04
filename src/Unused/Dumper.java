package Unused;

import Configurations.Settings;
import MediaLoader.ImageLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by omi on 5/3/16.
 */
public class Dumper {
    ImageLoader loader;

    public Dumper(String fileName) {
        this.loader = new ImageLoader(fileName);
    }

    public Dumper() {

    }

    public void dumpAll(String path) {
        BufferedImage img;
        for (int i = 0; i < loader.getTotalFrames(); i++) {
            img = new BufferedImage(loader.getWidth(), loader.getHeight(), BufferedImage.TYPE_INT_RGB);
            byte[] bytes = loader.getNext();
            int ind = 0;
            for (int y = 0; y < loader.getHeight(); y++) {

                for (int x = 0; x < loader.getWidth(); x++) {

                    byte a = 0;
                    byte r = bytes[ind];
                    byte g = bytes[ind + loader.getPixelsPerFrame()];
                    byte b = bytes[ind + loader.getPixelsPerFrame() * 2];

                    int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                    //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                    img.setRGB(x, y, pix);
                    ind++;
                }
            }

            try {
                ImageIO.write(img, "jpg", new File(path + "image_" + i + ".jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dumpFrame(byte[] bytes, String filename) {
        BufferedImage img = new BufferedImage(Settings.WIDTH, Settings.HEIGHT, BufferedImage.TYPE_INT_RGB);
        int ind = 0;
        for (int y = 0; y < Settings.HEIGHT; y++) {

            for (int x = 0; x < Settings.WIDTH; x++) {

                byte a = 0;
                byte r = bytes[ind];
                byte g = bytes[ind + Settings.PIXELS_PER_FRAME];
                byte b = bytes[ind + Settings.PIXELS_PER_FRAME * 2];

                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                img.setRGB(x, y, pix);
                ind++;
            }
        }

        try {
            ImageIO.write(img, "jpg", new File(filename + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dumpFrameGrayscale(byte[] bytes, String filename) {
        BufferedImage img = new BufferedImage(Settings.WIDTH, Settings.HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < Settings.HEIGHT; y++) {
            for (int x = 0; x < Settings.WIDTH; x++) {

            }
        }

        try {
            ImageIO.write(img, "jpg", new File(filename + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
