package nwpu.autosysteamtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AutomatedTestData {
	DocumentPrepcessing documentPrepcessing;
	private ConcurrentHashMap<String, String> operaterTypesMap;
	protected ConcurrentHashMap<String, ArrayList<String>> addInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> deleteInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> updateInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> findInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> parameterConstrainsMap;
	protected String resourcesid;
	protected String resourceid;
	protected String path;
	static final long MAX_POINT_DIGIT = 15;//小数点后最大位数
	static final long MAX_TOTAL_DIGIT = 15;//小数最大位数
	
	public AutomatedTestData(String path) throws InterruptedException {
		this.path = path;
		documentPrepcessing = DocumentPrepcessing.getInstance();
		this.operaterTypesMap = documentPrepcessing.getOperaterTypesMap();
		this.addInterfaceSetMap = documentPrepcessing.getAddInterfaceSetMap();
		this.deleteInterfaceSetMap = documentPrepcessing.getDeleteInterfaceSetMap();
		this.updateInterfaceSetMap = documentPrepcessing.getUpdateInterfaceSetMap();
		this.findInterfaceSetMap = documentPrepcessing.getFindInterfaceSetMap();
		this.parameterConstrainsMap = documentPrepcessing.getParameterConstrainsMap();
		this.run1();
	}

	protected void run1() {
		Set<String> key = operaterTypesMap.keySet();
		File file = null;
		try {
			file = new File(path + "date");
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {

		} finally {
			file = null;
		}
		this.path = path + "date\\";
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			this.resourcesid = (String) it.next();
			try {
				file = new File(path + this.resourcesid);
				if (!file.exists()) {
					file.mkdirs();
				}
			} catch (Exception e) {

			} finally {
				file = null;
			}
			try {
				run2();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	protected void run2() throws FileNotFoundException {
		xAutomated("add", this.addInterfaceSetMap);
		xAutomated("update", this.updateInterfaceSetMap);
		xAutomated("delete", this.deleteInterfaceSetMap);
		// findDateAutomated();
	}

	private void findDateAutomated() {

	}

	private void xAutomated(String operation, ConcurrentHashMap<String, ArrayList<String>> xInterfaceSetMap)
			throws FileNotFoundException {
		ArrayList<String> xInterfaceSets = xInterfaceSetMap.get(this.resourcesid);
		ArrayList<String> parameterConstrains = null;
		if (this.parameterConstrainsMap.containsKey(resourcesid)) {
			parameterConstrains = this.parameterConstrainsMap.get(this.resourcesid);
		}
		for (String xInterface : xInterfaceSets) {
			this.resourceid = xInterface.split("\\|")[0].split(",")[0];
			File file = null;
			try {
				file = new File(path + this.resourcesid + "\\" + operation + "\\" + this.resourceid);
				if (!file.exists()) {
					file.mkdirs();
				}
			} catch (Exception e) {

			} finally {
				file = null;
			}
			ArrayList<String> resourceParameterConstrains = new ArrayList<>();
			for (String parameterConstrain : parameterConstrains) {
				if (parameterConstrain.startsWith(resourceid)) {
					resourceParameterConstrains.add(parameterConstrain);
				}
			}
			analyticParameter(this.path + this.resourcesid + "\\" + operation + "\\" + this.resourceid + "\\",
					xInterface, resourceParameterConstrains);
		}

	}

	private void analyticParameter(String path, String xInterface, ArrayList<String> parameterConstrains) {
		String[] paramsplt = xInterface.split("->");
		if (paramsplt.length > 1) {
			String[] params = paramsplt[1].split("_");
			for (String param : params) {
				String paramName = param.split(",")[0];
				String paramConstrains = null;
				for (String s : parameterConstrains) {
					if (s.startsWith(resourceid + "-" + paramName))
						paramConstrains = s;
				}
				analyticParameterComposition(param,paramName,paramConstrains);
				
			}
		}

	}
	private void  analyticParameterComposition(String param,String paramName,String paramConstrains){
		System.out.println(param);
		System.out.println(paramConstrains);
		String paramType = param.split(",")[1];
		String paramAtributte = param.split(",")[2];
		String[] constraints = paramConstrains.split("<")[1].split(">")[0].split("#");
		switch (paramType) {
		case "byte":
		case"decimal":
		case "int":
		case "integer":
		case "long":
		case "negativeInteger":
		case "nonNegativeInteger":
		case "nonPositiveInteger":
		case "positiveInteger":	
		case "short":
		case "unsignedLong":
		case "unsignedInt":
		case "unsignedShort":
		case "unsignedByte":
		case "float":
		case "double":
			numericalType(constraints,paramType);
			break;
		case "string":
		case "token":
			stringType(constraints,paramType);
			break;
		case "date":
		case "dateTime":
		case "duration":		
		case "gDay":
		case "gMonth":
		case "gMonthDay":
		case "gYear":	
		case "gYearMonth":
		case "time":
			dateType(constraints,paramType);
			break;
		case "boolean":
			boolType();
			break;
		case "anyURI":
			fileType(constraints,paramType);
			break;
		default:
			break;
		}
		
	}
	private void fileType(String[] constraints, String paramType) {
		// TODO Auto-generated method stub
		
	}

	private void boolType() {
		// TODO Auto-generated method stub
		ArrayList<String> values = new ArrayList<>();	
		values.add("true");
		values.add("false");
		
	}

	private void dateType(String[] constraints, String paramType) {
		// TODO Auto-generated method stub
		
	}

	private void stringType(String[] constraints, String paramType) {
		// TODO Auto-generated method stub
		
	}

	private void numericalType(String[] constraints, String paramType) {
		// TODO Auto-generated method stub
		String constraintses = constraints.toString();
		ArrayList<String> values = new ArrayList<>();	
		if(constraintses.contains("enumeration")){
			for(String enumerations :constraints){
				values.add(enumerations.split(":")[1]);
			}
		}else{
			ConcurrentHashMap<String, String> constraint = new ConcurrentHashMap<>();
			for(String t:constraints){
				String[] tt = t.split(",");
				constraint.put(tt[0], tt[1]);
			}
			switch (paramType) {
			case "byte":
			case "int":
			case "integer":
			case "long":
				values = longTypedate(constraint);
				break;
			case "negativeInteger":
			case "nonNegativeInteger":
			case "nonPositiveInteger":
			case "positiveInteger":	
			case "short":
			case "unsignedLong":
			case "unsignedInt":
			case "unsignedShort":
			case "unsignedByte":
				break;
			case "decimal":
			case "float":
			case "double":
				break;
			default:
				break;
			}
		}
		
	}

	private ArrayList<String> longTypedate(ConcurrentHashMap<String, String> constraint) {
		// TODO Auto-generated method stub
		ArrayList<String> values = new ArrayList<>();
		long value = 0L;
		if(constraint.containsKey("minExclusive")){
			if(constraint.containsKey("maxExclusive")){
				value = p(Long.parseLong(constraint.get("minExclusive"))+1L,Long.parseLong(constraint.get("maxExclusive"))-1L);
			}else if(constraint.containsKey("maxIxclusive")){
				value = p(Long.parseLong(constraint.get("minExclusive"))+1L,Long.parseLong(constraint.get("maxIxclusive")));
			}else{
				value = p(Long.parseLong(constraint.get("minExclusive"))+1L,Long.MAX_VALUE);
			}
		}else if(constraint.containsKey("minInclusive")){
			if(constraint.containsKey("maxExclusive")){
				value = p(Long.parseLong(constraint.get("minInclusive")),Long.parseLong(constraint.get("maxExclusive"))-1L);
			}else if(constraint.containsKey("maxIxclusive")){
				value = p(Long.parseLong(constraint.get("minInclusive")),Long.parseLong(constraint.get("maxIxclusive")));
			}else{
				value = p(Long.parseLong(constraint.get("minInclusive")),Long.MAX_VALUE);			
			}
		}else if(constraint.containsKey("maxExclusive")){
			if(constraint.containsKey("minExclusive")){
				value = p(Long.parseLong(constraint.get("minExclusive"))+1L,Long.parseLong(constraint.get("maxExclusive"))-1L);
			}else if(constraint.containsKey("minInclusive")){
				value = p(Long.parseLong(constraint.get("minInclusive")),Long.parseLong(constraint.get("maxExclusive"))-1L);
			}else{
				value = p(Long.MIN_VALUE,Long.parseLong(constraint.get("maxExclusive"))-1L);
			}
		}else if(constraint.containsKey("maxIxclusive")){
			if(constraint.containsKey("minExclusive")){
				value = p(Long.parseLong(constraint.get("minExclusive"))+1L,Long.parseLong(constraint.get("maxIxclusive")));
			}else if(constraint.containsKey("minInclusive")){
				value = p(Long.parseLong(constraint.get("minIxclusive")),Long.parseLong(constraint.get("maxIxclusive")));
			}else{
				value = p(Long.MIN_VALUE,Long.parseLong(constraint.get("maxIxclusive")));
			}
		}else{
			value = p(Long.MIN_VALUE,Long.MAX_VALUE);
		}
		return values;
		
	}

	private long p(long l, long m) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void generateDataFile(String paramName){		
		try {
			PrintWriter out = new PrintWriter(path + paramName + ".xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}

