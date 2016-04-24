import org.opencv.core.Core;

/**
 * Created by omi on 4/23/16.
 */
public class Main {
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new RGBLoader(args[0]);

    }
}
