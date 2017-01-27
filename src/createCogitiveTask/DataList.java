package createCogitiveTask;

import java.util.ArrayList;
import java.util.List;

public class DataList {
	private List<Data> datas = new ArrayList<>();
//
	public List<Data> getDatas() {
		return datas;
	}

	public void setDatas(List<Data> datas) {
		this.datas = datas;
	}


	public void addData(Data data){
		this.datas.add(data);
	}

}
