import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class AudioSummarizing{


  private InputStream waveStream;
  private AudioInputStream audioInputStream;
  private AudioFormat audioFormat;
  private int mainThreshold = 1200;



  public void readAudio(String filename){

    try {
        waveStream = new FileInputStream(filename);
        audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(waveStream));
        audioFormat = audioInputStream.getFormat();
    } catch (UnsupportedAudioFileException e1) {
        e1.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e1) {
        e1.printStackTrace();
    }
  }

  public double meanSquare(double[] buffer){
    int ms = 0;
    for (int i = 0; i < buffer.length; i++)
           ms  += buffer[i];
     ms /= buffer.length;
     return ms;
  }

  public void printArray(double[] buffer){
    for (int i = 0; i < buffer.length; i++)
           System.out.print(buffer[i]+ " ");
  }


  public void calAudioIntensities(){

    double[] audioData = new double[24000*300];
    double[] meanSq = new double[600];

    for (int i = 0; i < 24000 * 300; i++) {
        byte[] b = new byte[2];
        try {
            if (audioInputStream.read(b, 0, 2) != -1) {
                short num = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
                audioData[i] = Math.abs(num);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     int audioIdx = 0;
     for(int time = 0 ; time < 600 ; time++){
          double[] buffer = new double[12000];

          for(int i = 0;i < 12000;i++){

            buffer[i] = audioData[audioIdx];
            audioIdx++;
          }

          meanSq[time] = meanSquare(buffer);

          //System.out.println(meanSq[time]+ " - "+ time);

     }
     int meanIdx = 0;
     Set secs = new TreeSet<Integer>();
     for(int time = 0 ; time<600; time+=20){


          double[] buffer = new double[20];
          for(int i = 0;i < 20;i++){

                buffer[i] = meanSq[meanIdx];
                meanIdx++;
         }

         printArray(buffer);

         double windowThreshold = meanSquare(buffer);

         System.out.println("Mean "+windowThreshold);

         for(int i = 0;i < 20;i++){

                if(buffer[i] >= mainThreshold && buffer[i] >= windowThreshold){
                              /*if((time+i-2) > 0){
                                secs.add(time+i-2);
                              }
                              if((time+i-1) > 0){
                                secs.add(time+i-1);
                              }
                              */
                              secs.add(time+i);
                              /*
                              if((time+i+1) < 600){
                                secs.add(time+i+1);
                              }
                              if((time+i+2) < 600){
                                secs.add(time+i+2);
                              }
                              */
                }
         }



     }

     System.out.println("Hash set values: "+ secs);

     System.out.println("Hash set size: "+ secs.size());

  }

  public static void main(String[] args){
    AudioSummarizing summ = new AudioSummarizing();
    summ.readAudio(args[0]);
    summ.calAudioIntensities();
  }
}
