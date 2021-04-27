package guitarModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class StaffTuning {

	@JacksonXmlProperty(isAttribute=true)
	private int line;

	@JacksonXmlProperty(localName = "tuning-step")
	@JsonProperty("tuning-step")
	private String tuningStep;

	@JacksonXmlProperty(localName = "tuning-octave")
	private String tuningOctave;

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getTuningStep() {
		return tuningStep;
	}

	public void setTuningStep(String tuningStep) {
		this.tuningStep = tuningStep;
	}

	public String getTuningOctave() {
		return tuningOctave;
	}

	public void setTuningOctave(String tuningOctave) {
		this.tuningOctave = tuningOctave;
	}

	public StaffTuning() {}
	public StaffTuning(int line, String tuningStep, String tuningOctave) {
		super();
		this.line = line;
		this.tuningStep = tuningStep;
		this.tuningOctave = tuningOctave;
	}  
}
