package BassModel;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import guitarModel.Slides;
import guitarModel.Slur;

@JsonPropertyOrder({"slide", "technical", "slur"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Notations {
	@JacksonXmlProperty(localName = "slide")
	@JacksonXmlElementWrapper(useWrapping = false)
	private ArrayList<BassSlide> slides;
	
	Technical technical;
	
	BassSlur slur;
	
	public BassSlur getSlur() { 
		
		return slur; 
		
	}
	
	public void setSlur(BassSlur s) { 
		
		slur = s; 
		
	}
	public void setSlides(ArrayList<BassSlide> s) { 
		this.slides = s;
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
