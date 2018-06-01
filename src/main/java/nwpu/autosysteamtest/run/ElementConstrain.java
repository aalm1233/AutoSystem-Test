package nwpu.autosysteamtest.run;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author Dengtong
 * @version 1.0,28/01/2018
 */
public class ElementConstrain {
	private NodeList constraints = null;

	public ElementConstrain( Node restriction) {
		this.constraints = restriction.getChildNodes();
	}

	public ArrayList<String> getResult() {
		ArrayList<String> reslut = null;
		try {
			reslut = constrainAnalysis();
			return reslut;
		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
		}
		return reslut;
	}

	private ArrayList<String> constrainAnalysis() {
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < constraints.getLength(); i++) {
			try{
				Element constraint = (Element) constraints.item(i);
				String constraintname = constraint.getNodeName();
				String constraintt = constraint.getTextContent();
				result.add(constraintname + ":" + constraintt);
			}catch (Exception e) {
			}
		}
		return result;
	}
}
