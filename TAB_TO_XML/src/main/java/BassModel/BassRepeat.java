package BassModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class BassRepeat {
	@JacksonXmlProperty(isAttribute=true)
	private String direction;
	
	public void setDirection(String d) {
		
		this.direction = d; 
		
	}
}
