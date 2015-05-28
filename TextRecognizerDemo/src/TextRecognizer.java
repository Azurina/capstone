package textAreaGetter;

import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;

public class TextAreaGetter {
	static {
		try {
			System.loadLibrary("opencv_java2410");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	// public static void surroundKeypoint(MatOfKeyPoint matOfKeyPoint, Mat
	// image) {
	// List<KeyPoint> keypointList = matOfKeyPoint.toList();
	// Point p0 = keypointList.get(0).pt;
	// // System.out.println("x: " + p.x + "   y: " + p.y);
	// double minX = p0.x;
	// double maxX = p0.x;
	// double minY = p0.y;
	// double maxY = p0.y;
	//
	// double x, y;
	// int a1 = 0;
	// int a2 = 0;
	// int a3 = 0;
	// int a4 = 0;
	// Point p;
	// for (int i = 1; i < keypointList.size(); ++i) {
	// p = keypointList.get(i).pt;
	// x = p.x;
	// y = p.y;
	//
	// if (i < 500)
	// System.out.println(i + ":   " + x + "   " + y + "   "
	// + keypointList.get(i).class_id);
	//
	// if (x < minX) {
	// minX = x;
	// a1 = i;
	// }
	// ;
	// if (x > maxX) {
	// maxX = x;
	// a2 = i;
	// }
	// ;
	// if (y < minY) {
	// minY = y;
	// a3 = i;
	// }
	// ;
	// if (y > maxY) {
	// maxY = y;
	// a4 = i;
	// }
	// ;
	// }
	//
	// // surround
	// Point topLeft = new Point(minX, minY);
	// Point topRight = new Point(maxX, minY);
	// Point bottomLeft = new Point(minX, maxY);
	// Point bottomRight = new Point(maxX, maxY);
	//
	// //System.out.println(a1 + "   " + a2 + "   " + a3 + "   " + a4);
	// // System.out.println(topLeft.toString());
	// // System.out.println(topRight.toString());
	// // System.out.println(bottomLeft.toString());
	// // System.out.println(bottomRight.toString());
	//
	// Core.line(image, topLeft, topRight, new Scalar(255, 0, 0), 3);
	// Core.line(image, topLeft, bottomLeft, new Scalar(255, 0, 0), 3);
	// Core.line(image, topRight, bottomRight, new Scalar(255, 0, 0), 3);
	// Core.line(image, bottomLeft, bottomRight, new Scalar(255, 0, 0), 3);
	//
	// // Highgui.imwrite("picture/result1.jpg", image);
	// }

	public static Mat getTextArea(MatOfKeyPoint matOfKeyPoint, Mat image) {
		List<KeyPoint> keypointList = matOfKeyPoint.toList();
		Point p0 = keypointList.get(0).pt;
		double minX = p0.x;
		double maxX = p0.x;
		double minY = p0.y;
		double maxY = p0.y;

		double x, y;
		Point p;
		for (int i = 1; i < keypointList.size(); ++i) {
			p = keypointList.get(i).pt;
			x = p.x;
			y = p.y;

			if (x < minX) {
				minX = x;
				// a1 = i;
			}
			;
			if (x > maxX) {
				maxX = x;
				// a2 = i;
			}
			;
			if (y < minY) {
				minY = y;
				// a3 = i;
			}
			;
			if (y > maxY) {
				maxY = y;
				// a4 = i;
			}
			;
		}

		Rect rect = new Rect();
		rect.x = (int) Math.floor(minX);
		rect.y = (int) Math.floor(minY);
		rect.width = (int) Math.ceil(maxX - minX);
		rect.height = (int) Math.ceil(maxY - minY);

		System.out.println("x = " + rect.x);
		System.out.println("y = " + rect.y);
		System.out.println("width = " + rect.width);
		System.out.println("height = " + rect.height);

		Mat cropedImage = new Mat(image, rect);

		return cropedImage;
	}

	/**
	 * Detect feature keypoint and draw surrounding lines
	 * 
	 * @param mat
	 *            - the Mat from an image
	 * @param filenameOutput
	 *            - the output filename
	 */
	public static void featureDetection(Mat mat, String filenameOutput) {
		FeatureDetector fd = FeatureDetector
				.create(FeatureDetector.DYNAMIC_FAST);
		MatOfKeyPoint keypoint = new MatOfKeyPoint();
		fd.detect(mat, keypoint);
		Mat image = new Mat();
		// Features2d.drawKeypoints(mat, keypoint, image, Scalar.all(-1), );
		Features2d.drawKeypoints(mat, keypoint, image);
		Mat outputImage = getTextArea(keypoint, mat);
		Highgui.imwrite(filenameOutput, outputImage);
		// Highgui.imwrite(filenameOutput, image);
	}

	/**
	 * Convert an image to gray scale image, detect feature keypoint and draw
	 * surrounding lines
	 * 
	 * @param filenameInput
	 *            - the input filename URL image
	 * @param filenameOutput
	 *            - the output filename URL image
	 */
	public static void surroundProcessingImage(String filenameInput,
			String filenameOutput) {
		try {
			Mat image = Highgui.imread(filenameInput);
			Mat gray_image = new Mat();
			Imgproc.cvtColor(image, gray_image, Imgproc.COLOR_RGB2GRAY);
			featureDetection(gray_image, filenameOutput);
			Size s = new Size(1, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void determineLine1(Mat img) {
		  Vector<Rect> boundRect;
		     Mat img_gray, img_sobel, img_threshold, element;
		     Imgproc.cvtColor(img, img_gray, Imgproc.COLOR_RGB2GRAY); // to gray
		     Imgproc.Sobel(img_gray, img_sobel, CvType.CV_8U, 1, 0, 3, 1, 0, Imgproc.BORDER_DEFAULT);
		     Imgproc.threshold(img_sobel, img_threshold, 0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
		     element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(17, 3) ); 
		     Imgproc.morphologyEx(img_threshold, img_threshold, Imgproc.MORPH_CLOSE, element); //Does the trick
		     Vector<MatOfPoint> contours;
		     Imgproc.findContours(img_threshold, contours, new Mat(), 0, 1); 
		     Vector<Vector<Point>> contours_poly = new Vector<Vector<Point>>(contours.size());
		     for( int i = 0; i < contours.size(); i++ )
		         if (contours.get(i).size() > 100)
		         { 
		             approxPolyDP( cv::Mat(contours[i]), contours_poly[i], 3, true );
		             Rect appRect = appRect( boundingRect( cv::Mat(contours_poly[i]) ));
		             if (appRect.width>appRect.height) 
		                 boundRect.push_back(appRect);
		         }
		     return boundRect;
		 }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filename = "picture/test_japaneseText.jpg";
		surroundProcessingImage(filename, "picture/test1.jpg");
		System.out.println("Success");
	}
}
