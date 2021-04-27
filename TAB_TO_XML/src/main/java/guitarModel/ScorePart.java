package guitarModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ScorePart {
	@JacksonXmlProperty(isAttribute=true)
	private String id;

	@JacksonXmlProperty(localName = "part-name")
	private String partName; 

	public ScorePart() {}
	
	
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
