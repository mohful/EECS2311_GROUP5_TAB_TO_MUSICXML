package guitarModel;

import java.util.ArrayList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Part {

	@JacksonXmlProperty(isAttribute = true)
	private String id;

	@JacksonXmlProperty(localName = "measure")
    @JacksonXmlElementWrapper(useWrapping = false)
	private ArrayList<Measure> measures;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<Measure> getMeasures() {
		return measures;
	}
	public void setMeasures(ArrayList<Measure> measures) {
		this.measures = measures;
	}

	public Part() {}
	public Part(String id, ArrayList<Measure> measures) {
		super();
		this.id = id;
		this.measures = measures;
	}

}
