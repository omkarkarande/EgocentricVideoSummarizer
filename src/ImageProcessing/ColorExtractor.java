package ImageProcessing;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by omi on 4/28/16.
 */
public class ColorExtractor {

    private static int HUE_RANGE = 360;

    public static Color getDominantColor(byte[] image, int pixels_per_frame){
        //loop over all pixels
        /*
        int[] hues = new int[HUE_RANGE];
        float[] saturations = new float[HUE_RANGE];
        float[] brightnesses = new float[HUE_RANGE];
        */

        float r = 0f;
        float g = 0f;
        float b = 0f;

        for (int i = 0; i < pixels_per_frame; i++){
            int red = (image[i] & 0xff);
            int green = (image[pixels_per_frame + i] & 0xff);
            int blue = (image[2*pixels_per_frame + i] & 0xff);

            /*
            float[] hsb = new float[3];
            Color.RGBtoHSB(red, green, blue, hsb);
            int hue = (int)Math.floor(hsb[0] * HUE_RANGE);

            hues[hue]++;
            saturations[hue] += hsb[1];
            brightnesses[hue] += hsb[2];*/

            r += red;
            g += green;
            b += blue;
        }

        /*
        //find the most common hue
        int hueCount = hues[0];
        int hue = 0;
        for(int i = 1; i < hues.length; i++){
            if(hues[i] > hueCount){
                hue = i;
                hueCount = hues[i];
            }
        }

        //set corrosponding saturation
        float saturation = saturations[hue] / hueCount;
        float brightness = brightnesses[hue] / hueCount;
        return new Color(Color.HSBtoRGB(((float)hue / HUE_RANGE), saturation, brightness));
        */

        r = r / pixels_per_frame;
        g = g / pixels_per_frame;
        b = b / pixels_per_frame;
        return new Color((int)r, (int)g, (int)b);
    }
}
