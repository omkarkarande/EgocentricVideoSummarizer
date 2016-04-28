import audioProcessing.*;
import imageProcessing.*;

import java.util.ArrayList;


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
        ImageProcessing rgbSummarize = new ImageProcessing(IMAGE_FILE_NAME,4500,480,270);
        ArrayList<Integer> imageFrames = rgbSummarize.generateKeyFrames();
        System.out.println("Image Frames Length: " + imageFrames.size());
        for(int frame: imageFrames){
            framesToKeep[frame] = true;
        }
        //call audio summarizer
        AudioProcessing wavSummarize = new AudioProcessing(AUDIO_FILE_NAME);
        ArrayList<Integer> audioFrames = wavSummarize.processAudio();
        System.out.println("Audio Frames Length: " + audioFrames.size());
        for(int i = 0; i < audioFrames.size(); i++){
            int timeStamp = audioFrames.get(i);
            framesToKeep[(int)Math.floor(timeStamp * 7.5)] = true;
        }

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

    }
}
