package MediaWriter;

import java.io.*;

/**
 * Created by omi on 4/27/16.
 */
public class RGBWriter {

    private String INPUT_FILE_NAME;
    private int WIDTH;
    private int HEIGHT;
    public RGBWriter(String fileName){
        this.INPUT_FILE_NAME = fileName;
    }

    public void writeFrames(boolean[] frames, File outputFile){
        this.WIDTH = 480;
        this.HEIGHT = 270;

        File file = new File(INPUT_FILE_NAME);
        int totalFrames = (int)file.length() / (480 * 270 * 3);

        try {
            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(outputFile);
            for(int i = 0; i < frames.length; i++){

                long len = WIDTH*HEIGHT*3;
                byte[] bytes = new byte[(int)len];

                int offset = 0;
                int numRead;
                while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                    offset += numRead;
                }
                if (frames[i]){
                    os.write(bytes);
                }
            }
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}