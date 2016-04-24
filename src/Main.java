import org.opencv.core.Core;
import player.*;

public class Main {
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	Player AVPlayer = new Player(args[0],args[1]);

	AVPlayer.loadAudio();
	AVPlayer.loadVideo();
	AVPlayer.initialize();
    new RGBLoader(args[0]);
    }

}
