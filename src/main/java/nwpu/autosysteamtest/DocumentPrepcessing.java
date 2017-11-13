package nwpu.autosysteamtest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * 
 * @author Dengtong
 * @version 1.1,06/11/2017
 *
 */
public class DocumentPrepcessing {
	private ConcurrentHashMap<String, String> operaterTypesMap;
	private ConcurrentHashMap<String, ArrayList<String>> addInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> deleteInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> updateInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> findInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> parameterConstrainsMap;
	private StringBuffer operaterTypes;
	private ArrayList<String> premise;
	private ArrayList<String> parameterConstrainsSet;
	protected Document doc;
	File[] fileSet;

	public DocumentPrepcessing(File[] fileSet) {
		super();
		this.fileSet = fileSet;
		operaterTypesMap = new ConcurrentHashMap<String, String>();
		addInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		deleteInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		updateInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		findInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		parameterConstrainsMap = new ConcurrentHashMap<String, ArrayList<String>>();
		premise = new ArrayList<String>();
	}

	public void run() {
		for (File file : fileSet) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder bulider;
			ArrayList<String> addInterfaceSet = new ArrayList<String>();
			ArrayList<String> deleteInterfaceSet = new ArrayList<String>();
			ArrayList<String> updateInterfaceSet = new ArrayList<String>();
			ArrayList<String> findInterfaceSet = new ArrayList<String>();
			parameterConstrainsSet = new ArrayList<String>();
			operaterTypes = new StringBuffer();
			try {
				bulider = factory.newDocumentBuilder();
				doc = bulider.parse(file);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Element root = doc.getDocumentElement();
			if (root.hasAttribute(ResourcesAttribute.premise.toString())) {
				premise.add(root.getAttribute(ResourcesAttribute.id.toString()) + "_"
						+ root.getAttribute(ResourcesAttribute.premise.toString()));
			}
			NodeList addNodeList = root.getElementsByTagName(Operation.add.toString());
			NodeList deleteNodeList = root.getElementsByTagName(Operation.delete.toString());
			NodeList updateNodeList = root.getElementsByTagName(Operation.update.toString());
			NodeList findNodeList = root.getElementsByTagName(Operation.find.toString());
			if (addNodeList.getLength() == 1) {
				initInterfaceSetMap(addNodeList.item(0), addInterfaceSet);
				addInterfaceSetMap.put(root.getAttribute(ResourcesAttribute.id.toString()), addInterfaceSet);
			}
			if (deleteNodeList.getLength() == 1) {
				initInterfaceSetMap(deleteNodeList.item(0), deleteInterfaceSet);
				deleteInterfaceSetMap.put(root.getAttribute(ResourcesAttribute.id.toString()), deleteInterfaceSet);
			}
			if (updateNodeList.getLength() == 1) {
				initInterfaceSetMap(updateNodeList.item(0), updateInterfaceSet);
				updateInterfaceSetMap.put(root.getAttribute(ResourcesAttribute.id.toString()), updateInterfaceSet);
			}
			if (findNodeList.getLength() == 1) {
				initInterfaceSetMap(findNodeList.item(0), findInterfaceSet);
				findInterfaceSetMap.put(root.getAttribute(ResourcesAttribute.id.toString()), findInterfaceSet);
			}
			operaterTypesMap.put(root.getAttribute(ResourcesAttribute.id.toString()), operaterTypes.toString());
		}

	}

	public ConcurrentHashMap<String, String> getOperaterTypesMap() {
		return operaterTypesMap;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getAddInterfaceSetMap() {
		return addInterfaceSetMap;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getDeleteInterfaceSetMap() {
		return deleteInterfaceSetMap;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getUpdateInterfaceSetMap() {
		return updateInterfaceSetMap;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getFindInterfaceSetMap() {
		return findInterfaceSetMap;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getParameterConstrainsMap() {
		return parameterConstrainsMap;
	}

	public ArrayList<String> getPremise() {
		return premise;
	}

	private void initInterfaceSetMap(Node node, ArrayList<String> xInteInterfaceSet) {
		operaterTypes.append("<"+node.getNodeName()+">");
		NodeList resourceList = node.getChildNodes();
		int ntv = resourceList.getLength();
		for (int i = 0; i < ntv; i++) {
			if(resourceList.item(i) instanceof Element){
				Element resource = (Element)resourceList.item(i);
				Element request = (Element)resource.getChildNodes().item(0);
				Element response = (Element)resource.getChildNodes().item(1);
				Element data = (Element) response.getChildNodes().item(0);
				NodeList params = request.getElementsByTagName(Param.param.toString());
				StringBuffer xInteInterface = new StringBuffer(resource.getAttribute(ResourceAttribute.id.toString()) + "_"
						+ data.getAttribute(DataAttribute.type.toString())+"_");
				for (int j = 0; j < params.getLength(); j++) {
					Element param = (Element) params.item(j);
					xInteInterface.append(param.getAttribute(ParamAttribute.name.toString()) + ","
							+ param.getAttribute(ParamAttribute.type.toString()));
					NodeList restrictions = param.getElementsByTagName(ParamElement.restriction.toString());
					if (restrictions.getLength() == 1) {
						Node restriction = restrictions.item(0);
						ParameterConstrain constrain = new ParameterConstrain(resource.getAttribute(ResourcesAttribute.id.toString()),param.getAttribute(ParamAttribute.name.toString()),restriction);
						String parameterConstrains = constrain.getResult();
						parameterConstrainsSet.add(parameterConstrains);
					}
					if(j<params.getLength()-1){
						xInteInterface.append("_");
					}
				}
				Element root = (Element) node.getParentNode();
				parameterConstrainsMap.put(root.getAttribute(ResourcesAttribute.id.toString()), parameterConstrainsSet);
				System.out.println(xInteInterface.toString());
				xInteInterfaceSet.add(xInteInterface.toString());
			}
		}
	}

}

enum ResourcesAttribute {
	// 每一个实体的属性
	id, name, base, premise
}

enum Operation {
	// 操作类型
	add, delete, update, find
}

enum ResourceAttribute {
	// 每一个操作的属性
	id, name, path, cascade
}

enum DataAttribute {
	// 返回数据类型
	name, type
}

enum Param {
	// 参数
	param
}

enum ParamAttribute {
	// 参数属性
	name, attribute, type, required
}

enum ParamElement {
	// param的子节点
	restriction, element
}