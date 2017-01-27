package createCogitiveTask;

import java.util.Arrays;
import java.util.List;
//
public class DenseCap {
	private String img_name;
	private double[] scores;
	private List<String> captions;
	private double[][] boxes;
	public String getImg_name() {
		return img_name;
	}
	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}
	public double[] getScores() {
		return scores;
	}
	public void setScores(double[] scores) {
		this.scores = scores;
	}
	public List<String> getCaptions() {
		return captions;
	}
	public void setCaptions(List<String> captions) {
		this.captions = captions;
	}
	public double[][] getBoxes() {
		return boxes;
	}
	public void setBoxes(double[][] boxes) {
		this.boxes = boxes;
	}

	public String returnData(){
		String data = Arrays.toString(scores);
		return data;
	}
}
