package nwpu.autosysteamtest.enity;

import java.util.ArrayList;

public class RequestParam {
	String name;
	String attribute;
	String type;
	String location;
	ArrayList<String> constraints;
	ArrayList<RequestElement> elements;
	public RequestParam(String name,String attribute,String type,String location){
		this.name = name;
		this.attribute = attribute;
		this.type = type;
		this.location = location;
		if("".equals(this.location)){
			this.location = "false";
		}
		constraints = null;
		elements = null;
	}
	public String getName() {
		return name;
	}
	public String getAttribute() {
		return attribute;
	}
	public String getType() {
		return type;
	}
	public String getLocation() {
		return location;
	}
	public ArrayList<String> getConstraints() {
		return constraints;
	}
	public void setConstraints(ArrayList<String> constraints) {
		this.constraints = constraints;
	}
	public ArrayList<RequestElement> getElements() {
		return elements;
	}
	public void setElements(ArrayList<RequestElement> elements) {
		this.elements = elements;
	}
	
}