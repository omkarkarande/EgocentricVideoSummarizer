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
        for(int i = 0; i < audioFrames.size(); i++){
            int timeStamp = audioFrames.get(i);
            framesToKeep[(int)Math.floor(timeStamp * 7.5)] = true;
        }

        int lastFrameKept = -1;
        ArrayList<Integer> framesKept = new ArrayList<>();
        for(int i = 0; i<framesToKeep.length; i++){
            if (framesToKeep[i]){
                if (lastFrameKept == -1){
                    lastFrameKept = i;
                    framesKept.add(i);
                }else{
                    if (i > lastFrameKept + 15 && !framesKept.contains(i)){
                        lastFrameKept = i;
                        framesKept.add(i);
                    }
                }
            }
        }

        System.out.println(framesKept);
        System.out.println(framesKept.size());
    }
}
