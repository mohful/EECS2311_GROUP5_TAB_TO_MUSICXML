package BassModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({"chord", "pitch", "duration", "voice", "type", "notations"})
public class ChordNote extends Note  {
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JacksonXmlProperty(localName = "chord")
	String chord;
	Pitch pitch;
	String duration;
	String voice;
	String type;
	Notations notations;
	
	
	 public void setChord() { 
		   this.chord = null;
	   }
	 public Pitch getPitch() {
			return pitch;
		}
		public void setPitch(Pitch pitch) {
			this.pitch = pitch;
		}
		public String getDuration() {
			return duration;
		}
		public void setDuration(String duration) {
			this.duration = duration;
		}
		public String getVoice() {
			return voice;
		}
		public void setVoice(String voice) {
			this.voice = voice;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Notations getNotations() {
			return notations;
		}
		public void setNotations(Notations notations) {
			this.notations = notations;
		}

		
		public ChordNote() {}
		public ChordNote(Pitch pitch, String duration, String voice, String type, Notations notations) {
			super();
			this.pitch = pitch;
			this.duration = duration;
			this.voice = voice;
			this.type = type;
			this.notations = notations;
		}
}

