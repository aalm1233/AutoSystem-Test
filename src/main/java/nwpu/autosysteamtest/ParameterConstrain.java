package nwpu.autosysteamtest;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author Dengtong
 *
 */
public class ParameterConstrain {
	private String resourceAttributeId;
	private String paramName;
	private NodeList constraint;

	public ParameterConstrain(String attribute, String attribute2, Node restriction) {
		this.resourceAttributeId = attribute;
		this.paramName = attribute2;
		this.constraint = restriction.getChildNodes();
	}

	public String getResult() {
		return this.resourceAttributeId+"_"+this.paramName+"_"+constrainAnalysis();
	}
	private String constrainAnalysis(){
		StringBuffer result = new StringBuffer();
		return result.toString();
	}

}
