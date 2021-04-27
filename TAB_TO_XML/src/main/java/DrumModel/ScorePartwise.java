package DrumModel;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName="score-partwise")
public class ScorePartwise {

	@JacksonXmlProperty(isAttribute = true)

	public String version;
	
	@JacksonXmlProperty(localName = "part-list")

	public PartList partList;
	
    @JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "part")
	public ArrayList<Part> parts;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public PartList getPartList() {
		return partList;
	}
	public void setPartList(PartList partList) {
		this.partList = partList;
	}
	public ArrayList<Part> getParts() {
		return parts;
	}
	public void setParts(ArrayList<Part> parts) {
		this.parts = parts;
	}
}
