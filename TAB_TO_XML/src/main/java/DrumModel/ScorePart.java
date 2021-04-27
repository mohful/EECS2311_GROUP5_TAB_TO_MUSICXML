package DrumModel;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;

@JsonPropertyOrder({"partName", "instruments"})
public class ScorePart {
	@JacksonXmlProperty(isAttribute=true)
	String id;

	@JacksonXmlProperty(localName = "part-name")
	String partName; 
	
	@JacksonXmlProperty(localName = "score-instrument")
    @JacksonXmlElementWrapper(useWrapping = false)
	ArrayList<ScoreInstrument> scoreInstruments;
	

	public ScorePart() {}
	
	public void setScoreInstruments(ArrayList<ScoreInstrument> scoreInstruments) { 
		
		this.scoreInstruments = scoreInstruments;
		
	}
	
	public ArrayList<ScoreInstrument> getScoreInstruments() { 
		
		return this.scoreInstruments;
	}
	/**
	 * <p> This a constructor specifically for drums </p>
	 * @param id
	 * @param partName
	 * @param instruments
	 */
	public ScorePart(String id, String partName, ArrayList<ScoreInstrument> scoreInstruments) { 
		
		super(); 

		this.id = id; 
		this.partName = partName; 
		this.scoreInstruments = scoreInstruments;
		
		
	}
	public ScorePart(String id, String partName) {
		super();
		this.id = id;
		this.partName = partName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}
}

