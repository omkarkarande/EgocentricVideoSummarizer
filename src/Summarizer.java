import AudioProcessing.WAVSummarize;
import ImageProcessing.RGBSummarize;
import MediaWriter.RGBWriter;
import MediaWriter.WAVWriter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by omi on 4/27/16.
 */
public class Summarizer {

    private String IMAGE_FILE_NAME;
    private String AUDIO_FILE_NAME;
    private static int TOTAL_FRAMES = 4500;
    private static int FRAME_WINDOW = 15;

    public Summarizer(String imageFile, String audioFile) {
        this.IMAGE_FILE_NAME = imageFile;
        this.AUDIO_FILE_NAME = audioFile;
    }

    public void summarize(String outputFile) {
        boolean[] framesToKeep = new boolean[TOTAL_FRAMES];

        //call image summarizer
        RGBSummarize rgbSummarize = new RGBSummarize(IMAGE_FILE_NAME);
        ArrayList<Integer> imageFrames = rgbSummarize.processRGB();
        //set the frames to keep
        for (int frame : imageFrames) {
            framesToKeep[frame] = true;
        }


        //call audio summarizer
        WAVSummarize wavSummarize = new WAVSummarize(AUDIO_FILE_NAME);
        ArrayList<Integer> audioFrames = wavSummarize.getTimeStamps();
        //set the frames to keep
        for (int i = 0; i < audioFrames.size(); i++) {
            int timeStamp = audioFrames.get(i);
            framesToKeep[(int) Math.floor(timeStamp * (float) FRAME_WINDOW / 2.0)] = true;
        }


        //Set all frames to keep
        int index = FRAME_WINDOW + 1;
        //get the first 15 frames by default
        for (int i = 1; i < index; i++) {
            framesToKeep[i] = true;
        }
        while (index < framesToKeep.length) {
            if (framesToKeep[index]) {
                for (int j = index - FRAME_WINDOW + 1; j >= 0 && j < framesToKeep.length && j <= index + FRAME_WINDOW; j++) {
                    framesToKeep[j] = true;
                }
                index += FRAME_WINDOW;
            }
            index += 1;
        }

        //count total frames set
        int totalFrames = 0;
        for(boolean set:framesToKeep){
            if(set){
                totalFrames += 1;
            }
        }

        //Write the files out
        RGBWriter imageWriter = new RGBWriter(IMAGE_FILE_NAME);
        try {
            imageWriter.writeFrames(framesToKeep, new File(outputFile + ".rgb"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        WAVWriter audioWriter = new WAVWriter(AUDIO_FILE_NAME);
        try {
            audioWriter.writeFrames(framesToKeep, totalFrames, new File(outputFile + ".wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
