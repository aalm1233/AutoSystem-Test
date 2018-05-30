package nwpu.autosysteamtest.enity;

import java.util.ArrayList;
/**
 * 
 * @author Dengtong
 * @version 1.0,28/01/2018
 */
public class RequestElement extends RequestParam{
	String name;
	String attribute;
	String type;
	String location;
	int level;
	ArrayList<String> constraints;
	ArrayList<RequestElement> elements;
	public RequestElement(String name,String attribute,String type,String location,int level){
		super(name,attribute,type,location);
		this.level = level;
	}
	public int getLevel(){
		return level;
	}
	
	public boolean isObject(){
		if(elements == null){
			return false;
		}else{
			return true;
		}
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
	public void setName(String name) {
		this.name = name;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	
}

