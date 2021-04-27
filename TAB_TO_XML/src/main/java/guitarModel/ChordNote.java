package guitarModel;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({ "chord", "pitch", "duration", "voice", "type", "notations" })
public class ChordNote extends Note {
	@JacksonXmlProperty(localName = "chord")
	private String chord;

	public String getChord() {
		return chord;
	}

	public void setChord() {
		this.chord = null;
	}

	public ChordNote() {
	}
}
