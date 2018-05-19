package nwpu.autosysteamtest;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author Dengtong
 * @version 1.0,17/12/2017
 */
public class ParameterConstrain {
	private NodeList constraints;

	public ParameterConstrain(Node restriction) {
		this.constraints = restriction.getChildNodes();
	}

	public ArrayList<String> getResult() {
		return constrainAnalysis();
	}
	private ArrayList<String> constrainAnalysis(){
		ArrayList<String> result = new ArrayList<>();
		for(int i = 0;i<constraints.getLength();i++){
			try{
				Element constraint = (Element)constraints.item(i);
				String constraintname = constraint.getNodeName();
				String constraintt =  constraint.getTextContent();
				result.add(constraintname+":"+constraintt);
			}catch (Exception e) {
				System.err.println(e.getMessage());
			}			
		}
		return result;
	}

}
enum Constraint{
	//枚举类型
	enumeration,totalDigits,fractionDigit,minExclusive,maxExclusive,minInclusive,maxInclusive,length,minLength,maxLength,minDate,maxDate,pattern,format,minSize,maxSize,whiteSpace
}