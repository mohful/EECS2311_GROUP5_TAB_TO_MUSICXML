package guitarModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GuitarDirectionType {
	
	@JacksonXmlProperty(localName = "words")
	private GuitarWords word;
	
	public void setWord(GuitarWords g) { 
		word = g;
	}
}
