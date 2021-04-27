
package guitarModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class PullOff {
	
	@JacksonXmlProperty(isAttribute=true)
	private int number;
	
	@JacksonXmlProperty(isAttribute=true)
	private String type; 
	
	// This field stores the value associated with the pull off..
	//usually a p or P
	@JacksonXmlText
	private String symbol;
	
	public int getNumber() {
		
		return number; 
		
	}
	
	public void setNumber(int num) { 
		
		number = num;
		
	}
	
	public String getType() { 
		
		return type; 
		
	}
	
	public void setType(String type) { 
		
		this.type = type; 
		
	}
	
	public void setSymbol(String sym) { 
		
		symbol = sym; 
		
	}
	
	public String getSymbol() { 
		
		return symbol;
		
	}
}