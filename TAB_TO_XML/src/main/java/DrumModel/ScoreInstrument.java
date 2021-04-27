package DrumModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ScoreInstrument {

	@JacksonXmlProperty(isAttribute=true)
	String id;
	
	
	@JacksonXmlProperty(localName = "instrument-name")
	String instrumentName; 
	
	public void setID(String id) { 
		
		this.id = id; 
		
	}
	
	public String getID() { 
		
		return this.id;
		
	}
	
	public void setInstrumentName(String name) { 
		
		instrumentName = name;
	}
	
	public String getInstrumentName() { 
		
		return instrumentName;
		
	}
}
