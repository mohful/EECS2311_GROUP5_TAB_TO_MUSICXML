package DrumModel;


import com.fasterxml.jackson.annotation.JsonInclude;



public class Attributes {
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	int divisions;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	Key key;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	Time time;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	Clef clef;

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


	public Attributes() {}
	
}
