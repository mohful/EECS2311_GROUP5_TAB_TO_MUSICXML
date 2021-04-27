package guitarModel;

public class Clef {
	private String sign;
	private String line;
	
	public Clef() {}
	public Clef(String sign, String line) {
		super();
		this.sign = sign;
		this.line = line;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
}
