package BassModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class BassHarmonic {

	@JacksonXmlProperty(localName = "natural")
	private String natural;
	
	public void setNatural() { 
		this.natural = null;
	}
}
