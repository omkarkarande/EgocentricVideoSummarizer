
import Stabalization.ImageStabalizer;
import Unused.Dumper;
import org.opencv.core.Core;


/**
 * Created by omi on 4/23/16.
 */
public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //Dumper dumper = new Dumper(args[0]);
        //dumper.dumpAll("/home/omi/Documents/Frames/");
        AVPlayer player = new AVPlayer(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));

        //ImageStabalizer stabalizer = new ImageStabalizer(args[0]);
        //stabalizer.stabalize();

    }
}
