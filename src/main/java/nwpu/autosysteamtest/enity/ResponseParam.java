package nwpu.autosysteamtest.enity;

import java.util.ArrayList;

public class ResponseParam {
	String name;
	String attribute;
	ArrayList<ResponseElement> elements;
	public ResponseParam(String name,String attribute){
		this.name = name;
		this.attribute = attribute;
	}
	public ArrayList<ResponseElement> getElements() {
		return elements;
	}
	public void setElements(ArrayList<ResponseElement> elements) {
		this.elements = elements;
	}
}
