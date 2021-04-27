package guitarModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GuitarDirection {
	
	@JacksonXmlProperty(isAttribute=true)
	private String placement;
	
	@JacksonXmlProperty(localName = "direction-type")
	private GuitarDirectionType type;
	
	public void setPlacement(String p) { 
		placement = p;
	}
	
	public void setDirectionType(GuitarDirectionType t) { 
		type = t;
		
	}
}
