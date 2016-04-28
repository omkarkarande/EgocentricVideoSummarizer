package MediaWriter;

import MediaLoader.ImageLoader;
import java.io.*;

/**
 * Created by omi on 4/27/16.
 */
public class RGBWriter {

    /*
    Constructor
    Sets the file to be read
     */

    private ImageLoader loader;

    public RGBWriter(String fileName) {
        this.loader = new ImageLoader(fileName);
    }

    /*
    Writes the set frames to an output.rgb file
     */
    public void writeFrames(boolean[] frames, File outputFile) throws Exception {

        OutputStream outputStream = new FileOutputStream(outputFile);
        byte[] bytes;
        int lastFrameKept = 0;

        //get the first frame - index 0
        bytes = loader.getNext();
        outputStream.write(bytes);

        //get subsequent frames
        for (int i = 1; i < frames.length; i++) {
            if (frames[i]) {
                bytes = loader.getNext((long) (i - 1 - lastFrameKept) * loader.getBytesPerFrame());
                outputStream.write(bytes);
            }
        }
        outputStream.close();
    }
}
