package MediaLoader;

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

    private static int SAMPLES_PER_FRAME = 1600;
    private static int BYTES_PER_SAMPLE = 2;
    private static int BYTES_PER_FRAME = SAMPLES_PER_FRAME * BYTES_PER_SAMPLE;


    public AudioLoader(String fileName){
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

    public byte[] getNext(long toSkip){
        try{
            this.waveStream.skip(toSkip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getNext();
    }

    public byte[] getNext(){
        byte[] bytes = new byte[this.BYTES_PER_FRAME];
        try{
            this.audioInputStream.read(bytes, 0, BYTES_PER_FRAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public int getBytesPerFrame(){
        return this.BYTES_PER_FRAME;
    }

    public AudioFormat getAudioFormat(){
        return this.audioInputStream.getFormat();
    }
}
