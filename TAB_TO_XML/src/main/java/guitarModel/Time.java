package guitarModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Time {
	private String beats;

	@JacksonXmlProperty(localName = "beat-type")
	private String beatType;

	public Time() {}
	public Time(String beats, String beatType) {
		super();
		this.beats = beats;
		this.beatType = beatType;
	}

	public String getBeats() {
		return beats;
	}

	public void setBeats(String beats) {
		this.beats = beats;
	}

	public String getBeatType() {
		return beatType;
	}

	public void setBeatType(String beatType) {
		this.beatType = beatType;
	}
}
