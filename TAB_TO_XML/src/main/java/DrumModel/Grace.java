package DrumModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Grace {

	@JacksonXmlProperty(isAttribute=true)
	String slash;
	
	public void setSlash(String slash) { 
		this.slash = slash;
	}
}
