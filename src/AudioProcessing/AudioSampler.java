package AudioProcessing;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by omi on 4/26/16.
 */
public class AudioSampler {

    private InputStream waveStream;
    private AudioInputStream audioInputStream;
    private AudioFormat audioFormat;
    public AudioSampler(String fileName) {
        try {
            waveStream = new FileInputStream(fileName);
            audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(waveStream));
            audioFormat = audioInputStream.getFormat();
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        processAudio();
    }

    public void processAudio() {

        FFT fft = new FFT();
        for (int frame = 0; frame < 4500; frame += 450) {

            double[] audioData = new double[1600 * 450 * 2];
            for (int i = 0; i < 1600 * 450; i++) {
                byte[] b = new byte[2];
                try {
                    if (audioInputStream.read(b, 0, 2) != -1) {
                        short num = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
                        audioData[2*i] = num;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(audioData[8] + " - " + audioData[100] + " - " + audioData[1600]);
            audioData = fft.calculateFFT(audioData);
            //udana

            for(int i = (1600 * 450 * 2) / 4; i < 1600 * 450 * 2; i++){
                audioData[i] = 0.0;
            }

            audioData = fft.calculateIFFT(audioData);
            System.out.println(audioData[8] + " - " + audioData[100] + " - " + audioData[1600]);

            byte[] bytes = new byte[1600 * 450 * 2];
            for(int i = 0; i < 1600 * 450; i++){
                short val = (short)Math.floor(audioData[2 * i]);
                bytes[2 * i]  = (byte) val;
                bytes[2 * i + 1] = (byte) ((val >> 8) & 0xff);
            }

            InputStream is = new ByteArrayInputStream(bytes);
            AudioInputStream outStream = new AudioInputStream(is, audioFormat , 1600 * 450 * 2);
            System.out.println(outStream.getFormat().toString());
            try {
                AudioSystem.write(outStream, AudioFileFormat.Type.WAVE, new File("/home/omi/Documents/out.wav"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Completed");
            break;
        }

    }
}
