package guitarModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GuitarRepeat {
	
	@JacksonXmlProperty(isAttribute=true)
	private String direction;
	
	public void setDirection(String d) {
		
		this.direction = d; 
		
	}
}
