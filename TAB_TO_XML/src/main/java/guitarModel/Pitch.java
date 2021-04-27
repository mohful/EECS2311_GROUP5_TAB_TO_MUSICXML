package guitarModel;

public class Pitch {
	private String step;
	private String octave;
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getOctave() {
		return octave;
	}
	public void setOctave(String octave) {
		this.octave = octave;
	}

	public Pitch() {}
	public Pitch(String step, String octave) {
		super();
		this.step = step;
		this.octave = octave;
	}
}
