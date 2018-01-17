package nwpu.autosysteamtest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Dengtong
 * @version 3.1,14/01/2018
 *
 */
public class DocumentPrepcessing {
	private volatile static DocumentPrepcessing documentPrepcessing;
	private ConcurrentHashMap<String, String> operaterTypesMap;
	private ConcurrentHashMap<String, ArrayList<String>> addInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> deleteInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> updateInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> findInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> parameterConstrainsMap;
	private ConcurrentHashMap<String, ArrayList<String>> elementConstrainsMap;
	File[] fileSet;

	public static DocumentPrepcessing getInstance() throws InterruptedException {
		if (documentPrepcessing == null) {
			synchronized (DocumentPrepcessing.class) {
				if (documentPrepcessing == null) {
					documentPrepcessing = new DocumentPrepcessing();
				}
			}
		}
		return documentPrepcessing;
	}

	public static DocumentPrepcessing getInstance(File[] fileSet) throws InterruptedException, FileNotFoundException {
		if (documentPrepcessing == null) {
			synchronized (DocumentPrepcessing.class) {
				if (documentPrepcessing == null) {
					documentPrepcessing = new DocumentPrepcessing(fileSet);
				}
			}
		}
		return documentPrepcessing;
	}

	private DocumentPrepcessing() {

	}

	public DocumentPrepcessing(File[] fileSet) throws InterruptedException, FileNotFoundException {
		super();
		this.fileSet = fileSet;
		operaterTypesMap = new ConcurrentHashMap<String, String>();
		addInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		deleteInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		updateInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		findInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		parameterConstrainsMap = new ConcurrentHashMap<String, ArrayList<String>>();
		elementConstrainsMap = new ConcurrentHashMap<String, ArrayList<String>>();
		run();
	}

	private void run() throws InterruptedException, FileNotFoundException {
		for (File file : fileSet) {
			if (!file.isDirectory()) {
				DocumentPrepcssingThread thread = new DocumentPrepcssingThread(file);
				new Thread(thread).start();
			}
		}
	}

	public ConcurrentHashMap<String, String> getOperaterTypesMap() {
		return operaterTypesMap;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getAddInterfaceSetMap() {
		return addInterfaceSetMap;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getElementConstrainsMap() {
		return elementConstrainsMap;
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

	public ConcurrentHashMap<String, ArrayList<String>> getXInteInterfaceSetMap(String type) {
		switch (type) {
		case "add":
			return addInterfaceSetMap;
		case "delete":
			return deleteInterfaceSetMap;
		case "update":
			return updateInterfaceSetMap;
		case "find":
			return findInterfaceSetMap;
		default:
			return null;
		}
	}

	class DocumentPrepcssingThread implements Runnable {
		private StringBuffer operaterTypes;
		private ArrayList<String> parameterConstrainsSet;
		private ArrayList<String> elementConstrainsSet;
		protected Document doc;
		File file;
		private PrintWriter out;

		public DocumentPrepcssingThread(File file) throws InterruptedException {
			this.file = file;
		}

		@Override
		public void run() {
			try {
				File dir = new File(file.getParentFile().getAbsolutePath() + "//log");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				out = new PrintWriter(new BufferedWriter(new FileWriter(dir.getAbsolutePath() + "//log.txt", true)));
				out.println("——————————————" + new Date() + "——————————————");
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			DocumentPrepcessing documentPrepcessing = null;
			try {
				documentPrepcessing = DocumentPrepcessing.getInstance();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder bulider;
			ArrayList<String> addInterfaceSet = new ArrayList<String>();
			ArrayList<String> deleteInterfaceSet = new ArrayList<String>();
			ArrayList<String> updateInterfaceSet = new ArrayList<String>();
			ArrayList<String> findInterfaceSet = new ArrayList<String>();
			parameterConstrainsSet = new ArrayList<String>();
			elementConstrainsSet = new ArrayList<>();
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
			out.flush();
			out.println("service name:" + root.getAttribute(ResourcesAttribute.id.toString()));
			NodeList addNodeList = root.getElementsByTagName(Operation.add.toString());
			NodeList deleteNodeList = root.getElementsByTagName(Operation.delete.toString());
			NodeList updateNodeList = root.getElementsByTagName(Operation.update.toString());
			NodeList findNodeList = root.getElementsByTagName(Operation.find.toString());
			if (addNodeList.getLength() == 1) {
				out.println("add InterfaceSet:");
				out.flush();
				initInterfaceSetMap("add", addNodeList.item(0), addInterfaceSet);
				documentPrepcessing.getAddInterfaceSetMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
						addInterfaceSet);
			}
			if (deleteNodeList.getLength() == 1) {
				out.println("delete InterfaceSet:");
				out.flush();
				initInterfaceSetMap("delete", deleteNodeList.item(0), deleteInterfaceSet);
				documentPrepcessing.getDeleteInterfaceSetMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
						deleteInterfaceSet);
			}
			if (updateNodeList.getLength() == 1) {
				out.println("update InterfaceSet:");
				out.flush();
				initInterfaceSetMap("update", updateNodeList.item(0), updateInterfaceSet);
				documentPrepcessing.getUpdateInterfaceSetMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
						updateInterfaceSet);
			}
			if (findNodeList.getLength() == 1) {
				out.println("find InterfaceSet:");
				out.flush();
				initInterfaceSetMap("find", findNodeList.item(0), findInterfaceSet);
				documentPrepcessing.getFindInterfaceSetMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
						findInterfaceSet);
			}
			documentPrepcessing.getOperaterTypesMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
					operaterTypes.toString());
			out.close();
		}

		private String paramAnalysis(Element resource, Element requestParam) {
			StringBuffer reslut = new StringBuffer();
			try{
				NodeList elements = requestParam.getElementsByTagName(TagName.element.toString());
				if (elements.getLength() > 0) {
					reslut.append(requestParam.getAttribute(ParamAttribute.name.toString()) + ","
							+ requestParam.getAttribute(ParamAttribute.type.toString()) + ","
							+ requestParam.getAttribute(ParamAttribute.attribute.toString()) + ","
							+ requestParam.getAttribute(ParamAttribute.location.toString()));
					reslut.append(",(");
					int elementsNum = elements.getLength();
					for (int i = 0; i < elementsNum; i++) {
						reslut.append(elementAnalysis(resource, requestParam, (Element) elements.item(i)));
						if(i < elementsNum-1){
							reslut.append("_");
						}
					}
					reslut.append(")");
				} else {
					reslut.append(requestParam.getAttribute(ParamAttribute.name.toString()) + ","
							+ requestParam.getAttribute(ParamAttribute.type.toString()) + ","
							+ requestParam.getAttribute(ParamAttribute.attribute.toString()) + ","
							+ requestParam.getAttribute(ParamAttribute.location.toString()));
					NodeList restrictions = requestParam.getElementsByTagName(ParamElement.restriction.toString());
					if (restrictions.getLength() == 1) {
						Node restriction = restrictions.item(0);
						ParameterConstrain constrain = new ParameterConstrain(
								resource.getAttribute(ResourcesAttribute.id.toString()),
								requestParam.getAttribute(ParamAttribute.name.toString()), restriction);
						String parameterConstrains = constrain.getResult();
						parameterConstrainsSet.add(parameterConstrains);
					}
				}
				return reslut.toString();
			}catch (Exception e) {
				System.err.println("param error");
			}
			return reslut.toString();
		}

		private String elementAnalysis(Element resource, Element requestParam, Element element) {
			StringBuffer reslut = new StringBuffer();
			try{
				reslut.append(element.getAttribute(ElementAttribute.name.toString()) + ","
						+ element.getAttribute(ElementAttribute.type.toString()) + ","
						+ element.getAttribute(ElementAttribute.attribute.toString()) + ","
						+ element.getAttribute(ElementAttribute.location.toString()) + ","
						+ element.getAttribute(ElementAttribute.level.toString()));
				NodeList restrictions = element.getElementsByTagName(ParamElement.restriction.toString());
				if (restrictions.getLength() == 1) {
					Node restriction = restrictions.item(0);
					ElementConstrain constrain = new ElementConstrain(
							resource.getAttribute(ResourcesAttribute.id.toString()),
							requestParam.getAttribute(ParamAttribute.name.toString()),
							element.getAttribute(ElementAttribute.name.toString()), restriction);
					String parameterConstrains = constrain.getResult();
					elementConstrainsSet.add(parameterConstrains);
				}
			}catch (Exception e) {
				System.err.println("element error");
			}
			return reslut.toString();
		}

		private void initInterfaceSetMap(String type, Node node, ArrayList<String> xInteInterfaceSet) {
			DocumentPrepcessing documentPrepcessing = null;
			try {
				documentPrepcessing = DocumentPrepcessing.getInstance();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			operaterTypes.append("<" + node.getNodeName() + ">");
			NodeList resourceList = node.getChildNodes();
			int ntv = resourceList.getLength();
			for (int i = 0; i < ntv; i++) {
				if (resourceList.item(i) instanceof Element) {
					try {
						Element element = (Element) resourceList.item(i);
						if (TagName.resource.toString().equals(element.getNodeName())) {
							Element resource = element;
							NodeList requestAndResponse = resource.getChildNodes();
							Element request = null;
							Element response = null;
							for (int k = 0; k < requestAndResponse.getLength(); k++) {
								try {
									Element elementTemp = (Element) requestAndResponse.item(k);						
									if (TagName.request.toString().equals(elementTemp.getNodeName())) {
										request = elementTemp;						
									}
									if (TagName.response.toString().equals(elementTemp.getNodeName())) {
										response = elementTemp;
									}
								} catch (Exception e) {
								}
							}
							if (request != null && response != null) {
								NodeList requestParams = request.getElementsByTagName(Param.param.toString());
								NodeList dependencys = request.getElementsByTagName(Param.dependency.toString());
								if (dependencys.getLength() != 0) {
									for (int j = 0; j < dependencys.getLength(); j++) {
										Element dependency = (Element) dependencys.item(j);
										String resourcesid = dependency
												.getAttribute(DependencyAttribute.resourcesid.toString());
										String resourceid = dependency
												.getAttribute(DependencyAttribute.resourceid.toString());
										while (!documentPrepcessing.getOperaterTypesMap().containsKey(resourcesid)) {
											synchronized (this) {
												try {
													this.wait();
												} catch (InterruptedException e) {
													e.printStackTrace();
												}
											}
										}
										ArrayList<String> xInteInterfaces = documentPrepcessing
												.getXInteInterfaceSetMap(type).get(resourcesid);// 获取对应服务分对应操作的所有接口
										String xInteInterface = null;
										for (String inteInterface : xInteInterfaces) {// 遍历所有接口
											if (inteInterface.contains(resourceid)) {// 获得制定接口
												xInteInterface = inteInterface;// 得到对应接口所有信息
											}
										}
										xInteInterfaceSet.add(xInteInterface.toString());
										for (String s : documentPrepcessing.getParameterConstrainsMap()
												.get(resourcesid)) {// 对于被依赖的服务的约束条件表进行遍历
											if (s.contains(resourceid)) {
												parameterConstrainsSet.add(s);// 将被依赖接口的约束添加进该接口约束域内
											}
										}
									}
								}
								StringBuffer xInteInterface = new StringBuffer(
										resource.getAttribute(ResourceAttribute.id.toString()) + ","
												+ resource.getAttribute(ResourceAttribute.path.toString()) + "|"
												+ response.getAttribute(DataAttribute.name.toString()) + "-"
												+ response.getAttribute(DataAttribute.dataType.toString()) + "-");
								NodeList responseParams = response.getElementsByTagName(Param.param.toString());
								for (int j = 0; j < responseParams.getLength(); j++) {
									Element responseParam = (Element) responseParams.item(j);
									xInteInterface.append(responseParam.getAttribute(ParamAttribute.name.toString())
											+ "," + responseParam.getAttribute(ParamAttribute.type.toString()) + ","
											+ responseParam.getAttribute(ParamAttribute.attribute.toString()));
									if (j < requestParams.getLength() - 1) {
										xInteInterface.append("_");
									}
								}
								xInteInterface.append("->");
									for (int j = 0; j < requestParams.getLength(); j++) {
										String param = null;
										try{
											param = paramAnalysis(resource, (Element) requestParams.item(j));
										}catch (Exception e) {
											System.err.println(param);
										}
										xInteInterface.append(param);	
									if (j < requestParams.getLength() - 1) {
										xInteInterface.append("_");
									}
								}
								Element root = (Element) node.getParentNode();
								documentPrepcessing.getParameterConstrainsMap().put(
										root.getAttribute(ResourcesAttribute.id.toString()), parameterConstrainsSet);
								documentPrepcessing.getElementConstrainsMap().put(
										root.getAttribute(ResourcesAttribute.id.toString()), elementConstrainsSet);
								out.println(xInteInterface.toString());
								out.flush();
								xInteInterfaceSet.add(xInteInterface.toString());
							}
						}
					} catch (Exception e) {
					}
				}
			}
			synchronized (this) {
				this.notifyAll();
			}
		}
	}
}

enum DependencyAttribute {
	// 依赖属性
	resourcesid, resourceid
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
	name, type, dataType
}

enum Param {
	// 参数
	param, dependency
}

enum ParamAttribute {
	// 参数属性
	name, attribute, type, required, location
}

enum ParamElement {
	// param的子节点
	restriction, element,
}

enum ElementAttribute {
	// 子项包含
	name, level, type, attribute, location
}

enum TagName {
	// 标签名
	resources, resource, add, find, delete, update, param, element, restricition, request, response
}
