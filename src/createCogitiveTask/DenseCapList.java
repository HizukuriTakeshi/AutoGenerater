package createCogitiveTask;

import java.util.List;
import java.util.Map;

public class DenseCapList {
//
	private Map<String, Object> opt;

	private List<DenseCap> results;

	public Map<String, Object> getOpt() {
		return opt;
	}

	public void setOpt(Map<String, Object> opt) {
		this.opt = opt;
	}

	public List<DenseCap> getResults() {
		return results;
	}

	public void setResults(List<DenseCap> results) {
		this.results = results;
	}

	public DataListList toDataListList() {
		// TODO 自動生成されたメソッド・スタブ

		//Dataを入れる

		DataListList dataListList = new DataListList();

//		for (DenseCap tmp : results) {
//			DataList dataList = new DataList();
//			//尤度0以上の説明文のみ使用
//			for(int i = 0;tmp.getScores()[i]>0;i++){
//				Data tmp2 = new Data(tmp.getImg_name(), tmp.getScores()[i], tmp.getCaptions().get(i),tmp.getBoxes()[i]);
//				dataList.addData(tmp2);
//			}
//			dataListList.addDataList(dataList);
//
//		}
		
		for (DenseCap tmp : results) {
			DataList dataList = new DataList();
			//尤度0以上の説明文のみ使用
			for(int i = 0;i<tmp.getScores().length;i++){
				Data tmp2 = new Data(tmp.getImg_name(), tmp.getScores()[i], tmp.getCaptions().get(i),tmp.getBoxes()[i]);
				dataList.addData(tmp2);
			}
			dataListList.addDataList(dataList);

		}

		return dataListList;
	}
}
