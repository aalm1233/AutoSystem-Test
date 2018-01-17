package nwpu.autosysteamtest;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ElementConstrain {
	private String resourceId = null;
	private String paramName = null;
	private String elementName = null;
	private NodeList constraints = null;

	public ElementConstrain(String resourceId, String paramName, String elementName, Node restriction) {
		this.resourceId = resourceId;
		this.paramName = paramName;
		this.elementName = elementName;
		this.constraints = restriction.getChildNodes();
	}

	public String getResult() {
		String reslut = null;
		try {
			reslut = this.resourceId + "-" + this.paramName + "-" + this.elementName + "<"
					+ this.constrainAnalysis() + ">";
			return reslut;
		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
		}
		return reslut;
	}

	private String constrainAnalysis() {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < constraints.getLength(); i++) {
			try{
				Element constraint = (Element) constraints.item(i);
				String constraintname = constraint.getNodeName();
				String constraintt = constraint.getTextContent();
				result.append(constraintname + ":" + constraintt + "#");
			}catch (Exception e) {
				//System.err.println(e.getMessage());
			}
		}
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}
}
