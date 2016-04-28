package AudioProcessing;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class WAVSummarize {


    private static InputStream waveStream;
    private static AudioInputStream audioInputStream;
    private static AudioFormat audioFormat;
    private static int mainThreshold = 1200;
    private static int audioFrameRate;
    private static int audioLengthInSeconds;

    public WAVSummarize(String filename) {
        //TODO set values from external file
        try {
            this.waveStream = new FileInputStream(filename);
            this.audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(waveStream));
            this.audioFormat = audioInputStream.getFormat();
            this.audioFrameRate = (int) audioFormat.getFrameRate();
            this.audioLengthInSeconds = (int) ((float) audioInputStream.getFrameLength() / (float) this.audioFrameRate);

        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public ArrayList<Integer> getTimeStamps() {
        try{
            return calAudioIntensities();
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    private double meanSquare(double[] buffer) {
        int ms = 0;
        for (int i = 0; i < buffer.length; i++)
            ms += buffer[i];
        ms /= buffer.length;
        return ms;
    }

    private ArrayList<Integer> calAudioIntensities() throws Exception {

        int totalFrames = this.audioFrameRate * this.audioLengthInSeconds;
        int sampleSize = this.audioLengthInSeconds * 2;

        double[] audioData = new double[totalFrames];
        double[] meanSq = new double[sampleSize];

        ArrayList<Integer> secs = new ArrayList<>();


        //read the wav file into a double array byte by byte.
        byte[] b;
        for (int i = 0; i < totalFrames; i++) {
            //2 bytes for each sample of 16 bit.
            b = new byte[2];
            if (audioInputStream.read(b, 0, 2) != -1) {
                //re-encoding endianness
                short num = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
                audioData[i] = Math.abs(num);
            }

        }

        int audioIdx = 0;
        double[] buffer;
        for (int time = 0; time < sampleSize; time++) {
            buffer = new double[12000];
            for (int i = 0; i < 12000; i++) {

                buffer[i] = audioData[audioIdx];
                audioIdx++;
            }
            meanSq[time] = meanSquare(buffer);
        }

        int meanIdx = 0;
        for (int time = 0; time < 600; time += 20) {
            buffer = new double[20];
            for (int i = 0; i < 20; i++) {
                buffer[i] = meanSq[meanIdx];
                meanIdx++;
            }
            double windowThreshold = meanSquare(buffer);
            for (int i = 0; i < 20; i++) {
                if (buffer[i] >= mainThreshold && buffer[i] >= windowThreshold) {
                    secs.add(time + i);
                }
            }
        }
        return secs;
    }

}
