package dnv.ati.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

public class SiftDetector {

	static final int sideK = 10;
	static final String[] imageSet = new String[]{"images/lena.pgm", "images/girl-color.bmp"};
	
	public SiftDetector(){
		String lib = "D:/opencv/build/java/x86/opencv_java2413.dll";
        System.load(lib);
	}
	
	private MatOfKeyPoint calculateMatOfKeyPoints(Mat image, int sideK){

		int k = sideK*sideK;

        KeyPoint[] keypoints = new KeyPoint[k];
        float stepWidth = image.width() / (sideK+2);
        float stepHeight = image.height() / (sideK+2);
        for(int i=0; i<sideK; i++){
        	for(int j=0; j<sideK; j++){
        		keypoints[i*sideK+j] = new KeyPoint(stepWidth*(i+1.5f), stepHeight*(j+1.5f), 1);
        	}
        }
        
        MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
        matOfKeyPoints.fromArray(keypoints);
        return matOfKeyPoints;
	}
	
	public String detectFace(String img1){
		Mat firstImage = Highgui.imread(img1, Highgui.CV_LOAD_IMAGE_COLOR);
        MatOfKeyPoint firstImageMatOfKeyPoints = calculateMatOfKeyPoints(firstImage, sideK);
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        MatOfKeyPoint firstImageDescriptors = new MatOfKeyPoint();
        descriptorExtractor.compute(firstImage, firstImageMatOfKeyPoints, firstImageDescriptors);
        int idx = 0;
        double minDistance = compareFaceImages(firstImageDescriptors, imageSet[0]);
        for(int i=1; i<imageSet.length; i++){
        	double auxDistance = compareFaceImages(firstImageDescriptors, imageSet[i]);
        	if(auxDistance < minDistance){
        		minDistance = auxDistance;
        		idx = i;
        	}
        }
        return imageSet[idx];
	}
	
	private double compareFaceImages(MatOfKeyPoint firstImageDescriptors, String img2){
		
        Mat secondImage = Highgui.imread(img2, Highgui.CV_LOAD_IMAGE_COLOR);
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        MatOfKeyPoint secondImageMatOfKeyPoints = calculateMatOfKeyPoints(secondImage, sideK);
        MatOfKeyPoint secondImageDescriptors = new MatOfKeyPoint();
        descriptorExtractor.compute(secondImage, secondImageMatOfKeyPoints, secondImageDescriptors);    
        
        List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        System.out.println("Matching images...");
        descriptorMatcher.knnMatch(firstImageDescriptors, secondImageDescriptors, matches, sideK*sideK);
        System.out.println("Calculating good match list...");
        LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

        double distanceSum = 0;
        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            for(DMatch d : dmatcharray){
            	if(d.trainIdx == d.queryIdx){
                    distanceSum+=d.distance;
            	}
            }
        }
        return distanceSum;
	}
	
	public String keyPoints(String img1){
		 	String bookObject = img1;

	        System.out.println("Started....");
	        System.out.println("Loading images...");
	        Mat objectImage = Highgui.imread(bookObject, Highgui.CV_LOAD_IMAGE_COLOR);

	        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
	        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
	        System.out.println("Detecting key points...");
	        featureDetector.detect(objectImage, objectKeyPoints);
	        KeyPoint[] keypoints = objectKeyPoints.toArray();

	        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
	        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
	        System.out.println("Computing descriptors...");
	        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

	        // Create the matrix for output image.
	        Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
	        Scalar newKeypointColor = new Scalar(255, 0, 0);

	        System.out.println("Drawing key points on image...");
	        Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

	        
	        Highgui.imwrite("output/temp_out.jpg", outputImage);

	        System.out.println("Ended....");
	        return "output/temp_out.jpg";
	}
	
	
	public String detection(String img1, String img2){

        String bookObject = img1;
        String bookScene = img2;

        System.out.println("Started....");
        System.out.println("Loading images...");
        Mat objectImage = Highgui.imread(bookObject, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat sceneImage = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);

        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        System.out.println("key points 1: "+keypoints.length);
        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        System.out.println("Computing descriptors...");
        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

        // Create the matrix for output image.
        Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar newKeypointColor = new Scalar(255, 0, 0);

        System.out.println("Drawing key points on image...");
        Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

        // Match object image with the scene image
        MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
        System.out.println("Detecting key points in second image...");
        featureDetector.detect(sceneImage, sceneKeyPoints);
        System.out.println("key points 2: "+sceneKeyPoints.size().height);
        System.out.println("Computing descriptors in second image...");
        descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

        Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar matchestColor = new Scalar(0, 255, 0);
        
        List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        System.out.println("Matching images...");
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

        System.out.println("Calculating good match list...");
        LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

        float nndrRatio = 0.7f;
        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);

            }
        }
        System.out.println("matches: "+goodMatchesList.size());
        System.out.println();
        System.out.println("Drawing matches image...");
        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(goodMatchesList);

        Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches, matchoutput, matchestColor, newKeypointColor, new MatOfByte(), 2);

        
        Highgui.imwrite("output/temp_out.jpg", matchoutput);

        System.out.println("Ended....");
        return "output/temp_out.jpg";
	}
	
}
