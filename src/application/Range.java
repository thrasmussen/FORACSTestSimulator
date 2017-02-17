package application;

import java.util.ArrayList;

public class Range {
	private ArrayList<Target> targets = new ArrayList<Target>();

	public Range() {
		super();
	}

	public Range(ArrayList<Target> targets) {
		super();
		this.targets = targets;
	}

	public ArrayList<Target> getTargets() {
		return targets;
	}

	public void setTargets(ArrayList<Target> targets) {
		this.targets = targets;
	}

	@Override
	public String toString() {
		return "Range [targets=" + targets.toString() + "]";
	}
	
	public void saveRange(){
		//Not implemented
	}
	public void loadRange(){
		//Not implemented
	}
	public Target addTarget(Target t){
		
		targets.add(t);
		return t;
		
	}
	
	public Target findTarget(String name){
		for(Target t : targets){
			if(t.getName() == name) {
				return t;
			}
			
		}
		return null;
	}

}
