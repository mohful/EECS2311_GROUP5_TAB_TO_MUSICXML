package guitarModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Harmonic {

	@JacksonXmlProperty(localName = "natural")
	private String natural;
	
	public void setNatural() { 
		this.natural = null;
	}
}
