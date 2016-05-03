package videoSearch;

import imageProcessing.ImageProcessing;
import org.opencv.core.CvType;
import org.opencv.core.*;
import org.opencv.ml.*;
import org.opencv.ml.KNearest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

/**
 * Created by sailesh on 4/30/16.
 */

public class VideoSearch {

    private int WIDTH = 480;
    private int HEIGHT = 270;
    private int PCA_DIMENTION = 20;
    private int CLUSTERS;
    private int ATTEMPTS = 5;
    Mat video;
    Mat queryImgMat;
    public VideoSearch(String fileName, String queryImg) {
        File file = new File(fileName);
        File imgFile = new File(queryImg);
        CLUSTERS = ImageProcessing.keyFrameSize *2;
        System.out.println("CLUSTERS : " + CLUSTERS);
        int totalFrames = 4500;//(int)file.length() / (480 * 270 * 3);
        video = new Mat(totalFrames,480*270,CvType.CV_8SC1);
        queryImgMat = new Mat(1,480*270,CvType.CV_8SC1);

        try {
            InputStream is = new FileInputStream(file);
            //InputStream img = new FileInputStream(imgFile);
            BufferedImage img = ImageIO.read(imgFile);

            byte[] imgBytes;
            imgBytes = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
            //System.out.println("imgBytes: " + imgBytes.length);
            for(int i = 0; i <totalFrames; i++) {
                long len = WIDTH * HEIGHT * 3;
                byte[] bytes = new byte[(int) len];

                int offset = 0;
                int numRead;

                if(i ==0) {
                    /*imgBytes = new byte[1280*720*3];
                    while (offset < imgBytes.length && (numRead = is.read(imgBytes, offset, imgBytes.length - offset)) >= 0) {
                        offset += numRead;
                    }*/
                    queryImgMat.put(0,0,imgBytes);
                    queryImgMat.convertTo(queryImgMat,CvType.CV_32F);
                }

                while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }

                video.put(i, 0, bytes);
            }

            System.out.println("video size : " + video.size());
            System.out.println("queryImg size : " + queryImgMat.size());

            Mat labels = new Mat();
            Mat centers = new Mat();
            video.convertTo(video,CvType.CV_32F);
            Core.kmeans(video,CLUSTERS,labels,new TermCriteria(TermCriteria.EPS + TermCriteria.COUNT, ATTEMPTS, 0.0001), ATTEMPTS, Core.KMEANS_PP_CENTERS,centers);

            System.out.println("kmeans done");
            Vector<Integer> index= new Vector<Integer>();
            Mat sortedFrames = new Mat();
            Mat sortedLabels = new Mat();


            Mat results = new Mat();
            Mat neigh = new Mat();
            Mat dist = new Mat();

            //System.out.println("**********");
            //System.out.println("***************queryImg :" + queryImgMat.dump());
            KNearest knn = KNearest.create();
            //knn.findNearest(testFeature,2,results,neigh,dist);
            //knn.findNearest(queryImgMat,5,results);
            knn.train(video,Ml.ROW_SAMPLE,labels);
            System.out.println("Trained : " + knn.isTrained());
            knn.findNearest(queryImgMat,10,results,neigh,dist);

            System.out.println("***************results :" + results.dump());
            System.out.println("***************neigh :" + neigh.dump());
            System.out.println("***************dist :" + dist.dump());
            double temp[] = results.get(0,0);

            int count = 0;
            for(int i = 0;i <4500; i++)
            {
                double temp1[] = labels.get(i,0);
                if(temp[0] == temp1[0])
                {
                    count ++;
                    System.out.println("Label : "+ temp1[0] + " --- frame no: " + i);//index.elementAt(i));
                }
            }
            System.out.println("Count: " + count);

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
