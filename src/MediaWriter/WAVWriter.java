package MediaWriter;

import MediaLoader.AudioLoader;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

/**
 * Created by omi on 4/27/16.
 */
public class WAVWriter {
    private AudioLoader loader;

    /*
    Sets the File name to be read
     */
    public WAVWriter(String fileName) {
        this.loader = new AudioLoader(fileName);
    }

    /*
    Writes the set bits of the audio file to and output.wav file
     */
    public void writeFrames(boolean[] frames, int setFrames, File outputFile) throws Exception {

        //byte array to store all audio data in the summarized wav
        byte[] audio = new byte[loader.getBytesPerFrame() * setFrames];
        int offset = 0;
        int lastFrameRead = 0;

        //read zeroth frame
        byte[] bytes = loader.getNext();
        System.arraycopy(bytes, 0, audio, offset, bytes.length);
        offset += bytes.length;
        //read remaining frames
        for (int i = 1; i < frames.length; i++) {
            if (frames[i]) {
                bytes = loader.getNext((long) ((i - 1 - lastFrameRead) * loader.getBytesPerFrame()));
                System.arraycopy(bytes, 0, audio, offset, bytes.length);
                offset += bytes.length;
                lastFrameRead = i;
            }
        }

        InputStream is = new ByteArrayInputStream(audio);
        AudioInputStream outStream = new AudioInputStream(is, loader.getAudioFormat(), audio.length);
        AudioSystem.write(outStream, AudioFileFormat.Type.WAVE, outputFile);
    }

    public void writeFrames(int startFrame, int endFrame, File outputFile) throws Exception {
        //byte array to store all audio data in the summarized wav
        byte[] audio = new byte[loader.getBytesPerFrame() * (endFrame - startFrame + 1)];
        int offset = 0;
        //skip begining frames
        loader.skip((long) (startFrame - 1) * loader.getBytesPerFrame());
        //read frames
        byte[] bytes;
        for (int i = startFrame; i <= endFrame; i++) {
            bytes = loader.getNext();
            System.arraycopy(bytes, 0, audio, offset, bytes.length);
            offset += bytes.length;
        }

        InputStream is = new ByteArrayInputStream(audio);
        AudioInputStream outStream = new AudioInputStream(is, loader.getAudioFormat(), audio.length);
        AudioSystem.write(outStream, AudioFileFormat.Type.WAVE, outputFile);
    }
}
