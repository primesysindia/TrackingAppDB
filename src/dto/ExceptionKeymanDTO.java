package dto;

public class ExceptionKeymanDTO {
	String TotalBeatnotCover,TotalBeatCoverSucesfull,OverSpeedDevices,KeyManOffDevice,KeyManOnDevice;

	public String getKeyManOffDevice() {
		return KeyManOffDevice;
	}

	public String getKeyManOnDevice() {
		return KeyManOnDevice;
	}

	public void setKeyManOnDevice(String keyManOnDevice) {
		KeyManOnDevice = keyManOnDevice;
	}

	public void setKeyManOffDevice(String keyManOffDevice) {
		KeyManOffDevice = keyManOffDevice;
	}

	public String getOverSpeedDevices() {
		return OverSpeedDevices;
	}

	public void setOverSpeedDevices(String overSpeedDevices) {
		OverSpeedDevices = overSpeedDevices;
	}

	public String getTotalBeatnotCover() {
		return TotalBeatnotCover;
	}

	public void setTotalBeatnotCover(String totalBeatnotCover) {
		TotalBeatnotCover = totalBeatnotCover;
	}

	public String getTotalBeatCoverSucesfull() {
		return TotalBeatCoverSucesfull;
	}

	public void setTotalBeatCoverSucesfull(String totalBeatCoverSucesfull) {
		TotalBeatCoverSucesfull = totalBeatCoverSucesfull;
	}

}
