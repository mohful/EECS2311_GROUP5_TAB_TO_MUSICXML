package BassModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;



public class BassDirection {
	
	@JacksonXmlProperty(isAttribute=true)
	private String placement;
	
	@JacksonXmlProperty(localName = "direction-type")
	private BassDirectionType type;
	
	public void setPlacement(String p) { 
		placement = p;
	}
	
	public void setDirectionType(BassDirectionType t) { 
		type = t;
		
	}
}

