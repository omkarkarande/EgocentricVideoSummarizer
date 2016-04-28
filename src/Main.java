import org.opencv.core.Core;
import player.*;
import audioProcessing.*;
import imageProcessing.*;

public class Main {
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	Player AVPlayer = new Player(args[0],args[1]);

	AVPlayer.loadAudio();
	AVPlayer.loadVideo();
	AVPlayer.initialize();


	Summarizer summ = new Summarizer(args[0],args[1]);
		summ.summarize("asd");
	
    }

}
