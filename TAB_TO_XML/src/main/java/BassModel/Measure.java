package BassModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"attributes", "forwardBarline", "direction", "note", "backwardBarline"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Measure {

	@JacksonXmlProperty(isAttribute=true)
	private int number;

	@JacksonXmlProperty(localName = "attributes")
	private Attributes attributes;
	
	@JacksonXmlProperty(localName = "direction")
	private BassDirection direction;
	 
    @JacksonXmlElementWrapper(useWrapping = false)
	private ArrayList<Note> note;
    
    @JacksonXmlProperty(localName = "barline")
	private ForwardBarline forwardBarline;
    
    private BackwardBarline backwardBarline;


	public Measure() {}
	public Measure(int number, ArrayList<Note> note, Attributes attributes, ForwardBarline barline) {
		super();
		this.number = number;
		this.note = note;
		this.attributes = attributes;
		this.forwardBarline = barline;
	}
    
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public Attributes getAttributes() {
		return attributes;
	}
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}
	public BassDirection getDirection() {
		return direction;
	}
	public void setDirection(BassDirection direction) {
		this.direction = direction;
	}
	public ArrayList<Note> getNote() {
		return note;
	}
	public void setNote(ArrayList<Note> note) {
		this.note = note;
	}
	public ForwardBarline getForwardBarline() {
		return forwardBarline;
	}
	public void setForwardBarline(ForwardBarline forwardBarline) {
		this.forwardBarline = forwardBarline;
	}
	public BackwardBarline getBackwardBarline() {
		return backwardBarline;
	}
	public void setBackwardBarline(BackwardBarline backwardBarline) {
		this.backwardBarline = backwardBarline;
	}


}
