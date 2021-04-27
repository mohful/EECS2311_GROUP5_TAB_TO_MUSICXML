package BassModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class BassSlide {
	
	@JacksonXmlProperty(isAttribute=true)
	private int number;
	
	@JacksonXmlProperty(isAttribute=true)
	private String type; 
	
	
	public void setNumber(int number) { 
		
		this.number = number; 
		
	}
	
	public int getNumber() { 
		return number; 
		
	}
	

	
	public void setType(String t) { 
		
		type = t; 
		
	}
	
	public String getType() { 
		return type;
		
	}
}
