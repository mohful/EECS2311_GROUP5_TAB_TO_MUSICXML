package DrumModel;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({"chord", "unpitched", "duration", "instrument", "voice", "type", "stem", "notehead"})
public class ChordNoteNH extends Note{
	@JacksonXmlProperty(localName = "chord")
	String chord;
	
	Unpitched unpitched;
	String duration; 
	Instrument instrument;
	String voice; 
	String type; 
	String stem;
	String notehead;
	
	public void setChord() { 
		   this.chord = null;
		   
	}
	
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
	
	public ChordNoteNH() {}
	
	
}
