package dto;


import java.util.ArrayList;

public class HistoryInfoDTO {
	int historyDataSize;
	ArrayList<HistoryDTO> historyInfo;
	public int getHistoryDataSize() {
		return historyDataSize;
	}
	public void setHistoryDataSize(int historyDataSize) {
		this.historyDataSize = historyDataSize;
	}
	public ArrayList<HistoryDTO> getHistoryInfo() {
		return historyInfo;
	}
	public void setHistoryInfo(ArrayList<HistoryDTO> historyInfo) {
		this.historyInfo = historyInfo;
	}
	
	
}