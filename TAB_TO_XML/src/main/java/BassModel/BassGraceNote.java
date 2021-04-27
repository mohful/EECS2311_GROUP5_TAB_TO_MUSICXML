package BassModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({ "grace", "pitch", "duration", "voice","stem", "type", "notations" })
public class BassGraceNote extends Note{
	@JacksonXmlProperty(localName = "grace")
	private String grace;

	@JacksonXmlProperty(localName = "stem")
	private String stem;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String duration; 
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String type; 
	
	public void setGrace() { 
		this.grace = null;
	}

	public void setStem() { 
		this.stem = "none";
	}
	public void setDuration() { 
		this.duration = null;
	}
	//Default Constructor
	public BassGraceNote() {}


	public void removeNoteType() {
		this.type = null;
		
	}
	
}

