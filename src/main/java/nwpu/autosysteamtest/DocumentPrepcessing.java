package nwpu.autosysteamtest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
 * @version 2.1,25/11/2017
 *
 */
public class DocumentPrepcessing {
	private volatile static DocumentPrepcessing  documentPrepcessing;
	private ConcurrentHashMap<String, String> operaterTypesMap;
	private ConcurrentHashMap<String, ArrayList<String>> addInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> deleteInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> updateInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> findInterfaceSetMap;
	private ConcurrentHashMap<String, ArrayList<String>> parameterConstrainsMap;
	File[] fileSet;
	public static DocumentPrepcessing getInstance() throws InterruptedException{
		if(documentPrepcessing == null){
			synchronized(DocumentPrepcessing.class){
				if(documentPrepcessing == null){
					documentPrepcessing = new DocumentPrepcessing();
				}
			}
		}
		return documentPrepcessing;
	}
	public static DocumentPrepcessing getInstance(File[] fileSet) throws InterruptedException{
		if(documentPrepcessing == null){
			synchronized(DocumentPrepcessing.class){
				if(documentPrepcessing == null){
					documentPrepcessing = new DocumentPrepcessing(fileSet);
				}
			}
		}
		return documentPrepcessing;
	}
	private DocumentPrepcessing(){
		
	}
	public DocumentPrepcessing(File[] fileSet) throws InterruptedException {
		super();
		this.fileSet = fileSet;
		operaterTypesMap = new ConcurrentHashMap<String, String>();
		addInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		deleteInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		updateInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		findInterfaceSetMap = new ConcurrentHashMap<String, ArrayList<String>>();
		parameterConstrainsMap = new ConcurrentHashMap<String, ArrayList<String>>();
		run();
	}

	private void run() throws InterruptedException {
		for(File file:fileSet){
			DocumentPrepcssingThread thread = new DocumentPrepcssingThread(file);
			new Thread(thread).start();
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
	
	public  ConcurrentHashMap<String, ArrayList<String>> getXInteInterfaceSetMap(String type){
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
			protected Document doc;
			File file;

			public DocumentPrepcssingThread(File file) throws InterruptedException {
				this.file = file;
			}
			@Override
			public void run() {
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
				NodeList addNodeList = root.getElementsByTagName(Operation.add.toString());
				NodeList deleteNodeList = root.getElementsByTagName(Operation.delete.toString());
				NodeList updateNodeList = root.getElementsByTagName(Operation.update.toString());
				NodeList findNodeList = root.getElementsByTagName(Operation.find.toString());
				if (addNodeList.getLength() == 1) {
					initInterfaceSetMap("add",addNodeList.item(0), addInterfaceSet);
					documentPrepcessing.getAddInterfaceSetMap().put(root.getAttribute("id"),
							addInterfaceSet);
				}
				if (deleteNodeList.getLength() == 1) {
					initInterfaceSetMap("delete",deleteNodeList.item(0), deleteInterfaceSet);
					documentPrepcessing.getDeleteInterfaceSetMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
							deleteInterfaceSet);
				}
				if (updateNodeList.getLength() == 1) {
					initInterfaceSetMap("update",updateNodeList.item(0), updateInterfaceSet);
					documentPrepcessing.getUpdateInterfaceSetMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
							updateInterfaceSet);
				}
				if (findNodeList.getLength() == 1) {
					initInterfaceSetMap("find",findNodeList.item(0), findInterfaceSet);
					documentPrepcessing.getFindInterfaceSetMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
							findInterfaceSet);
				}
				documentPrepcessing.getOperaterTypesMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
						operaterTypes.toString());
			}

			private void initInterfaceSetMap(String type,Node node, ArrayList<String> xInteInterfaceSet) {
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
						Element resource = (Element) resourceList.item(i);
						Element request = (Element) resource.getChildNodes().item(0);
						Element response = (Element) resource.getChildNodes().item(1);
						NodeList requestParams = request.getElementsByTagName(Param.param.toString());
						NodeList dependencys = request.getElementsByTagName(Param.dependency.toString());
						if(dependencys.getLength()!=0){
							for (int j = 0; j < dependencys.getLength(); j++) {
								Element dependency = (Element) dependencys.item(j);
								String resourcesid = dependency.getAttribute(DependencyAttribute.resourcesid.toString());
								String resourceid = dependency.getAttribute(DependencyAttribute.resourceid.toString());
								while(!documentPrepcessing.getOperaterTypesMap().containsKey(resourcesid)){
									synchronized (this) {
										try {
											this.wait();
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
								ArrayList<String> xInteInterfaces = documentPrepcessing.getXInteInterfaceSetMap(type).get(resourcesid);//获取对应服务分对应操作的所有接口
								String xInteInterface= null;
								for(String inteInterface :xInteInterfaces){//遍历所有接口
									if(inteInterface.contains(resourceid)){//获得制定接口
										xInteInterface = inteInterface;//得到对应接口所有信息
									}
								}
								xInteInterfaceSet.add(xInteInterface.toString());
								for(String s:documentPrepcessing.getParameterConstrainsMap().get(resourcesid)){//对于被依赖的服务的约束条件表进行遍历
									if(s.contains(resourceid)){
										parameterConstrainsSet.add(s);//将被依赖接口的约束添加进该接口约束域内
									}
								}
								
							}	
						}
						StringBuffer xInteInterface = new StringBuffer(resource.getAttribute(ResourceAttribute.id.toString())
								+","+resource.getAttribute(ResourceAttribute.path.toString())+ "|" + response.getAttribute(DataAttribute.name.toString())+"-"+response.getAttribute(DataAttribute.dataType.toString()) + "-");
						
						NodeList responseParams = response.getElementsByTagName(Param.param.toString());
						for (int j = 0; j < responseParams.getLength(); j++) {
							Element responseParam = (Element) responseParams.item(j);
							xInteInterface.append(responseParam.getAttribute(ParamAttribute.name.toString()) + ","
									+ responseParam.getAttribute(ParamAttribute.type.toString())+","+responseParam.getAttribute(ParamAttribute.attribute.toString()));
							if (j < requestParams.getLength() - 1) {
								xInteInterface.append("_");
							}
						}
						xInteInterface.append("->");
						for (int j = 0; j < requestParams.getLength(); j++) {
							Element requestParam = (Element) requestParams.item(j);
							xInteInterface.append(requestParam.getAttribute(ParamAttribute.name.toString()) + ","
									+ requestParam.getAttribute(ParamAttribute.type.toString())+","+requestParam.getAttribute(ParamAttribute.attribute.toString())+","+requestParam.getAttribute(ParamAttribute.location.toString()));
							NodeList restrictions = requestParam.getElementsByTagName(ParamElement.restriction.toString());
							if (restrictions.getLength() == 1) {
								Node restriction = restrictions.item(0);
								ParameterConstrain constrain = new ParameterConstrain(
										resource.getAttribute(ResourcesAttribute.id.toString()),
										requestParam.getAttribute(ParamAttribute.name.toString()), restriction);
								String parameterConstrains = constrain.getResult();
								parameterConstrainsSet.add(parameterConstrains);
							}
							if (j < requestParams.getLength() - 1) {
								xInteInterface.append("_");
							}
						}
						Element root = (Element) node.getParentNode();
						documentPrepcessing.getParameterConstrainsMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
								parameterConstrainsSet);
						System.out.println(xInteInterface);
						xInteInterfaceSet.add(xInteInterface.toString());
					}
				}
				synchronized (this) {
					this.notifyAll();
				}
			}
		}
}

enum DependencyAttribute {
	//依赖属性
	resourcesid,resourceid
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
	param,dependency
}

enum ParamAttribute {
	// 参数属性
	name, attribute, type, required, location
}

enum ParamElement {
	// param的子节点
	restriction, element,
}
enum ElementAttribute{
	//子项包含
	name,level,type
}
