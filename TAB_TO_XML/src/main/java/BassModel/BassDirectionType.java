package BassModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;



public class BassDirectionType {
	@JacksonXmlProperty(localName = "words")
	private BassWords word;
	
	public void setWord(BassWords g) { 
		word = g;
		
	}
	
}
