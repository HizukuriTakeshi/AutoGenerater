package createCogitiveTask;

public class Data {
	private String img_name;
	private double score;
	private String caption;
	private double[] box;
	private int link = -1;
	private QuestionType type;
	private double wariai;
	private double coscore;


	public double getCoscore() {
		return coscore;
	}

	public void setCoscore(double coscore) {
		this.coscore = coscore;
	}

	//
	Data(String img_name, double score, String caption, double[] box){
		this.img_name = img_name;
		this.score = score;
		this.caption = caption;
		this.box = box;
	}

	public String getImg_name() {
		return img_name;
	}
	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public double[] getBox() {
		return box;
	}
	public void setBox(double[] box) {
		this.box = box;
	}

	public int getLink() {
		return link;
	}

	public void setLink(int link) {
		this.link = link;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public double getWariai() {
		return wariai;
	}

	public void setWariai(double wariai) {
		this.wariai = wariai;
	}
}
