package Player;

import java.io.*;
import javax.sound.sampled.*;

/**
 * Created by omi on 4/24/16.
 */
public class AudioPlayer {
    //PRIVATE VARIABLES
    private InputStream waveStream;
    private AudioInputStream audioInputStream;
    private Clip audioClip;
    private int currentFrame = 0;
    private long currentTime = 0;
    private AudioFormat audioFormat;
    private double audioFrameRate;
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

    public AudioPlayer(InputStream waveStream) throws Exception {
        this.waveStream = new BufferedInputStream(waveStream);
        loadAudio();
    }


    private void loadAudio() throws Exception {

        audioInputStream = null;
        audioInputStream = AudioSystem.getAudioInputStream(this.waveStream);
        audioClip = AudioSystem.getClip();
        audioFormat = audioClip.getFormat();
        audioFrameRate = audioFormat.getFrameRate();
    }

    public void play() throws Exception {
        audioClip.setMicrosecondPosition(currentTime);
        audioClip.setFramePosition(currentFrame);
        audioClip.start();
    }


    public void pause(){
        audioClip.stop();
        this.currentFrame = audioClip.getFramePosition();
        this.currentTime = audioClip.getMicrosecondPosition();
    }

    public void stop(){
        audioClip.stop();
        this.currentTime = 0;
        this.currentFrame = 0;
    }

    public long getLength(){
        return audioClip.getMicrosecondLength();
    }

    public long getPosition(){
        return audioClip.getMicrosecondPosition();
    }
}
