package translation;

import com.memetix.mst.translate.Translate;


public class Transration {

	public Transration(){
		 // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
	    Translate.setClientId("client_task");
	    Translate.setClientSecret("64L2j7gMi9MMfu/5wwtxUvUN58zcyIH4RykeMdBgYt8=");

	}

	public String transrateCaption(String caption) {
    // English AUTO_DETECT -> japanese
    String translatedText = null;
	try {
		translatedText = caption;//Translate.execute(caption,Language.JAPANESE);
	} catch (Exception e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
	}
    //System.out.println("English AUTO_DETECT -> Japanese: " + translatedText);
	return translatedText;
	}
}
