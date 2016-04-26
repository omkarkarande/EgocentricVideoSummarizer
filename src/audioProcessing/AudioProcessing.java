package audioProcessing;

import player.PlayWaveException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Created by sailesh on 4/26/16.
 */
public class AudioProcessing {

    private InputStream waveStream;
    private AudioInputStream audioInputStream;
    double[] audioData;
    private int VIDEO_FPS = 15;

    AudioProcessing(String fileName)
    {
        try {
            waveStream = new FileInputStream(fileName);
            audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(waveStream));
        }
        catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void processAudio() {

        byte[] b = new byte[2];

        int index = 0;

        ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
        wrapped.order(ByteOrder.LITTLE_ENDIAN);

        for(int frame = 0; frame<4500 ;frame++)
        {
            double[] audioData = new double[1600];
            for(int i=0; i<1600; i++) {
                try {
                    if(audioInputStream.read(b, 0, 2) != -1) {
                        double num = wrapped.getDouble();
                        audioData[i] = num;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }

    }
}
