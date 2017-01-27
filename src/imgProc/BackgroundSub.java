package imgProc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import createCogitiveTask.Data;
import createCogitiveTask.QuestionType;

public class BackgroundSub {

	private Mat diffImg;
	private Mat homoImg;
	private Mat homobeforeImg;
	
	String resultdirPath;
	public Mat getHomobeforeImg() {
		return homobeforeImg;
	}

	public void setHomobeforeImg(Mat homobeforeImg) {
		this.homobeforeImg = homobeforeImg;
	}

	private Mat beforeImg;
	private Mat afterImg;

	public Mat getBeforeImg() {
		return beforeImg;
	}

	public void setBeforeImg(Mat beforeImg) {
		this.beforeImg = beforeImg;
	}

	public Mat getAfterImg() {
		return afterImg;
	}

	public void setAfterImg(Mat afterImg) {
		this.afterImg = afterImg;
	}

	public Mat getHomoImg() {
		return homoImg;
	}

	public void setHomoImg(Mat homoImg) {
		this.homoImg = homoImg;
	}

	private int x, y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Mat getDiffImg() {
		return diffImg;
	}

	public void setDiffImg(Mat diffImg) {
		this.diffImg = diffImg;
	}



	/**
	 * @param beforeImgpath 過去画像
	 * @param afterImgpath
	 */
	public BackgroundSub(String beforeImgpath, String afterImgpath,QuestionType type){
		
		File beforefile = new File(beforeImgpath);
		File afterfile = new File(afterImgpath);
		
		resultdirPath = "./imgs/result/"+beforefile.getName()+"_"+afterfile.getName();
		
		//消失の時は逆の処理にする
		if(type.equals(QuestionType.DISAPPEARANCE)){
			String tmp = beforeImgpath;
			beforeImgpath = afterImgpath;
			afterImgpath = tmp;
		}
		
		Mat img_before = Highgui.imread(beforeImgpath, 0);
		Mat img_after = Highgui.imread(afterImgpath, 0);

		
		System.out.println(resultdirPath);
		
		setBeforeImg(Highgui.imread(beforeImgpath));
		setAfterImg(Highgui.imread(afterImgpath));
		
		String typename;
		if(type.equals(QuestionType.APPEARANCE)){
			typename = "appearance";
		}else if(type.equals(QuestionType.DISAPPEARANCE)){
			typename = "disappearance";
		}else{
			typename = "null";
		}

		//		//特徴検出
		//		FeatureDetector detector = FeatureDetector.create(4); //4 = SURF 
		//		MatOfKeyPoint keypoints_object = new MatOfKeyPoint();
		//		MatOfKeyPoint keypoints_scene  = new MatOfKeyPoint();
		//		detector.detect(img_before, keypoints_object);
		//		detector.detect(img_after, keypoints_scene);
		//
		//		//特徴記述
		//		DescriptorExtractor extractor = DescriptorExtractor.create(2); //2 = SURF;
		//		Mat descriptor_object = new Mat();
		//		Mat descriptor_scene = new Mat();
		//		extractor.compute(img_before, keypoints_object, descriptor_object);
		//		extractor.compute(img_after, keypoints_scene, descriptor_scene);
		//
		//		//マッチング
		//		DescriptorMatcher matcher = DescriptorMatcher.create(1); // 1 = FLANNBASED
		//		List<MatOfDMatch> knnmatch_points = new ArrayList<MatOfDMatch>();
		//		matcher.knnMatch(descriptor_object, descriptor_scene, knnmatch_points, 2);
		//
		//		final double match_par = 0.6;
		//		LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
		//		MatOfDMatch gm = new MatOfDMatch();
		//		LinkedList<Point> match_point1 = new LinkedList<Point>();
		//		LinkedList<Point> match_point2 = new LinkedList<Point>();
		//
		//		//knnマッチング結果の絞り込み
		//		for(int i = 0; i< knnmatch_points.size(); ++i){
		//			double distance1 = knnmatch_points.get(i).toArray()[0].distance;
		//			double distance2 = knnmatch_points.get(i).toArray()[1].distance;
		//			//第2候補から距離値が離れている点のみ抽出
		//			if(distance1< distance2*match_par){
		//				good_matches.addLast(knnmatch_points.get(i).toArray()[0]);
		//				match_point1.add(keypoints_object.toList().get(knnmatch_points.get(i).toArray()[0].queryIdx).pt);
		//				match_point2.add(keypoints_scene.toList().get(knnmatch_points.get(i).toArray()[0].trainIdx).pt);
		//			}
		//		}
		//
		//		gm.fromList(good_matches);
		//		MatOfPoint2f m_p1 = new MatOfPoint2f();
		//		m_p1.fromList(match_point1);
		//		MatOfPoint2f m_p2 = new MatOfPoint2f();
		//		m_p2.fromList(match_point2);
		//
		//		//ホモグラフィ行列計算
		//		Mat masks = new Mat();
		//		Mat H = Calib3d.findHomography(m_p1, m_p2, 3, Calib3d.RANSAC, masks);
		//		Mat HP = H.clone();
		//
		//		LinkedList<DMatch> inlinerMatch = new LinkedList<DMatch>();
		//		MatOfDMatch inM = new MatOfDMatch();
		//
		//		//RANSACに使った点のみ抽出
		//		for(int i = 0; i< masks.rows(); ++i){
		//			if(masks.get(i, 0)[0] == 1){
		//				inlinerMatch.addLast(good_matches.get(i));
		//			}
		//		}
		//		inM.fromList(inlinerMatch);

		//		Mat img_matches = new Mat();
		//		Features2d.drawMatches(img_before, keypoints_object, img_after, keypoints_scene, inM, img_matches, new Scalar(255,0,0), new Scalar(0,0,255), new MatOfByte(), 2);
		//		Highgui.imwrite("./imgs/match.jpg", img_matches);
		//
		//
		Mat img_homo = new Mat();
		Mat img_diff = new Mat();
		//
		//		//ホモグラフィ変換
		//		Imgproc.warpPerspective(img_before, homo_img, HP, new Size(img_before.cols(), img_before.rows()));
		if(checkMatch(getAfterImg(), getBeforeImg(), img_homo,typename)){
		setHomoImg(img_homo);
		}else{
		setHomoImg(getBeforeImg());
		}
		Mat img_homo_gray = new Mat();
		Imgproc.cvtColor(getHomoImg(), img_homo_gray, Imgproc.COLOR_RGBA2GRAY);
		//差分
		Core.absdiff(img_homo_gray, img_after, img_diff);
		Highgui.imwrite(resultdirPath+"/"+typename+"/"+typename+"_"+"purediff.jpg",img_diff);
		//黒背景を処理
		for(int i = 0; i < img_diff.cols();i++){
			for(int j = 0; j < img_diff.rows(); j++){
				if(getHomoImg().get(j, i)[0]==0){
					img_diff.put(j, i, new double[] {0});
				}
			}
		}

		//しきい値
		Imgproc.threshold(img_diff, img_diff, 0.0,255.0,Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		//ノイズ除去
		Imgproc.erode(img_diff, img_diff, new Mat(), new Point(-1,-1), 1);
		//欠損部補完
		Imgproc.dilate(img_diff, img_diff, new Mat());

		setDiffImg(img_diff);

		setX(img_before.cols());
		setY(img_before.rows());

		Highgui.imwrite(resultdirPath+"/"+typename+"/"+typename+"_"+"homo.jpg",getHomoImg());
		Highgui.imwrite(resultdirPath+"/"+typename+"/"+typename+"_"+"diff.jpg",img_diff);
	}

	/**キャプションのBBを切り出して差分と比較して出現物体説明文を見つける
	 * @param d
	 * @param name
	 * @return
	 */
	public double checkBoundingBox(Data d){
		//DenseCapのBBをリサイズ
		double [] box = resize(d, 720, 540);

		Mat cut_img = new Mat(getDiffImg(), new Rect(box));

		//BB内の差の割合計算
		double sum = 0;
		for(int i = 0; i< cut_img.rows(); i++){
			for(int j = 0; j < cut_img.cols(); j++){
				sum+= cut_img.get(i, j)[0]/255;
			}
		}

		double result = sum/(cut_img.rows()*cut_img.cols());
		if(result > 0.1){
			//System.out.println(result);
			//Highgui.imwrite("imgs/processed/cut"+name+".jpg",cut_img);
		}
		
		return result;
	}

	public double[] resize(Data d, int width, int height){
		double[] box = new double[4];
		int x,y,w,h;

		if(d.getBox()[0] > 0){
			x = (int)((d.getBox()[0]*getX()/width));
		}else{
			x= 0;
		}
		if(d.getBox()[1] > 0){
			y = (int)((d.getBox()[1]*getY()/height));
		}else{
			y= 0;
		}
		//横幅が画像サイズを越えないとき
		if(x+(int)d.getBox()[2]*getX()/width < getX()){
			w = (int)d.getBox()[2]*getX()/width;
		}else{
			w = getX()-x;
		}

		//縦幅が画像サイズを越えないとき
		if(y+(int)d.getBox()[3]*getY()/height < getY()){
			h = (int)d.getBox()[3]*getY()/height;
		}else{
			h = getY()-y;
		}
		box[0] = x;
		box[1] = y;
		box[2] = w;
		box[3] = h;

		return box;
	}

	/**輪郭抽出
	 * @param diff_img
	 * @return
	 */
	public Rect[] findDifference(Mat diff_img){
		Mat hierarchy=Mat.zeros(new Size(5,5), CvType.CV_8UC1);
		Mat invsrc = diff_img.clone();
		List<MatOfPoint> contours=new ArrayList<MatOfPoint>(); 
		Imgproc.findContours(invsrc, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_L1); 
		Mat dstt=Mat.zeros(new Size(diff_img.width(),diff_img.height()),CvType.CV_8UC3);  
		Scalar color=new Scalar(255,255,255);  
		Imgproc.drawContours(dstt, contours, -1, color,1);
		Rect [] rects = new Rect[contours.size()];
		int i=0;  
		for(i=0;i<contours.size();i++)  
		{  
			MatOfPoint ptmat= contours.get(i);  

			color=new Scalar(255,0,0);  
			MatOfPoint2f ptmat2 = new MatOfPoint2f( ptmat.toArray() );  
			RotatedRect bbox=Imgproc.minAreaRect(ptmat2);  
			Rect box=bbox.boundingRect();
			Core.circle(dstt, bbox.center, 5, color,-1);  
			color=new Scalar(0,255,0);  
			Core.rectangle(dstt,box.tl(),box.br(),color,2); 
			rects[i] = box;
		}

		Highgui.imwrite("imgs/processed/test.png",dstt); 

		return rects;
	}

	/**
	 * @param past 過去の写真(ホモグラフィ変形後)
	 * @param src2　現在の写真
	 * @param width 部分差分画像の横幅
	 * @param height　部分差分画像の縦幅
	 * @param x 差分画像のx移動 今のところx,yのパラメータはwidthとheightと同じにする(重なり部分の処理未実装のため)
	 * @param y 差分画像のy移動 
	 * @return
	 */
	public Mat createPartSub(Mat past, Mat present, int width, int height, int x, int y){
		//結果出力用mat
		//単純に差分をとる
		Mat baseimg = imageDiff(past, present);

		//作業用mat
		Mat target = new Mat();


		int i = 0;
		int j = 100;
		int w=width;
		boolean flagx = true;
		boolean flagy = true;
		Rect box;

		while(i<present.cols()||flagx){
			if(i+width>present.cols()){
				w=present.cols()-i;
				flagx=false;
			}
			j=0;
			int h = height;
			flagy=true;
			while(j<present.rows()&&flagy){

				if(j+height>present.rows()){
					h=present.rows()-j;
					flagy=false;

				}
				System.out.println(i+" "+ j +" "+w+" "+h);
				box = new Rect(new double[] {i,j,w,h});
				//ターゲット画像の切り出し
				target = new Mat(present,box);

				//対応画像の切り出し
				Mat tmp = new Mat();
				//マッチングの結果があるなら
				if(checkMatch(target,past,tmp, "null")){
					//ターゲット画像と対応画像の差分
					Mat diff = imageDiff(target, tmp);
					//ベース画像の位置
					Mat Roi= new Mat(baseimg, box);
					//画像の貼り付け
					diff.copyTo(Roi);
					//画像の左上角のy座標増
				}
				j= j+y;
			}
			//画像の左上角のx座標増
			i=i+x;
		}

		//黒背景を処理
		for(int k = 0; k < baseimg.cols();k++){
			for(int l = 0; l < baseimg.rows(); l++){
				if(past.get(l, k)[0]==0){
					baseimg.put(l, k, new double[] {0});
				}
			}
		}

		//しきい値
		Imgproc.threshold(baseimg, baseimg, 0.0,255.0,Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		//ノイズ除去
		Imgproc.erode(baseimg, baseimg, new Mat(), new Point(-1,-1), 1);
		//欠損部補完
		Imgproc.dilate(baseimg, baseimg, new Mat());

		Highgui.imwrite("imgs/processed/part.png",baseimg); 
		return baseimg;
	}

	/**
	 * @param objectImage オブジェクトのmat
	 * @param sceneImage  シーンのmat
	 * @return
	 */
	public boolean checkMatch(Mat objectImage, Mat sceneImage, Mat dst, String typename){

		System.out.println("Started....");
		System.out.println("Loading images...");
		//SURF特徴
		MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
		FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
		//		System.out.println("Detecting key points...");
		featureDetector.detect(objectImage, objectKeyPoints);
		//		KeyPoint[] keypoints = objectKeyPoints.toArray();
		//		System.out.println(keypoints);
		//特徴記述
		MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
		DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
		//		System.out.println("Computing descriptors...");
		descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

		// Create the matrix for output image.
		//		Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
		//		Scalar newKeypointColor = new Scalar(255, 0, 0);

		//		System.out.println("Drawing key points on object image...");
		//		Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

		// Match object image with the scene image
		MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
		MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
		//		System.out.println("Detecting key points in background image...");
		featureDetector.detect(sceneImage, sceneKeyPoints);
		//		System.out.println("Computing descriptors in background image...");
		descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

		//		Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
		//		Scalar matchestColor = new Scalar(0, 255, 0);

		List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
		DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
		//		System.out.println("Matching object and scene images...");
		descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

		//		System.out.println("Calculating good match list...");
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

		//		System.out.println(goodMatchesList.size());
		if (goodMatchesList.size() >= 20) {
			System.out.println("Object Found!!!");

			List<KeyPoint> objKeypointlist = objectKeyPoints.toList();
			List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();

			LinkedList<Point> objectPoints = new LinkedList<>();
			LinkedList<Point> scenePoints = new LinkedList<>();

			for (int i = 0; i < goodMatchesList.size(); i++) {
				objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);
				scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);
			}

			MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
			objMatOfPoint2f.fromList(objectPoints);
			MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
			scnMatOfPoint2f.fromList(scenePoints);

			Mat homography = Calib3d.findHomography(scnMatOfPoint2f,objMatOfPoint2f, Calib3d.RANSAC, 2);


			System.out.println("Drawing matches image...");
			MatOfDMatch goodMatches = new MatOfDMatch();
			goodMatches.fromList(goodMatchesList);


			Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
			Scalar matchestColor = new Scalar(0, 255, 0);
			Scalar newKeypointColor = new Scalar(255, 0, 0);
			Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches, matchoutput, matchestColor, newKeypointColor, new MatOfByte(), 2);
			Highgui.imwrite(resultdirPath+"/"+typename+"/"+typename+"_"+"matchoutput.jpg", matchoutput);

			Size s = new Size(objectImage.cols(),objectImage.rows());

			Imgproc.warpPerspective(sceneImage, dst, homography, s);			

		} else {
			System.out.println("Object Not Found");
			dst = sceneImage.clone();
			FileWriter fw2;
			try {
				fw2 = new FileWriter(resultdirPath+"/"+typename+"/ObjectNotFound.txt",false);
				PrintWriter pw2 = new PrintWriter(new BufferedWriter(fw2));
				//ファイルに書き出す
				pw2.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}  //※１
			return false;
		}

		System.out.println("Ended....");
		return true;
	}

	public  Mat imageDiff(Mat src1, Mat src2){
		Mat result = new Mat();
		Mat gray_img1 = new Mat();
		Mat gray_img2 = new Mat();
		//グレースケールに変換
		Imgproc.cvtColor(src1, gray_img1, Imgproc.COLOR_RGBA2GRAY);
		Imgproc.cvtColor(src2, gray_img2, Imgproc.COLOR_RGBA2GRAY);

		//差分
		Core.absdiff(gray_img1, gray_img2, result);
		return result;
	}

	public double checkBoundingBox(Rect r){

		Mat diff_img = getDiffImg();
		Mat cut_img = new Mat(diff_img, r);

		//BB内の差の割合計算
		double sum = 0;
		for(int i = 0; i< cut_img.rows(); i++){
			for(int j = 0; j < cut_img.cols(); j++){
				sum+= cut_img.get(i, j)[0]/255;
			}
		}

		double result = sum/(cut_img.rows()*cut_img.cols());

		return result;	
	}

	public Mat test(Mat img_after,Mat img_before){
		//特徴検出
		FeatureDetector detector = FeatureDetector.create(4); //4 = SURF 
		MatOfKeyPoint keypoints_object = new MatOfKeyPoint();
		MatOfKeyPoint keypoints_scene  = new MatOfKeyPoint();
		detector.detect(img_before, keypoints_object);
		detector.detect(img_after, keypoints_scene);

		//特徴記述
		DescriptorExtractor extractor = DescriptorExtractor.create(2); //2 = SURF;
		Mat descriptor_object = new Mat();
		Mat descriptor_scene = new Mat();
		extractor.compute(img_before, keypoints_object, descriptor_object);
		extractor.compute(img_after, keypoints_scene, descriptor_scene);

		//マッチング
		DescriptorMatcher matcher = DescriptorMatcher.create(1); // 1 = FLANNBASED
		List<MatOfDMatch> knnmatch_points = new ArrayList<MatOfDMatch>();
		matcher.knnMatch(descriptor_object, descriptor_scene, knnmatch_points, 2);

		final double match_par = 0.6;
		LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
		MatOfDMatch gm = new MatOfDMatch();
		LinkedList<Point> match_point1 = new LinkedList<Point>();
		LinkedList<Point> match_point2 = new LinkedList<Point>();

		//knnマッチング結果の絞り込み
		for(int i = 0; i< knnmatch_points.size(); ++i){
			double distance1 = knnmatch_points.get(i).toArray()[0].distance;
			double distance2 = knnmatch_points.get(i).toArray()[1].distance;
			//第2候補から距離値が離れている点のみ抽出
			if(distance1< distance2*match_par){
				good_matches.addLast(knnmatch_points.get(i).toArray()[0]);
				match_point1.add(keypoints_object.toList().get(knnmatch_points.get(i).toArray()[0].queryIdx).pt);
				match_point2.add(keypoints_scene.toList().get(knnmatch_points.get(i).toArray()[0].trainIdx).pt);
			}
		}

		gm.fromList(good_matches);
		MatOfPoint2f m_p1 = new MatOfPoint2f();
		m_p1.fromList(match_point1);
		MatOfPoint2f m_p2 = new MatOfPoint2f();
		m_p2.fromList(match_point2);

		//ホモグラフィ行列計算
		Mat masks = new Mat();
		Mat H = Calib3d.findHomography(m_p1, m_p2, 3, Calib3d.RANSAC, masks);
		LinkedList<DMatch> inlinerMatch = new LinkedList<DMatch>();
		MatOfDMatch inM = new MatOfDMatch();

		//RANSACに使った点のみ抽出
		for(int i = 0; i< masks.rows(); ++i){
			if(masks.get(i, 0)[0] == 1){
				inlinerMatch.addLast(good_matches.get(i));
			}
		}
		inM.fromList(inlinerMatch);

		Mat img_matches = new Mat();
		Features2d.drawMatches(img_before, keypoints_object, img_after, keypoints_scene, inM, img_matches, new Scalar(255,0,0), new Scalar(0,0,255), new MatOfByte(), 2);
		Highgui.imwrite("./imgs/processed/testMatch.jpg", img_matches);

		Mat result = new Mat();
		Size s = new Size(img_before.cols(),img_before.rows());
		Imgproc.warpPerspective(img_after, result, H, s);			
		Highgui.imwrite("./imgs/processed/testHomo.jpg", result);
		return result;
	}
}
