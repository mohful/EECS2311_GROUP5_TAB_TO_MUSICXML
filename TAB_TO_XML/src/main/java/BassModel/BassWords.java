package BassModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class BassWords {
	@JacksonXmlProperty(isAttribute = true, localName = "relative-x")
	private double relativex;
	
	@JacksonXmlProperty(isAttribute = true, localName = "relative-y")
	private double relativey;
	
	@JacksonXmlText
	private String repeatText; 
	
	
	public void setRelativeX() { 
		this.relativex = 256.17;
	}
	
	public void setRelativeY() { 
		this.relativey = 256.17;
	}
	public void setRepeatText(int number) { 
		this.repeatText = "Repeat " + number + " times";
	}
}
