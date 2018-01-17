package nwpu.autosysteamtest;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author Dengtong
 * @version 1.0,17/12/2017
 */
public class ParameterConstrain {
	private String resourceId;
	private String name;
	private NodeList constraints;

	public ParameterConstrain(String resourceId, String paramName, Node restriction) {
		this.resourceId = resourceId;
		this.name = paramName;
		this.constraints = restriction.getChildNodes();
	}

	public String getResult() {
		return this.resourceId+"-"+this.name+"<"+constrainAnalysis()+">";
	}
	private String constrainAnalysis(){
		StringBuffer result = new StringBuffer();
		for(int i = 0;i<constraints.getLength();i++){
			try{
				Element constraint = (Element)constraints.item(i);
				String constraintname = constraint.getNodeName();
				String constraintt =  constraint.getTextContent();
				result.append(constraintname+":"+constraintt+"#");
			}catch (Exception e) {
				System.err.println(e.getMessage());
			}			
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}

}
enum Constraint{
	//所有的约束
	enumeration,totalDigits,fractionDigit,minExclusive,maxExclusive,minInclusive,maxInclusive,length,minLength,maxLength,minDate,maxDate,pattern,format,minSize,maxSize,whiteSpace
}