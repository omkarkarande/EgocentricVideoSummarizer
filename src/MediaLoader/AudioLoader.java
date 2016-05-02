package MediaLoader;

import Configurations.Settings;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

/**
 * Created by omi on 4/28/16.
 */
public class AudioLoader {

    private InputStream waveStream;
    private AudioInputStream audioInputStream;

    public AudioLoader(String fileName) {
        try {
            this.waveStream = new FileInputStream(fileName);
            this.audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(this.waveStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void skip(long toSkip) {
        try {
            this.waveStream.skip(toSkip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getNext(long toSkip) {
        try {
            this.waveStream.skip(toSkip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getNext();
    }


    //returns the next frame
    public byte[] getNext() {
        byte[] bytes = new byte[Settings.AUDIO_BYTES_PER_VIDEO_FRAME];
        try {
            this.audioInputStream.read(bytes, 0, Settings.AUDIO_BYTES_PER_VIDEO_FRAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public int getBytesPerFrame() {
        return Settings.AUDIO_BYTES_PER_VIDEO_FRAME;
    }

    public AudioFormat getAudioFormat() {
        return this.audioInputStream.getFormat();
    }
}
