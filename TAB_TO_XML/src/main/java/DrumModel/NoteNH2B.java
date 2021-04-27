package DrumModel;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({"unpitched", "duration", "instrument", "voice", "type", "stem", "notehead", "beam[0]", "beam[1]"})
public class NoteNH2B extends Note{
	@JacksonXmlProperty(isAttribute=true)
	String location1;
	
	@JacksonXmlProperty(localName = "beam")
	@JacksonXmlElementWrapper(useWrapping = false)
	Beam[] beam = new Beam[2];
	
	@JacksonXmlProperty(isAttribute=true)
	String location2;
	
	Unpitched unpitched;
	String duration; 
	Instrument instrument;
	String voice; 
	String type; 
	String stem;
	String notehead;
	
	public void setUnpitched(Unpitched pitch) { 
		unpitched = pitch;
		
	}
	
	public Unpitched getUnpitched() { 
		
		return unpitched;
		
	}
	
	public void setDuration(String duration) { 
		
		this.duration = duration; 
		
	}
	
	public String getDuration() { 
		
		return duration; 
		
	}
	
	public void setVoice(String voice) { 
		
		this.voice = voice;  
		
	}
	
	public String getVoice() { 
		
		return voice; 
		
	}
	
	public void setType(String type) { 
		
		this.type = type; 
		
	
	}
	
	public String getType() { 
		
		return type;
		
	}
	
	public void setStem(String stem) { 
		
		this.stem = stem; 
		
	}
	
	public String getStem() { 
		
		return stem;
		
	}
	
	public void setInstrument(Instrument instrument) { 
		this.instrument = instrument;
		
	}
	public Instrument getInstrument() { 
		
		return instrument; 
		
	}
	
	public void setNotehead(String notehead) {
		this.notehead = notehead;
	}
	
	public String getNotehead() {
		return notehead;
	}
	
	public void setBeam(Beam beam, int beamnum) {
		this.beam[beamnum] = beam;
	}
	
	public Beam[] getBeam() {
		return beam;
	}
	
	public void setLocation1(String location) {
		this.location1 = location;
	}
	
	public String getLocation1() {
		return location1;
	}
	
	public void setLocation2(String location) {
		this.location2 = location;
	}
	
	public String setLocation2() {
		return location2;
	}
	
	public NoteNH2B() {}

}
