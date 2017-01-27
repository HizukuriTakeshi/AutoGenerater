package imgProc;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import createCogitiveTask.QuestionType;
import imgProc.BackgroundSub;

public class FindDiff {

	private Mat diffImg;
	private Mat beforeImg;
	private Mat afterImg;

	public Mat getDiffImg() {
		return diffImg;
	}

	public void setDiffImg(Mat diffImg) {
		this.diffImg = diffImg;
	}

	public Mat getBeforeImg() {
		return beforeImg;
	}

	public void setBeforeImg(Mat beforeImg) {
		this.beforeImg = beforeImg;
	}

	public Mat getAfterImg() {
		return afterImg;
	}

	public void setAfterImg(Mat afterImag) {
		this.afterImg = afterImag;
	}

	public FindDiff(Mat dffImg, Mat beforeImg, Mat afterImg){
		setDiffImg(dffImg);
		setBeforeImg(beforeImg);
		setAfterImg(afterImg);
	}

	/**尤も変化してそうな輪郭の選択
	 * @param before_imgName
	 * @param after_imgName
	 */
	public void selectChangeContour(String before_imgName, String after_imgName, QuestionType type){

		String before_imgPath = before_imgName;
		String after_imgPath =  after_imgName;

		//差分計算
		BackgroundSub bgs = new BackgroundSub(before_imgPath, after_imgPath, type);

		//輪郭抽出した画像の切り出し
		Rect[] rects = bgs.findDifference(bgs.getDiffImg());


		Mat diff_img = bgs.getDiffImg();
		Mat after_img = Highgui.imread(after_imgPath);


		Rect max_r = new Rect();
		double max = 0;

		for(int i= 0; i<rects.length ; i++ ){
			Rect r = rects[i];

			double a = r.width*r.height;
			double b = diff_img.rows()*diff_img.cols();
			double result = bgs.checkBoundingBox(r);

			if(result > 0.1 && a/b > 0.005){
				if(max<result){
					max_r = r;
				}
			}	

		}
		max_r.set(new double[] {max_r.x, max_r.y ,max_r.width,max_r.height});
		Mat img = new Mat(after_img, max_r);
		Highgui.imwrite("imgs/processed/rinkaku.jpg",img);
	}

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
			//System.out.println(rects[i].x+" "+rects[i].y+" "+rects[i].height+" "+rects[i].width);
		}

		Highgui.imwrite("imgs/processed/test.png",dstt); 

		return rects;
	}

	public void selectChangeContour(){
		//差分計算

		//輪郭抽出した画像の切り出し
		Rect[] rects = findDifference(diffImg);


		Mat diff_img = getDiffImg();
		Mat after_img = getAfterImg();


		Rect max_r = new Rect();
		double max = 0;

		for(int i= 0; i<rects.length ; i++ ){
			Rect r = rects[i];

			double a = r.width*r.height;
			double b = diff_img.rows()*diff_img.cols();
			int yoko = r.x+r.width;
			int tate = r.y+r.height;
			if(r.x<0){
				r.x=0;
			}
			if(r.y<0){
				r.y=0;
			}
			if(yoko>diffImg.cols()){
				r.width = diffImg.cols()-r.x;
			}
			if(tate>diffImg.rows()){
				r.height = diffImg.rows()-r.y;
			}


			if(a/b > 0.005){
				System.out.println(r.x+" "+r.y+" "+r.height+" "+r.width);
				double result = checkBoundingBox(r, i);
				if(result > 0.1){
					if(max<result){	
						max_r = r;
					}
				}	
			}

		}

		if(max_r.x-max_r.width*0.05>0){
			max_r.x = (int) (max_r.x-max_r.width*0.05);
		}else{
			max_r.x = 0;	
		}
		if(max_r.y-max_r.height*0.05>0){
			max_r.y = (int) (max_r.y-max_r.height*0.05);
		}else{
			max_r.y = 0;
		}

		if(max_r.width*1.1+max_r.x<afterImg.cols()){
			max_r.width= (int) (max_r.width*1.1);
		}else{
			max_r.width= afterImg.cols()-max_r.x;	
		}
		if(max_r.height*1.1+max_r.y<afterImg.rows()){
			max_r.height=(int) (max_r.height*1.1);
		}else{
			max_r.height= afterImg.rows()-max_r.y;		
		}

		//実際の輪郭よりも大きくBBをとる
		max_r.set(new double[] {max_r.x, max_r.y ,max_r.width,max_r.height});

		

		Mat img = new Mat(after_img, max_r);
		Highgui.imwrite("imgs/processed/rinkaku.jpg",img);
	}

	public double checkBoundingBox(Rect r, int name){

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
}
