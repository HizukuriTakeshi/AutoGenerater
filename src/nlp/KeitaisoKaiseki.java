package nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class KeitaisoKaiseki {


	public void Keitaiso(String str){
	Tokenizer tokenizer = Tokenizer.builder().build();

	List<Token> tokens = tokenizer.tokenize(str);

//	for (Token token : tokens) {
//		System.out.println("==================================================");
//		System.out.println("allFeatures : " + token.getAllFeatures());
//		System.out.println("surfaceFrom : " + token.getSurfaceForm());
//		System.out.println("allFeaturesArray : " + Arrays.asList(token.getAllFeaturesArray()));
//	}


	List<Word> words = new ArrayList<>();


	for(Token token : tokens){
		Word word = new Word(token);
		words.add(word);
	}


	String[] pos = {"は","が","って"};
	List<String> list = Arrays.asList(pos);

	for(Word word : words){
		//主語かどうがチェック
		if(list.contains(word.getSurfaceFrom())&&word.getType().equals("助詞")){
			System.out.println(word.getSurfaceFrom());
		}
	}

	StringBuilder buf = new StringBuilder();
	for(Word word : words){
	    buf.append(word.getSurfaceFrom());
	}
	buf.toString();
	//System.out.println(buf);

	}
}
