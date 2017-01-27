package createCogitiveTask;

import java.util.ArrayList;
import java.util.List;

public class DataListList {

	private List<DataList> datalistList = new ArrayList<>();
//
	public List<DataList> getDatalistList() {
		return datalistList;
	}

	public void setDatalistList(List<DataList> datalistList) {
		this.datalistList = datalistList;
	}

	public void addDataList(DataList dataList){
		datalistList.add(dataList);
	}


}
