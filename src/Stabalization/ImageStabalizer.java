package Stabalization;

import Configurations.Settings;
import MediaLoader.ImageLoader;
import Unused.Dumper;
import javafx.geometry.Point2D;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.calib3d.*;
import org.opencv.utils.Converters;
import org.opencv.video.Video;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by omi on 5/3/16.
 */
public class ImageStabalizer {
    ImageLoader loader;
    Mat rigidTransform;
    MatOfPoint2f trackedFeatures;
    Mat previousGrey;
    boolean freshStart = true;

    Dumper dumper = new Dumper();

    public ImageStabalizer(String RGBFile) {
        this.loader = new ImageLoader(RGBFile);
        rigidTransform = Mat.eye(3, 3, CvType.CV_32FC1);
        trackedFeatures = new MatOfPoint2f();
        previousGrey = new Mat();
    }

    private Mat bytesToMat(byte[] image) {
        Mat matImage = new Mat(Settings.HEIGHT, Settings.WIDTH, CvType.CV_8UC3);
        matImage.put(0, 0, image);
        return matImage;
    }


    private void processImage(Mat image) {

        //converting to greyscale
        Mat imageGrey = new Mat();
        Imgproc.cvtColor(image, imageGrey, Imgproc.COLOR_RGB2GRAY);

        //corner mat
        MatOfPoint corners = new MatOfPoint();

        if (trackedFeatures.rows() < 200) {

            Imgproc.goodFeaturesToTrack(imageGrey, corners, 300, 0.01, 30);
            for (int i = 0; i < corners.rows(); i++) {
                MatOfPoint2f corners2f = new MatOfPoint2f();
                corners.convertTo(corners2f, CvType.CV_32FC2);
                trackedFeatures.push_back(corners2f.row(i));
            }

            System.out.println(corners.size());
        }

        if (!previousGrey.empty()) {
            MatOfByte status = new MatOfByte();
            MatOfFloat errors = new MatOfFloat();
            MatOfPoint2f corners2f = new MatOfPoint2f();
            corners.convertTo(corners2f, CvType.CV_32FC2);

            Video.calcOpticalFlowPyrLK(previousGrey, imageGrey, trackedFeatures, corners2f, status, errors, new Size(10, 10), 4);

            if (Core.countNonZero(status) < status.rows() * 0.8) {
                rigidTransform = Mat.eye(3, 3, CvType.CV_32FC1);
                //possible change here
                trackedFeatures.release();
                previousGrey.release();
                freshStart = true;
                return;
            } else {
                freshStart = false;
            }


            Mat newRigidTransform = Video.estimateRigidTransform(trackedFeatures, corners2f, false);
            Mat nrt33 = MatOfFloat.eye(3, 3, CvType.CV_32F);
            newRigidTransform.copyTo(nrt33.rowRange(0, 2));
            rigidTransform.mul(nrt33);  //possible change
            trackedFeatures.release();

            for (int i = 0; i < status.rows(); i++) {
                //possible change
                if (status.get(i, 0)[0] != 0) {
                    trackedFeatures.push_back(corners2f.row(i));
                }
            }

            List<Point> points= new ArrayList<>();
            Converters.Mat_to_vector_Point(trackedFeatures,points);
            for (int i = 0; i < trackedFeatures.rows(); i++) {
                Imgproc.circle(image,points.get(i), 3, new Scalar(0, 0, 255), 1);
                byte[] buffer = new byte[Settings.BYTES_PER_FRAME];
                image.get(0, 0, buffer);
                dumper.dumpFrame(buffer, "/home/omi/Documents/Stabilized/dumped_" + i);
            }
        }

        imageGrey.copyTo(previousGrey);

    }

    public void stabalize() {
        //loop for all frames taking two frames at a time
        //Mat previousFrame = bytesToMat(loader.getNext());
        //Mat currentFrame;
        Mat destination = new Mat();
        for (int i = 1; i < loader.getTotalFrames(); i++) {

            //get current frame
            Mat currentFrame = bytesToMat(loader.getNext());
            processImage(currentFrame);

            Mat invTrans = rigidTransform.inv(Core.DECOMP_SVD);
            Imgproc.warpAffine(currentFrame, destination, invTrans.rowRange(0, 2), new Size());


        }
    }

















    /*
    public void stabalize() {

        Dumper dumper = new Dumper();

        Mat image1 = new Mat(loader.getHeight(), loader.getWidth(), CvType.CV_8UC3);
        Mat image2 = new Mat(loader.getHeight(), loader.getWidth(), CvType.CV_8UC3);
        image1.put(0, 0, loader.getNext());
        image2.put(0, 0, loader.getNext());


        Imgproc.cvtColor(image1, image1, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(image2, image2, Imgproc.COLOR_BGR2GRAY);

        FeatureDetector fd = FeatureDetector.create(FeatureDetector.ORB);

        MatOfKeyPoint mkp1 = new MatOfKeyPoint();
        fd.detect(image1, mkp1);
        MatOfKeyPoint mkp2 = new MatOfKeyPoint();
        fd.detect(image2, mkp2);

        DescriptorExtractor de = DescriptorExtractor.create(DescriptorExtractor.ORB);

        Mat desc1 = new Mat();
        de.compute(image1, mkp1, desc1);
        Mat desc2 = new Mat();
        de.compute(image2, mkp2, desc2);

        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(desc1, desc2, matches);

        List<DMatch> matched = matches.toList();
        double avg = 0.0;
        for (int i = 0; i < matched.size(); i++) {
            avg += matched.get(i).distance;
        }
        avg /= matched.size();


        List<KeyPoint> image1KeyPoints = mkp1.toList();
        List<KeyPoint> image2KeyPoints = mkp2.toList();

        List<Point> image1Points = new ArrayList<>();
        List<Point> image2Points = new ArrayList<>();

        for (int i = 0; i < matched.size(); i++) {
            DMatch match = matched.get(i);
            if (match.distance <= avg) {
                image1Points.add(image1KeyPoints.get(match.trainIdx).pt);
                image2Points.add(image2KeyPoints.get(match.queryIdx).pt);
            }
        }

            MatOfPoint2f im1pt = new MatOfPoint2f();
            im1pt.push_back(Converters.vector_Point2f_to_Mat(image1Points));
            MatOfPoint2f im2pt = new MatOfPoint2f();
            im2pt.push_back(Converters.vector_Point2f_to_Mat(image2Points));

            Mat homo = Calib3d.findHomography(im2pt, im1pt);
            System.out.println(homo.dump());
            //Mat perspectiveTransform = Imgproc.getPerspectiveTransform(im1pt, im2pt);

            //image2.convertTo(image2,CvType.CV_32F);
            Mat destination = new Mat();

            Imgproc.warpPerspective(image2, image2, homo, image2.size());
            Imgproc.cvtColor(image2, image2, Imgproc.COLOR_GRAY2RGB);
            byte[] buffer = new byte[Settings.BYTES_PER_FRAME];// / Settings.CHANNELS];
        image2.get(0, 0, buffer);
            dumper.dumpFrame(buffer, "/home/omi/Documents/dumped");

        }*/


}

