package guitarModel;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({"step", "alter", "octave"})
public class AlteredPitch extends Pitch {
	
	@JacksonXmlProperty(localName = "alter")
	private String alter;

	public void setAlter(String alter) { 
		this.alter = alter; 
		
	}
	
	public String getAlter() { 
		
		return this.alter;
		
	}
}
