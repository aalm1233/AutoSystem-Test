package nwpu.autosysteamtest.run;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import nwpu.autosysteamtest.entity.RequestElement;
import nwpu.autosysteamtest.entity.RequestParam;
import nwpu.autosysteamtest.entity.ResponseElement;
import nwpu.autosysteamtest.entity.ResponseParam;
import nwpu.autosysteamtest.entity.Service;

/**
 * 
 * @author Dengtong
 * @version 4.0,28/01/2018
 *
 */
public class DocumentPrepcessing {
	private volatile static DocumentPrepcessing documentPrepcessing;
	private ConcurrentHashMap<String, String> operaterTypesMap;
	private ArrayList<Service> services;
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

	public ArrayList<Service> getServices() {
		return services;
	}

	public Service searchServiceById(String id) {
		Service reslut = null;
		for (Service s : services) {
			String sid = s.getId();
			if (sid.equals(id)) {
				reslut = s;
				break;
			}
		}
		return reslut;
	}

	public DocumentPrepcessing(File[] fileSet) throws InterruptedException, FileNotFoundException {
		super();
		this.fileSet = fileSet;
		operaterTypesMap = new ConcurrentHashMap<String, String>();
		services = new ArrayList<>();
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

	public void addService(Service service) {
		services.add(service);
	}

	public ConcurrentHashMap<String, String> getOperaterTypesMap() {
		return operaterTypesMap;
	}

	class DocumentPrepcssingThread implements Runnable {
		private StringBuffer operaterTypes;
		protected Document doc;
		Service service;
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
				out.println("---------------------------" + new Date() + "---------------------------");
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
			service = new Service(root.getAttribute(ResourcesAttribute.name.toString()),
					root.getAttribute(ResourcesAttribute.id.toString()),
					root.getAttribute(ResourcesAttribute.base.toString()));
			System.out.println(service.getId()+": start anlysis");
			out.flush();
			out.println("service name:" + service.getId());
			NodeList addNodeList = root.getElementsByTagName(Operation.add.toString());
			NodeList deleteNodeList = root.getElementsByTagName(Operation.delete.toString());
			NodeList updateNodeList = root.getElementsByTagName(Operation.update.toString());
			NodeList findNodeList = root.getElementsByTagName(Operation.find.toString());
			if (addNodeList.getLength() == 1) {
				out.println("add InterfaceSet:");
				out.flush();
				ArrayList<nwpu.autosysteamtest.entity.Operation> adds = new ArrayList<>();
				service.setAdds(adds);
				initInterfaceSetMap(addNodeList.item(0), adds);
			}
			if (deleteNodeList.getLength() == 1) {
				out.println("delete InterfaceSet:");
				out.flush();
				ArrayList<nwpu.autosysteamtest.entity.Operation> deletes = new ArrayList<>();
				service.setDeletes(deletes);
				initInterfaceSetMap(deleteNodeList.item(0), deletes);
			}
			if (updateNodeList.getLength() == 1) {
				out.println("update InterfaceSet:");
				out.flush();
				ArrayList<nwpu.autosysteamtest.entity.Operation> updates = new ArrayList<>();
				service.setUpdates(updates);
				initInterfaceSetMap(updateNodeList.item(0), updates);

			}
			if (findNodeList.getLength() == 1) {
				out.println("find InterfaceSet:");
				out.flush();
				ArrayList<nwpu.autosysteamtest.entity.Operation> finds = new ArrayList<>();
				service.setFinds(finds);
				initInterfaceSetMap(findNodeList.item(0), finds);

			}
			documentPrepcessing.addService(service);
			documentPrepcessing.getOperaterTypesMap().put(root.getAttribute(ResourcesAttribute.id.toString()),
					operaterTypes.toString());
			System.out.println(service.getId()+" :anlysis finished");
			out.close();
		}

		private RequestParam requestParamAnalysis(Element resource, Element requestParam) {
			RequestParam param = null;
			try {
				NodeList elements = requestParam.getElementsByTagName(TagName.element.toString());
				if (elements.getLength() > 0) {
					param = new RequestParam(requestParam.getAttribute(ParamAttribute.name.toString()),
							requestParam.getAttribute(ParamAttribute.attribute.toString()),
							requestParam.getAttribute(ParamAttribute.type.toString()),
							requestParam.getAttribute(ParamAttribute.location.toString()));
					int elementsNum = elements.getLength();
					ArrayList<RequestElement> elementes = new ArrayList<>();
					Stack<RequestElement> stack = new Stack<>();
					for (int i = 0; i < elementsNum; i++) {
						RequestElement requestElement = requestElementAnalysis(resource, requestParam,
								(Element) elements.item(i));
						if (stack.isEmpty()) {
							stack.push(requestElement);
							elementes.add(requestElement);
						} else {
							
							int parentLevel = stack.peek().getLevel();
							int level = requestElement.getLevel();
							if (parentLevel == level) {
								if (level == 1) {
									stack.pop();
									stack.push(requestElement);
									elementes.add(requestElement);
								} else {
									stack.pop();
									for (;;) {
										parentLevel = stack.peek().getLevel();
										if (parentLevel == level||parentLevel > level) {
											stack.pop();
											continue;
										} else if (parentLevel < level) {
											stack.peek().addElement(requestElement);
											stack.push(requestElement);
											break;
										}
									}
								}
							} else if (parentLevel > level) {
								stack.pop();
								a : for (;;) {
									if (stack.empty()) {
										stack.push(requestElement);
										elementes.add(requestElement);
										break a;
									} else {
										parentLevel = stack.peek().getLevel();
										if (parentLevel == level) {
											stack.pop();
											continue a;
										} else if (parentLevel > level) {
											stack.pop();
											continue a;
										}else if (parentLevel < level) {
											stack.peek().addElement(requestElement);
											stack.push(requestElement);
											break a;
										}
									}
								}
							} else if (parentLevel < level) {
								RequestElement parentElement = stack.peek();
								parentElement.addElement(requestElement);
								stack.push(requestElement);
							}
						}
					}
					param.setElements(elementes);
					out.println("    " + resource.getAttribute(ResourcesAttribute.id.toString()) + "-"
								+ requestParam.getAttribute(ParamAttribute.name.toString()));
				} else {
					param = new RequestParam(requestParam.getAttribute(ParamAttribute.name.toString()),
							requestParam.getAttribute(ParamAttribute.attribute.toString()),
							requestParam.getAttribute(ParamAttribute.type.toString()),
							requestParam.getAttribute(ParamAttribute.location.toString()));
					NodeList restrictions = requestParam.getElementsByTagName(ParamElement.restriction.toString());
					if (restrictions.getLength() == 1) {
						Node restriction = restrictions.item(0);
						ParameterConstrain constrain = new ParameterConstrain(restriction);
						ArrayList<String> parameterConstrains = constrain.getResult();
						param.setConstraints(parameterConstrains);
						out.println("    " + resource.getAttribute(ResourcesAttribute.id.toString()) + "-"
								+ requestParam.getAttribute(ParamAttribute.name.toString())
								+ parameterConstrains.toString());
					} else {
						out.println("    " + resource.getAttribute(ResourcesAttribute.id.toString()) + "-"
								+ requestParam.getAttribute(ParamAttribute.name.toString()));
					}
				}
				return param;
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("param error");
			}
			return param;
		}

		private RequestElement requestElementAnalysis(Element resource, Element requestParam, Element element) {
			RequestElement childElement = null;
			try {
				String levelstring = element.getAttribute(ElementAttribute.level.toString());
				int level = 1;
				if (!"".equals(levelstring)) {
					level = Integer.parseInt(levelstring);
				}
				childElement = new RequestElement(element.getAttribute(ElementAttribute.name.toString()),
						element.getAttribute(ElementAttribute.attribute.toString()),
						element.getAttribute(ElementAttribute.type.toString()),
						element.getAttribute(ElementAttribute.location.toString()), level);
				NodeList restrictions = element.getElementsByTagName(ParamElement.restriction.toString());
				if (restrictions.getLength() == 1) {
					Node restriction = restrictions.item(0);
					ElementConstrain constrain = new ElementConstrain(restriction);
					ArrayList<String> elementConstrains = constrain.getResult();
					childElement.setConstraints(elementConstrains);
					out.println("          " + resource.getAttribute(ResourcesAttribute.id.toString()) + "-"
							+ requestParam.getAttribute(ParamAttribute.name.toString()) + "-"
							+ element.getAttribute(ElementAttribute.name.toString()) + elementConstrains.toString());
				} else {
					out.println("          " + resource.getAttribute(ResourcesAttribute.id.toString()) + "-"
							+ requestParam.getAttribute(ParamAttribute.name.toString()) + "-"
							+ element.getAttribute(ElementAttribute.name.toString()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("element error");
			}
			return childElement;
		}

		private ResponseParam responseParamAnalysis(Element resource, Element responseParam) {
			ResponseParam param = null;
			try {
				NodeList elements = responseParam.getElementsByTagName(TagName.element.toString());
				if (elements.getLength() > 0) {
					param = new ResponseParam(responseParam.getAttribute(ParamAttribute.name.toString()),
							responseParam.getAttribute(ParamAttribute.attribute.toString()));
					int elementsNum = elements.getLength();
					ArrayList<ResponseElement> elementes = new ArrayList<>();
					for (int i = 0; i < elementsNum; i++) {
						Stack<ResponseElement> stack = new Stack<>();
						ResponseElement responseElement = responseElementAnalysis(resource, responseParam,
								(Element) elements.item(i));
						if (stack.isEmpty()) {
							stack.push(responseElement);
							elementes.add(responseElement);
						} else {
							int parentLevel = stack.peek().getLevel();
							int level = responseElement.getLevel();
							if (parentLevel == level) {
								if (level == 1) {
									stack.pop();
									stack.push(responseElement);
									elementes.add(responseElement);
								} else {
									stack.pop();
									for (;;) {
										parentLevel = stack.peek().getLevel();
										if (parentLevel == level||parentLevel > level) {
											stack.pop();
											continue;
										} else if (parentLevel < level) {
											stack.peek().addElement(responseElement);
											stack.push(responseElement);
											break;
										}
									}
								}
							} else if (parentLevel > level) {
								stack.pop();
								a : for (;;) {
									parentLevel = stack.peek().getLevel();
									if (stack.empty()) {
										stack.push(responseElement);
										elementes.add(responseElement);
										break a;
									} else {
										if (parentLevel == level) {
											stack.pop();
											continue a;
										} else if (parentLevel > level) {
											stack.pop();
											continue a;
										}else if (parentLevel < level) {
											stack.peek().addElement(responseElement);
											stack.push(responseElement);
											break a;
										}
									}
								}
							} else if (parentLevel < level) {
								ResponseElement parentElement = stack.peek();
								parentElement.addElement(responseElement);
								stack.push(responseElement);
							}
						}
					}
					param.setElements(elementes);
				} else {
					param = new ResponseParam(responseParam.getAttribute(ParamAttribute.name.toString()),
							responseParam.getAttribute(ParamAttribute.attribute.toString()));
				}

				return param;
			} catch (Exception e) {
				System.err.println("param error");
			}
			return param;
		}

		private ResponseElement responseElementAnalysis(Element resource, Element responseParam, Element element) {
			ResponseElement childElement = null;
			try {
				String levelstring = element.getAttribute(ElementAttribute.level.toString());
				int level = 1;
				if (!"".equals(levelstring)) {
					level = Integer.parseInt(levelstring);
				}
				childElement = new ResponseElement(element.getAttribute(ElementAttribute.name.toString()),
						element.getAttribute(ElementAttribute.attribute.toString()), level);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("element error");
			}
			return childElement;
		}

		private void resourceAnalysis(Element resource, String type,
				ArrayList<nwpu.autosysteamtest.entity.Operation> operations) {
			nwpu.autosysteamtest.entity.Operation operation = new nwpu.autosysteamtest.entity.Operation(
					resource.getAttribute(ResourceAttribute.name.toString()),
					resource.getAttribute(ResourceAttribute.id.toString()),
					resource.getAttribute(ResourceAttribute.path.toString()), type);
			out.println("  " + operation.toString());
			out.flush();
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
				ArrayList<RequestParam> requestParames = new ArrayList<>();
				for (int j = 0; j < requestParams.getLength(); j++) {
					RequestParam requestParam = null;
					try {
						requestParam = requestParamAnalysis(resource, (Element) requestParams.item(j));
						requestParames.add(requestParam);
					} catch (Exception e) {
					}
				}
				NodeList dependencys = resource.getElementsByTagName(Param.dependency.toString());
				if (dependencys.getLength() != 0) {
					for (int j = 0; j < dependencys.getLength(); j++) {
						Element dependency = (Element) dependencys.item(j);
						String resourcesid = dependency.getAttribute(DependencyAttribute.resourcesid.toString());
						String resourceid = dependency.getAttribute(DependencyAttribute.resourceid.toString());
						while (!documentPrepcessing.getOperaterTypesMap().containsKey(resourcesid)) {
							synchronized (this) {
								try {
									System.out.println(service.getName()+" :find a dependency not in the RAM,start wait");
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
						System.out.println(service.getName()+" :find it continue anlysis");
						nwpu.autosysteamtest.entity.Operation dependencyInteInterface = documentPrepcessing
								.searchServiceById(resourcesid).searchAllOperationById(resourceid);
						operation.addDependency(dependencyInteInterface);
					}
				}
				String responses = response.getAttribute(DataAttribute.dataType.toString());
				operation.setResponse(responses);
				NodeList responseParams = response.getElementsByTagName(Param.param.toString());
				ArrayList<ResponseParam> responseParames = new ArrayList<>();
				operation.setRequestParams(requestParames);
				for (int j = 0; j < responseParams.getLength(); j++) {
					ResponseParam responseParam = null;
					try {
						responseParam = responseParamAnalysis(resource, (Element) responseParams.item(j));
						responseParames.add(responseParam);
					} catch (Exception e) {
					}
				}
				operation.setResponseParams(responseParames);
				out.flush();
				
			}
			operations.add(operation);
		}

		private void initInterfaceSetMap(Node node, ArrayList<nwpu.autosysteamtest.entity.Operation> operations) {
			String type = node.getNodeName();
			operaterTypes.append("<" + type + ">");
			NodeList resourceList = node.getChildNodes();
			int ntv = resourceList.getLength();
			for (int i = 0; i < ntv; i++) {
				if (resourceList.item(i) instanceof Element) {
					try {
						Element element = (Element) resourceList.item(i);
						if (TagName.resource.toString().equals(element.getNodeName())) {
							Element resource = element;
							resourceAnalysis(resource, type, operations);
						}else if(TagName.dependency.toString().equals(element.getNodeName())){
							Element dependency = element;
							dependencyAnalysis(dependency,operations);
						}
					} catch (Exception e) {
					}
				}
			}
			synchronized (this) {
				this.notifyAll();
			}
		}

		private void dependencyAnalysis(Element dependency,
				ArrayList<nwpu.autosysteamtest.entity.Operation> operations) {
			String resourcesid = dependency.getAttribute(DependencyAttribute.resourcesid.toString());
			String resourceid = dependency.getAttribute(DependencyAttribute.resourceid.toString());
			while (!documentPrepcessing.getOperaterTypesMap().containsKey(resourcesid)) {
				synchronized (this) {
					try {
						System.out.println(service.getName()+" :find a dependency not in the RAM,start wait");
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println(service.getName()+" :find it continue anlysis");
			nwpu.autosysteamtest.entity.Operation dependencyInteInterface = documentPrepcessing
					.searchServiceById(resourcesid).searchAllOperationById(resourceid);
			operations.add(dependencyInteInterface);
			
		}
	}
}

enum DependencyAttribute {
	//依赖
	resourcesid, resourceid
}

enum ResourcesAttribute {
	//服务属性
	id, name, base
}

enum Operation {
	// 操作类型
	add, delete, update, find
}

enum ResourceAttribute {
	//接口属性
	id, name, path, cascade
}

enum DataAttribute {
	//回复属性
	dataType
}

enum Param {
	//参数类型
	param, dependency
}

enum ParamAttribute {
	//参数属性
	name, attribute, type, required, location
}

enum ParamElement {
	//参数子节点
	restriction, element,
}

enum ElementAttribute {
	//子参数属性
	name, level, type, attribute, location
}

enum TagName {
	//xmlTag名称
	resources, resource, add, find, delete, update, param, element, restricition, request, response ,dependency
}
