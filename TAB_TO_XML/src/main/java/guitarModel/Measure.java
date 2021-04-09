package guitarModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"attributes", "barline", "direction", "note"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Measure {

	@JacksonXmlProperty(isAttribute=true)
	private int number;

	private Attributes attributes;
	
    @JacksonXmlElementWrapper(useWrapping = false)
	private ArrayList<Note> note;
    
    @JacksonXmlElementWrapper(useWrapping = false)
	private ArrayList<Barline> barline;

    @JacksonXmlProperty(localName = "direction")
	private GuitarDirection direction;
    
	public Measure() {}
	public Measure(int number, ArrayList<Note> note, Attributes attributes, ArrayList<Barline> barline) {
		super();
		this.number = number;
		this.note = note;
		this.attributes = attributes;
		this.barline = barline;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public ArrayList<Note> getNote() {
		return note;
	}
	public void setNote(ArrayList<Note> note) {
		this.note = note;
	}
	public Attributes getAttributes() {
		return attributes;
	}
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}
	public 	ArrayList<Barline> getBarline() {
		return barline;
	}
	public void setBarline(ArrayList<Barline> barline) {
		this.barline = barline;
	}
	
	public void setGuitarDirection(GuitarDirection dir) {
		
		this.direction = dir;
		
	}

	

}
