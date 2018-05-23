package nwpu.autosysteamtest.enity;

import java.util.ArrayList;
/**
 * 
 * @author Dengtong
 * @version 1.0,28/01/2018
 */
public class ResponseElement {
	ArrayList<ResponseElement> elements = null;
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
	
	public String getName() {
		return name;
	}
	public String getAttribute() {
		return attribute;
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
