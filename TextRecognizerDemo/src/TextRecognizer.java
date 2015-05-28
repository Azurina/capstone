import java.util.StringTokenizer;

import javafx.scene.shape.DrawMode;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;


public class TextRecognizer {
	static {
	    try {
	    	System.loadLibrary("opencv_java2410");
	    } catch (UnsatisfiedLinkError e) {
	      System.err.println("Native code library failed to load.\n" + e);
	      System.exit(1);
	    }
	  }
	
	public static void featureDetection(Mat mat) {
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.DYNAMIC_FAST);
		MatOfKeyPoint keypoint = new MatOfKeyPoint();
		fd.detect(mat, keypoint);
		Mat image = new Mat();
		//Features2d.drawKeypoints(mat, keypoint, image, Scalar.all(-1), );
		Features2d.drawKeypoints(mat, keypoint, image);
		System.out.println("Detection done!");
		Highgui.imwrite("picture/test3.jpg", image);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//String filename = "picture/test_japaneseText.jpg";
			//String filename = "picture/Untitled.jpg";
			String filename = "picture/Dekiru_front.jpg";
			Mat image = Highgui.imread(filename);
			Mat gray_image = new Mat();
			Imgproc.cvtColor(image, gray_image, Imgproc.COLOR_RGB2GRAY);
			featureDetection(gray_image);
			//Highgui.imwrite("picture/test2.jpg", gray_image);
			System.out.println("Success");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
//		String property = System.getProperty("java.library.path");
//		StringTokenizer parser = new StringTokenizer(property, ";");
//		while (parser.hasMoreTokens()) {
//		    System.err.println(parser.nextToken());
//		    }
	}

}
