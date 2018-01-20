package nwpu.autosysteamtest.enity;

import java.util.ArrayList;

public class ResponseElement {
	ArrayList<ResponseElement> elements;
	String name;
	String attribute;
	String type;
	int level;
	String[] constraint;
	
	public ResponseElement(String name,String attribute,String type,int level){
		this.name = name;
		this.attribute = attribute;
		this.type = type;
		this.level = level;
	}
	public int getLevel(){
		return level;
	}
	
	public ArrayList<ResponseElement> getElements() {
		return elements;
	}
	public void addElement(ResponseElement element) {
		if(elements == null){
			elements = new ArrayList<>();
			elements.add(element);
		}else{
			elements.add(element);
		}
	}
}
