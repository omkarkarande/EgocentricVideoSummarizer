package MediaWriter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

/**
 * Created by omi on 4/27/16.
 */
public class WAVWriter {
    private String INPUT_FILE_NAME;
    public WAVWriter(String fileName){
        this.INPUT_FILE_NAME = fileName;
    }

    public void writeFrames(boolean[] frames, File outputFile){
        //read Audio File
        try {
            InputStream waveStream = new FileInputStream(INPUT_FILE_NAME);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(waveStream));
            int audioFrameRate = (int)audioInputStream.getFormat().getFrameRate();
            int frameCount = 0;
            for(boolean set:frames){
                if(set){
                    frameCount += 1;
                }
            }
            System.out.println("TOTAL FRAMES: " + frameCount);
            //handle zero case
            //bytes to store one frame worth audio data
            byte[] bytes = new byte[3200 * frameCount];
            audioInputStream.read(bytes, 0, 3200);
            int offset = 3200;
            int lastFrameRead = 0;

            for (int i = 1; i < frames.length; i++){
                if (frames[i]){
                    long toSkip = (i - 1 - lastFrameRead) * 1600;
                    System.out.println(i + " - " + toSkip);
                    waveStream.skip(toSkip);
                    audioInputStream.read(bytes, offset, 3200);
                    offset += 3200;
                    lastFrameRead = i;
                }
            }

            InputStream is = new ByteArrayInputStream(bytes);
            AudioInputStream outStream = new AudioInputStream(is, audioInputStream.getFormat(), 3200 * frameCount);
            AudioSystem.write(outStream, AudioFileFormat.Type.WAVE, outputFile);

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
