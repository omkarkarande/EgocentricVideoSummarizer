package MediaWriter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

/**
 * Created by omi on 4/27/16.
 */
public class WAVWriter {

    private static int SAMPLES_PER_FRAME = 1600;
    private static int BYTES_PER_SAMPLE = 2;
    private static int BYTES_PER_FRAME = SAMPLES_PER_FRAME * BYTES_PER_SAMPLE;

    private String INPUT_FILE_NAME;

    /*
    Sets the File name to be read
     */
    public WAVWriter(String fileName) {
        this.INPUT_FILE_NAME = fileName;
    }

    /*
    Writes the set bits of the audio file to and output.wav file
     */
    public void writeFrames(boolean[] frames, File outputFile) {
        //read Audio File
        try {
            InputStream waveStream = new FileInputStream(INPUT_FILE_NAME);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(waveStream));

            int frameCount = 0;
            for (boolean set : frames) {
                if (set) {
                    frameCount += 1;
                }
            }

            //handle zero case
            //bytes to store one frame worth audio data
            byte[] bytes = new byte[BYTES_PER_FRAME * frameCount];
            audioInputStream.read(bytes, 0, BYTES_PER_FRAME);
            int offset = 3200;
            int lastFrameRead = 0;

            for (int i = 1; i < frames.length; i++) {
                if (frames[i]) {
                    long toSkip = (i - 1 - lastFrameRead) * BYTES_PER_FRAME;
                    System.out.println(i + " - " + toSkip);
                    waveStream.skip(toSkip);
                    audioInputStream.read(bytes, offset, BYTES_PER_FRAME);
                    offset += BYTES_PER_FRAME;
                    lastFrameRead = i;
                }
            }

            InputStream is = new ByteArrayInputStream(bytes);
            AudioInputStream outStream = new AudioInputStream(is, audioInputStream.getFormat(), BYTES_PER_FRAME * frameCount);
            AudioSystem.write(outStream, AudioFileFormat.Type.WAVE, outputFile);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
