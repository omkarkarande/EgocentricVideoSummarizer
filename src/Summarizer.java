import AudioProcessing.WAVSummarize;
import AudioProcessing.WAVSummarizeSailesh;
import ImageProcessing.RGBSummarize;

import java.util.ArrayList;

/**
 * Created by omi on 4/27/16.
 */
public class Summarizer {

    private String IMAGE_FILE_NAME;
    private String AUDIO_FILE_NAME;

    private String OUTPUT_FILE_NAME;

    public Summarizer(String imageFile, String audioFile){
        this.IMAGE_FILE_NAME = imageFile;
        this.AUDIO_FILE_NAME = audioFile;
    }

    public void summarize(String outputFile){
        this.OUTPUT_FILE_NAME = outputFile;
        boolean[] framesToKeep = new boolean[4500];

        //call image summarizer
        RGBSummarize rgbSummarize = new RGBSummarize(IMAGE_FILE_NAME);
        ArrayList<Integer> imageFrames = rgbSummarize.processRGB();
        System.out.println("Image Frames Length: " + imageFrames.size());
        for(int frame: imageFrames){
            framesToKeep[frame] = true;
        }
        //call audio summarizer
        WAVSummarizeSailesh wavSummarize = new WAVSummarizeSailesh(AUDIO_FILE_NAME);
        ArrayList<Integer> audioFrames = wavSummarize.getTimeStamps();
        System.out.println("Audio Frames Length: " + audioFrames.size());
        System.out.println(audioFrames);
        for(int i = 0; i < audioFrames.size(); i++){
            int timeStamp = audioFrames.get(i);
            framesToKeep[(int)Math.floor(timeStamp * 7.5)] = true;
        }

        //Set all frames to keep
        int index = 0;
        while(index < framesToKeep.length){
            if (framesToKeep[index]){
                for(int j = index - 14; j >= 0 && j<framesToKeep.length && j<=index + 15; j++){
                    framesToKeep[j] = true;
                }
                index += 16;
            }else{
                index += 1;
            }
        }

        int count = 0;
        for(boolean set:framesToKeep){
            if(set){
                count++;
            }
        }
        System.out.println(count);

    }
}
