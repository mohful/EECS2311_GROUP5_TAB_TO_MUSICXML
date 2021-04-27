package DrumModel;

public class RestNote {
	Rest rest;
	String duration; 
	String voice; 
	String type; 
	
	public void setRest(Rest res) {
		rest = res;
	}
	
	public Rest getRest() {
		return rest;
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
	
	public RestNote() {}
}
