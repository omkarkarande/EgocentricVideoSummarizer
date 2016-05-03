
import org.opencv.core.Core;


/**
 * Created by omi on 4/23/16.
 */
public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        AVPlayer player = new AVPlayer(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));

    }
}
