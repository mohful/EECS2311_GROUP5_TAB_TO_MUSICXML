package guitarModel;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({"slide", "technical", "slur"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Notations {
	
	@JacksonXmlProperty(localName = "slide")
	@JacksonXmlElementWrapper(useWrapping = false)
	private ArrayList<Slides> slides;
	
	
	private Technical technical;
	
	private Slur slur;
	
	public Slur getSlur() { 
		
		return slur; 
		
	}
	public void setSlides(ArrayList<Slides> s) { 
		this.slides = s;
	}
	public void setSlur(Slur s) { 
		
		slur = s; 
		
	}
	
	public Notations() {}
	public Notations(Technical technical) {
		super();
		this.technical = technical;
	}

	public Technical getTechnical() {
		return technical;
	}

	public void setTechnical(Technical technical) {
		this.technical = technical;
	}
	

}
