package DrumModel;


//import guitarModel.Clef;
//import guitarModel.Key;
import guitarModel.StaffDetails;


public class Attributes {
	int divisions;

	Key key;
	Time time;
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
