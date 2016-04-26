package AudioProcessing;

import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.utils.IOUtils;

import java.util.Arrays;

/**
 * Created by omi on 4/26/16.
 */
public class FFT {
    public FFT(){

    }

    public double[] calculateFFT(double[] sample){

        DoubleFFT_1D fft = new DoubleFFT_1D(sample.length / 2);
        fft.complexForward(sample);
        return sample;
    }

    public double[] calculateIFFT(double[] fft){
        DoubleFFT_1D ifft = new DoubleFFT_1D(fft.length / 2);
        ifft.complexInverse(fft, true);
        return fft;
    }
}
