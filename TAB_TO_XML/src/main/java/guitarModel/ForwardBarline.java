package guitarModel;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({ "bar-style", "repeat" })
public class ForwardBarline {

	@JacksonXmlProperty(isAttribute=true)
	private String location;

	@JacksonXmlProperty(localName = "bar-style")
	private String barStyle;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JacksonXmlProperty(localName = "repeat")
	private GuitarRepeat repeat;
	
	
	
	public ForwardBarline() {}
	public ForwardBarline(String location, String barStyle) {
		super();
		this.location = location;
		this.barStyle = barStyle;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBarStyle() {
		return barStyle;
	}

	public void setBarStyle(String barStyle) {
		this.barStyle = barStyle;
	}
	public void setRepeat(GuitarRepeat r ) { 
		this.repeat  = r; 
	}

}