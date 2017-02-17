package application;

public class Target {
	
	private String name; 
	private String type;
	private String description;
	private LLA lla;
	
	
	
	
	
	
	
	public Target(String name, String type, String description, LLA LLA) {
		super();
		this.name = name;
		this.type = type;
		this.description = description;
		this.lla = LLA;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LLA getLLA() {
		return lla;
	}
	public void setPosLLA(LLA posLLA) {
		this.lla = posLLA;
	}
	public String toString(){
		return this.name;
	}
	
	
	
	
	
}
