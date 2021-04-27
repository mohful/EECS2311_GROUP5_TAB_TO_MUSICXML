package guitarModel;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({"harmonic", "pulloff", "hammer", "string", "fret"})
public class Technical {
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	protected String string;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	protected String fret;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JacksonXmlProperty(localName = "hammer-on")
	@JacksonXmlElementWrapper(useWrapping = false)
	protected ArrayList<HammerOn> hammer; 
	
	/*
	 * This has to be an array list because for some reason some notes have
	 * 2 pull offs and I'm not sure how much more it can have
	 * 
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "pull-off")
	protected ArrayList<PullOff> pulloff;
	
	

	
	public ArrayList<HammerOn> getHammer() {
		
		return hammer;
		
	}
	
	public void setHammer(ArrayList<HammerOn> h) { 
		
		hammer = h;
		
	}



	public Technical() {}
	public Technical(String string, String fret) {
		super();
		this.string = string;
		this.fret = fret;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public String getFret() {
		return fret;
	}
	public void setFret(String fret) {
		this.fret = fret;
	}

	public void setPull(ArrayList<PullOff> pullList) {
		pulloff = pullList;
		
	}


}
