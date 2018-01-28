package nwpu.autosysteamtest.enity;

import java.util.ArrayList;

public class ResponseElement {
	ArrayList<ResponseElement> elements;
	String name;
	String attribute;
	int level;
	
	public ResponseElement(String name,String attribute,int level){
		this.name = name;
		this.attribute = attribute;
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
