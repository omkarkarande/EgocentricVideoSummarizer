import AudioProcessing.WAVSummarize;
import Configurations.Settings;
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

    public Summarizer(String imageFile, String audioFile) {
        this.IMAGE_FILE_NAME = imageFile;
        this.AUDIO_FILE_NAME = audioFile;
    }

    public void summarize(String outputFile) {
        boolean[] framesToKeep = new boolean[Settings.TOTAL_FRAMES];

        //call image summarizer
        RGBSummarize rgbSummarize = new RGBSummarize(IMAGE_FILE_NAME);
        ArrayList<Integer> imageFrames = rgbSummarize.processRGB();
        //set the frames to keep
        for (int frame : imageFrames) {
            framesToKeep[frame] = true;
        }


        //call audio summarizer
        WAVSummarize wavSummarize = new WAVSummarize(AUDIO_FILE_NAME);
        ArrayList<Integer> audioFrames = wavSummarize.processAudio();
        //set the frames to keep
        for (int i = 0; i < audioFrames.size(); i++) {
            int timeStamp = audioFrames.get(i);
            framesToKeep[(int) Math.floor(timeStamp * Settings.VIDEO_FRAMES_PER_STEP)] = true;
        }


        //Set all frames to keep
        int index = Settings.SUMMARY_FRAMES_TO_WRITE + 1;
        //get the first 15 frames by default
        for (int i = 1; i < index; i++) {
            framesToKeep[i] = true;
        }
        while (index < framesToKeep.length) {
            if (framesToKeep[index]) {
                for (int j = index - Settings.SUMMARY_FRAMES_TO_WRITE + 1; j >= 0 && j < framesToKeep.length && j <= index + Settings.SUMMARY_FRAMES_TO_WRITE; j++) {
                    framesToKeep[j] = true;
                }
                index += Settings.SUMMARY_FRAMES_TO_WRITE;
            }
            index += 1;
        }

        //count total frames set
        int totalFrames = 0;
        for (boolean set : framesToKeep) {
            if (set) {
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
