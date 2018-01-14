package nwpu.autosysteamtest;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ElementConstrain {
	private String resourceId;
	private String paramName;
	private String elementName;
	private NodeList constraints;
	
	public ElementConstrain(String resourceId,String paramName, String elementName, Node restriction) {
		this.resourceId = resourceId;
		this.paramName = paramName;
		this.elementName = elementName;
		this.constraints = restriction.getChildNodes();
	}
	
	public String getResult() {
		return this.resourceId+"-"+this.paramName+"-"+this.elementName+"<"+constrainAnalysis()+">";
	}
	
	private String constrainAnalysis(){
		StringBuffer result = new StringBuffer();
		for(int i = 0;i<constraints.getLength();i++){		
			Element constraint = (Element)constraints.item(i);
			String constraintname = constraint.getNodeName();
			String constraintt =  constraint.getTextContent();
			result.append(constraintname+":"+constraintt+"#");
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}
}
