package DrumModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class Beam {
	
	@JacksonXmlProperty(isAttribute=true)
	String number;
	
	@JacksonXmlText
	String value;
	
	public Beam() {}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setNumber(String number) { 
		
		this.number = number; 
		
	}
	
	public String getNumber() { 
		
		return number;
	}
}
