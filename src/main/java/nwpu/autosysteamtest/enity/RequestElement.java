package nwpu.autosysteamtest.enity;

import java.util.ArrayList;

public class RequestElement {
	ArrayList<RequestElement> elements;
	String name;
	String attribute;
	String type;
	String location;
	int level;
	ArrayList<String> constraints;
	public RequestElement(String name,String attribute,String type,String location,int level){
		this.name = name;
		this.attribute =attribute;
		this.type = type;
		this.location = location;
		if("".equals(this.location)){
			this.location = "false";
		}
		this.level = level;
		elements = null;
		constraints = null;
	}
	public int getLevel(){
		return level;
	}
	
	public ArrayList<RequestElement> getElements() {
		return elements;
	}
	public void addElement(RequestElement element) {
		if(elements == null){
			elements = new ArrayList<>();
			elements.add(element);
		}else{
			elements.add(element);
		}
	}
	public ArrayList<String> getConstraints() {
		return constraints;
	}
	public void setConstraints(ArrayList<String> constraints) {
		this.constraints = constraints;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	
}

