package guitarModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StaffDetails {
	@JacksonXmlProperty(localName = "staff-lines")
	private String staffLines;

	@JacksonXmlProperty(localName = "staff-tuning")
	@JacksonXmlElementWrapper(useWrapping = false)
	private ArrayList<StaffTuning> staffTunings;

	public String getStaffLines() {
		return staffLines;
	}

	public void setStaffLines(String staffLines) {
		this.staffLines = staffLines;
	}

	public ArrayList<StaffTuning> getStaffTunings() {
		return staffTunings;
	}

	public void setStaffTunings(ArrayList<StaffTuning> staffTunings) {
		this.staffTunings = staffTunings;
	}

	public StaffDetails() {}
	public StaffDetails(String staffLines, ArrayList<StaffTuning> staffTunings) {
		super();
		this.staffLines = staffLines;
		this.staffTunings = staffTunings;
	}
}
