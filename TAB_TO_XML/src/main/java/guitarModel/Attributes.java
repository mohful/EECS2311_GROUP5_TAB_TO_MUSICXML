package guitarModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Attributes {

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private int divisions;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Key key;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Time time;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Clef clef;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JacksonXmlProperty(localName = "staff-details")
	private StaffDetails staffDetails;

	public int getDivisions() {
		return divisions;
	}

	public void setDivisions(int divisions) {
		this.divisions = divisions;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Clef getClef() {
		return clef;
	}

	public void setClef(Clef clef) {
		this.clef = clef;
	}

	public StaffDetails getStaffDetails() {
		return staffDetails;
	}

	public void setStaffDetails(StaffDetails staffDetails) {
		this.staffDetails = staffDetails;
	}

	public Attributes() {}
	public Attributes(int divisions, Key key, Time time, Clef clef, StaffDetails staffDetails) {
		super();
		this.divisions = divisions;
		this.key = key;
		this.time = time;
		this.clef = clef;
		this.staffDetails = staffDetails;
	}
}
