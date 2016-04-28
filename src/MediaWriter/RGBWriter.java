package MediaWriter;

import java.io.*;

/**
 * Created by omi on 4/27/16.
 */
public class RGBWriter {

    private String INPUT_FILE_NAME;
    private static int WIDTH = 480;
    private static int HEIGHT = 270;
    private static int CHANNELS = 3;
    private static int BYTES_PER_FRAME = WIDTH * HEIGHT * CHANNELS;

    /*
    Constructor
    Sets the file to be read
     */
    public RGBWriter(String fileName) {
        this.INPUT_FILE_NAME = fileName;
    }

    /*
    Writes the set frames to an output.rgb file
     */
    public void writeFrames(boolean[] frames, File outputFile) {
        File file = new File(INPUT_FILE_NAME);
        try {
            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(outputFile);

            byte[] bytes;
            int lastFrameKept = 0;

            //get the first frame - index 0
            bytes = new byte[(int) BYTES_PER_FRAME];
            int offset = 0;
            int numRead;

            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            os.write(bytes);

            //get subsequent frames
            for (int i = 1; i < frames.length; i++) {
                //if the frame is included in the summary
                if (frames[i]) {

                    bytes = new byte[(int) BYTES_PER_FRAME];
                    long toSkip = (i - 1 - lastFrameKept) * BYTES_PER_FRAME;
                    is.skip(toSkip);

                    offset = 0;
                    while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                        offset += numRead;
                    }
                    os.write(bytes);
                }
            }
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
