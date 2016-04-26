import AudioProcessing.AudioSampler;
import Player.AVPlayer;
import org.opencv.core.Core;

/**
 * Created by omi on 4/23/16.
 */
public class Main {
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //new RGBLoader(args[0]);
        //new AVPlayer(args[0], args[1]);
        new AudioSampler(args[1]);
    }
}
