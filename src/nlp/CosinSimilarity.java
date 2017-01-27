package nlp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.apache.lucene.search.spell.LevensteinDistance;
 

public class CosinSimilarity {

	public double caluculate(String str1, String str2){
		List<String> a1 = MorphologicalAnalysis.breakUp(str1);
		List<String> a2 = MorphologicalAnalysis.breakUp(str2);


		List<String> tmp = new ArrayList<String>();
		tmp.addAll(a1);
		tmp.addAll(a2);


		Set<String> set = new HashSet<>(tmp);
		List<String> uniqu_words = new ArrayList<>(set);
		

		double[] f1 = makeFlags(uniqu_words, a1);
		double[] f2 = makeFlags(uniqu_words, a2);
		

		Vector v1 = new Vector(f1, f1.length);
		Vector v2 = new Vector(f2, f2.length);

		return v1.naiseki(v2)/(v1.zettai()*v2.zettai());
	}

	public double[] makeFlags(List<String> uniqu_words, List<String> elements){
		double[] flags = new double[uniqu_words.size()];
		 double flag =  0;

		for(int i = 0; i< uniqu_words.size();i++){
		flag = (elements.contains(uniqu_words.get(i)) == true ? 1 : 0);
		flags[i]=flag;
		}

		return flags;
	}

	public double levensteinDistance(String a, String b){
		 LevensteinDistance l_algo = new LevensteinDistance();
		return l_algo.getDistance(a,b);
	}
	

	public double jaroWinklerDistance(String a, String b){
		JaroWinklerDistance j_algo = new JaroWinklerDistance();
		return j_algo.getDistance(a,b);
	}

}
