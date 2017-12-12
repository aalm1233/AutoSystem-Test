package nwpu.autosysteamtest;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author Dengtong
 *
 */
public class ParameterConstrain {
	private String resourceId;
	private String paramName;
	private NodeList constraints;

	public ParameterConstrain(String attribute, String attribute2, Node restriction) {
		this.resourceId = attribute;
		this.paramName = attribute2;
		this.constraints = restriction.getChildNodes();
	}

	public String getResult() {
		return this.resourceId+"-"+this.paramName+"<"+constrainAnalysis()+">";
	}
	private String constrainAnalysis(){
		StringBuffer result = new StringBuffer();
		for(int i = 0;i<constraints.getLength();i++){		
			Element constraint = (Element)constraints.item(i);
			String constraintname = constraint.getNodeName();
			String constraintt = constraint.getTextContent();
			result.append(constraintname+":"+constraintt+"#");
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}

}
enum Constraint{
	//所有的约束
	enumeration,totalDigits,fractionDigit,minExclusive,maxExclusive,minInclusive,maxInclusive,length,minLength,maxLength,minDate,maxDate,pattern,format,minSize,maxSize,whiteSpace
}