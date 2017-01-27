package nlp;

import org.atilika.kuromoji.Token;

public class Word {
	private String surfaceFrom;
	private String type;


	public Word(Token token){
		this.setSurfaceFrom(token.getSurfaceForm());
		this.setType(token.getAllFeaturesArray()[0]);
	}


	public String getSurfaceFrom() {
		return surfaceFrom;
	}
	public void setSurfaceFrom(String surfaceFrom) {
		this.surfaceFrom = surfaceFrom;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


}
