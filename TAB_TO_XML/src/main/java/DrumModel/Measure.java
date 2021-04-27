package DrumModel;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import guitarModel.BackwardBarline;
import guitarModel.ForwardBarline;
import guitarModel.GuitarDirection;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "number", "attributes", "forwardBarline", "direction", "note", "backup", "noteBack", "backwardBarline" })
public class Measure {
	@JacksonXmlProperty(isAttribute = true)
	int number;

	Attributes attributes;

	@JacksonXmlElementWrapper(useWrapping = false)
	ArrayList<Object> note;
	Backup backup;
	
	@JacksonXmlProperty(localName = "direction")
	private GuitarDirection direction;

	@JacksonXmlProperty(localName = "barline")
	private ForwardBarline forwardBarline;
	
	private BackwardBarline backwardBarline;

	@JacksonXmlElementWrapper(useWrapping = false)
	ArrayList<Object> noteBack;

	public ArrayList<Object> getNoteBack() {
		return noteBack;
	}

	public void setNoteBack(ArrayList<Object> noteBack) {
		this.noteBack = noteBack;
	}

	public Backup getBackup() {
		return backup;
	}

	public void setBackup(Backup backup) {
		this.backup = backup;
	}

	public Measure() {
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ArrayList<Object> getNote() {
		return note;
	}

	public void setNote(ArrayList<Object> note) {
		this.note = note;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
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
	
	public GuitarDirection getDirection() {
		return direction;
	}
	public void setDirection(GuitarDirection direction) {
		this.direction = direction;
	}
}
