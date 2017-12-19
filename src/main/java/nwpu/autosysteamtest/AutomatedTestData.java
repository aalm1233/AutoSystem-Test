package nwpu.autosysteamtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedInts;
import com.google.common.primitives.UnsignedLong;
import com.google.common.primitives.UnsignedLongs;
/**
 * 
 * @author Dengtong
 *@version 0.49
 */
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
					if (s.startsWith(resourceid + "-" + paramName)){
						paramConstrains = s;
					}
				}
				analyticParameterComposition(path,param,paramName,paramConstrains);
			}
		}

	}
	private void  analyticParameterComposition(String path,String param,String paramName,String paramConstrains){
		String paramType = param.split(",")[1];
		String paramAtributte = param.split(",")[2];
		String paramStatus = param.split(",")[3];
		if("false".equals(paramStatus)){
			ArrayList<String> values = new ArrayList<>();
			String[] constraints = null;
			if(paramConstrains != null&&paramConstrains.contains("#")){
				constraints = paramConstrains.split("<")[1].split(">")[0].split("#");
			}else if(paramConstrains != null){
				constraints = paramConstrains.split("<")[1].split(">");
			}
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
				values = numericalType(constraints,paramType);
				break;
			case "string":
			case "token":
				values = stringType(constraints,paramType);
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
				values = dateType(constraints,paramType);
				break;
			case "boolean":
				values = boolType();
				break;
			case "anyURI":
				values = fileType(constraints,paramType);
				break;
			default:
				break;
			}
			try {
				
				PrintWriter out = new PrintWriter(path + paramName + ".xml");
				out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				out.flush();
				out.println("<param name=\""+paramName+"\" attribute=\""+paramAtributte+"\" status=\"generate\">");
				out.flush();
				for(String value:values){
					out.println("	<value>"+value+"</value>");
					out.flush();
				}
				out.println("</param>");
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else if("true".equals(paramStatus)){
			try {	
				PrintWriter out = new PrintWriter(path + paramName + ".xml");
				out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				out.flush();
				out.println("<param name=\""+paramName+"\" attribute=\""+paramAtributte+"\" status=\"extend\">");
				out.flush();
				out.println("</param>");
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
	private ArrayList<String> fileType(String[] constraints, String paramType) {
		// TODO Auto-generated method stub
		ArrayList<String> values = new ArrayList<>();	
		return values;
	}

	private ArrayList<String> boolType() {
		// TODO Auto-generated method stub
		ArrayList<String> values = new ArrayList<>();	
		values.add("true");
		values.add("false");
		return values;
	}

	private ArrayList<String> dateType(String[] constraints, String paramType) {
		// TODO Auto-generated method stub
		ArrayList<String> values = new ArrayList<>();	
		return values;
	}

	private ArrayList<String> stringType(String[] constraints, String paramType) {
		// TODO Auto-generated method stub
		ArrayList<String> values = new ArrayList<>();	
		return values;
	}

	private ArrayList<String> numericalType(String[] constraints, String paramType) {
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
				values = byteType(constraint);
				break;
			case "int":
			case "integer":
				values = intType(constraint);
				break;
			case "long":
				values = longType(constraint);
				break;
			case "negativeInteger":
				values = negativeIntType(constraint);
				break;
			case "nonNegativeInteger":
				values = nonNegativeIntType(constraint);
				break;
			case "nonPositiveInteger":
				values = nonPositivIntType(constraint);
				break;
			case "positiveInteger":
				values = PositivIntType(constraint);
				break;
			case "short":
				values =shortType(constraint);
				break;
			case "unsignedLong":
				values =unsignedLongType(constraint);
				break;
			case "unsignedInt":
				values =unsignedIntType(constraint);
				break;
			case "unsignedShort":
				values =unsignedShortType(constraint);
				break;
			case "unsignedByte":
				values =unsignedByteType(constraint);
				break;
			case "decimal":
			case "float":
			case "double":
				break;
			default:
				break;
			}
		}
		return values;
		
	}

	private ArrayList<String> unsignedByteType(ConcurrentHashMap<String, String> constraint) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> unsignedShortType(ConcurrentHashMap<String, String> constraint) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> unsignedIntType(ConcurrentHashMap<String, String> constraint) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> unsignedLongType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		return values;
	}

	private ArrayList<String> shortType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if(constraint.containsKey("minExclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = shortTypeDate(Short.parseShort(constraint.get("minExclusive"))+1,Short.parseShort(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = shortTypeDate(Short.parseShort(constraint.get("minExclusive"))+1,Short.parseShort(constraint.get("maxIxclusive")));
			}else{
				values = shortTypeDate(Short.parseShort(constraint.get("minExclusive"))+1,Short.MAX_VALUE-1);
			}
		}else if(constraint.containsKey("minInclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = shortTypeDate(Short.parseShort(constraint.get("minInclusive")),Short.parseShort(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = shortTypeDate(Short.parseShort(constraint.get("minInclusive")),Short.parseShort(constraint.get("maxIxclusive")));
			}else{
				values = shortTypeDate(Short.parseShort(constraint.get("minInclusive")),Short.MAX_VALUE-1);			
			}
		}else if(constraint.containsKey("maxExclusive")){
			if(constraint.containsKey("minExclusive")){
				values = shortTypeDate(Short.parseShort(constraint.get("minExclusive"))+1,Short.parseShort(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("minInclusive")){
				values = shortTypeDate(Short.parseShort(constraint.get("minInclusive")),Short.parseShort(constraint.get("maxExclusive"))-1);
			}else{
				values = shortTypeDate(Short.MIN_VALUE+1,Short.parseShort(constraint.get("maxExclusive"))-1);
			}
		}else if(constraint.containsKey("maxIxclusive")){
			if(constraint.containsKey("minExclusive")){
				values = shortTypeDate(Short.parseShort(constraint.get("minExclusive"))+1,Short.parseShort(constraint.get("maxIxclusive")));
			}else if(constraint.containsKey("minInclusive")){
				values = shortTypeDate(Short.parseShort(constraint.get("minIxclusive")),Short.parseShort(constraint.get("maxIxclusive")));
			}else{
				values = shortTypeDate(Short.MIN_VALUE+1,Short.parseShort(constraint.get("maxIxclusive")));
			}
		}else{
			values = shortTypeDate(Short.MIN_VALUE+1,Short.MAX_VALUE-1);
		}
		return values;
	}

	private ArrayList<String> shortTypeDate(int minValue, int maxValue) {
		ArrayList<String> t = new ArrayList<>();
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue-1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue+1));
		t.add(String.valueOf((minValue+maxValue)/2));
		return t;
	}

	private ArrayList<String> PositivIntType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if(constraint.containsKey("minExclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.MAX_VALUE-1);
			}
		}else if(constraint.containsKey("minInclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.MAX_VALUE-1);			
			}
		}else if(constraint.containsKey("maxExclusive")){
			if(constraint.containsKey("minExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("minInclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else{
				values = intTypeDate(1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}
		}else if(constraint.containsKey("maxIxclusive")){
			if(constraint.containsKey("minExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}else if(constraint.containsKey("minInclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minIxclusive")),Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(1,Integer.parseInt(constraint.get("maxIxclusive")));
			}
		}else{
			values = intTypeDate(1,Integer.MAX_VALUE-1);
		}
		return values;
	}

	private ArrayList<String> nonPositivIntType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if(constraint.containsKey("minExclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,0);
			}
		}else if(constraint.containsKey("minInclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),0);			
			}
		}else if(constraint.containsKey("maxExclusive")){
			if(constraint.containsKey("minExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("minInclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else{
				values = intTypeDate(Integer.MIN_VALUE+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}
		}else if(constraint.containsKey("maxIxclusive")){
			if(constraint.containsKey("minExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}else if(constraint.containsKey("minInclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minIxclusive")),Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.MIN_VALUE+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}
		}else{
			values = intTypeDate(Integer.MIN_VALUE+1,0);
		}
		return values;
	}

	private ArrayList<String> nonNegativeIntType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if(constraint.containsKey("minExclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.MAX_VALUE-1);
			}
		}else if(constraint.containsKey("minInclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.MAX_VALUE-1);			
			}
		}else if(constraint.containsKey("maxExclusive")){
			if(constraint.containsKey("minExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("minInclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else{
				values = intTypeDate(0,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}
		}else if(constraint.containsKey("maxIxclusive")){
			if(constraint.containsKey("minExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}else if(constraint.containsKey("minInclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minIxclusive")),Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(0,Integer.parseInt(constraint.get("maxIxclusive")));
			}
		}else{
			values = intTypeDate(0,Integer.MAX_VALUE-1);
		}
		return values;
	}

	private ArrayList<String> negativeIntType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if(constraint.containsKey("minExclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,-1);
			}
		}else if(constraint.containsKey("minInclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),-1);			
			}
		}else if(constraint.containsKey("maxExclusive")){
			if(constraint.containsKey("minExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("minInclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else{
				values = intTypeDate(Integer.MIN_VALUE+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}
		}else if(constraint.containsKey("maxIxclusive")){
			if(constraint.containsKey("minExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}else if(constraint.containsKey("minInclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minIxclusive")),Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.MIN_VALUE+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}
		}else{
			values = intTypeDate(Integer.MIN_VALUE+1,-1);
		}
		return values;
	}

	private ArrayList<String> byteType(ConcurrentHashMap<String, String> constraint) {
		// TODO Auto-generated method stub
		ArrayList<String> values = new ArrayList<>();
		if(constraint.containsKey("minExclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = byteTypeDate(Byte.parseByte(constraint.get("minExclusive"))+1,Byte.parseByte(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = byteTypeDate(Byte.parseByte(constraint.get("minExclusive"))+1,Byte.parseByte(constraint.get("maxIxclusive")));
			}else{
				values = byteTypeDate(Byte.parseByte(constraint.get("minExclusive"))+1,Byte.MAX_VALUE-1);
			}
		}else if(constraint.containsKey("minInclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = byteTypeDate(Byte.parseByte(constraint.get("minInclusive")),Byte.parseByte(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = byteTypeDate(Byte.parseByte(constraint.get("minInclusive")),Byte.parseByte(constraint.get("maxIxclusive")));
			}else{
				values = byteTypeDate(Byte.parseByte(constraint.get("minInclusive")),Byte.MAX_VALUE-1);			
			}
		}else if(constraint.containsKey("maxExclusive")){
			if(constraint.containsKey("minExclusive")){
				values = byteTypeDate(Byte.parseByte(constraint.get("minExclusive"))+1,Byte.parseByte(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("minInclusive")){
				values = byteTypeDate(Byte.parseByte(constraint.get("minInclusive")),Byte.parseByte(constraint.get("maxExclusive"))-1);
			}else{
				values = byteTypeDate(Byte.MIN_VALUE+1,Byte.parseByte(constraint.get("maxExclusive"))-1);
			}
		}else if(constraint.containsKey("maxIxclusive")){
			if(constraint.containsKey("minExclusive")){
				values = byteTypeDate(Byte.parseByte(constraint.get("minExclusive"))+1,Byte.parseByte(constraint.get("maxIxclusive")));
			}else if(constraint.containsKey("minInclusive")){
				values = byteTypeDate(Byte.parseByte(constraint.get("minIxclusive")),Byte.parseByte(constraint.get("maxIxclusive")));
			}else{
				values = byteTypeDate(Byte.MIN_VALUE+1,Byte.parseByte(constraint.get("maxIxclusive")));
			}
		}else{
			values = byteTypeDate(Byte.MIN_VALUE+1,Byte.MAX_VALUE-1);
		}
		return values;
	}

	private ArrayList<String> byteTypeDate(int minValue, int maxValue) {
		// TODO Auto-generated method stub
		ArrayList<String> t = new ArrayList<>();
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue-1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue+1));
		t.add(String.valueOf((minValue+maxValue)/2));
		return t;
	}

	private ArrayList<String> intType(ConcurrentHashMap<String, String> constraint) {
		// TODO Auto-generated method stub
		ArrayList<String> values = new ArrayList<>();
		if(constraint.containsKey("minExclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.MAX_VALUE-1);
			}
		}else if(constraint.containsKey("minInclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("maxIxclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.MAX_VALUE-1);			
			}
		}else if(constraint.containsKey("maxExclusive")){
			if(constraint.containsKey("minExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else if(constraint.containsKey("minInclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minInclusive")),Integer.parseInt(constraint.get("maxExclusive"))-1);
			}else{
				values = intTypeDate(Integer.MIN_VALUE+1,Integer.parseInt(constraint.get("maxExclusive"))-1);
			}
		}else if(constraint.containsKey("maxIxclusive")){
			if(constraint.containsKey("minExclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minExclusive"))+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}else if(constraint.containsKey("minInclusive")){
				values = intTypeDate(Integer.parseInt(constraint.get("minIxclusive")),Integer.parseInt(constraint.get("maxIxclusive")));
			}else{
				values = intTypeDate(Integer.MIN_VALUE+1,Integer.parseInt(constraint.get("maxIxclusive")));
			}
		}else{
			values = intTypeDate(Integer.MIN_VALUE+1,Integer.MAX_VALUE-1);
		}
		return values;
	}

	private ArrayList<String> intTypeDate(int minValue, int maxValue) {
		// TODO Auto-generated method stub
		ArrayList<String> t = new ArrayList<>();
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue-1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue+1));
		t.add(String.valueOf((minValue+maxValue)/2));
		return t;
	}

	private ArrayList<String> longType(ConcurrentHashMap<String, String> constraint) {
		// TODO Auto-generated method stub
		ArrayList<String> values = new ArrayList<>();
		if(constraint.containsKey("minExclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = longTypeDate(Long.parseLong(constraint.get("minExclusive"))+1L,Long.parseLong(constraint.get("maxExclusive"))-1L);
			}else if(constraint.containsKey("maxIxclusive")){
				values = longTypeDate(Long.parseLong(constraint.get("minExclusive"))+1L,Long.parseLong(constraint.get("maxIxclusive")));
			}else{
				values = longTypeDate(Long.parseLong(constraint.get("minExclusive"))+1L,Long.MAX_VALUE-1L);
			}
		}else if(constraint.containsKey("minInclusive")){
			if(constraint.containsKey("maxExclusive")){
				values = longTypeDate(Long.parseLong(constraint.get("minInclusive")),Long.parseLong(constraint.get("maxExclusive"))-1L);
			}else if(constraint.containsKey("maxIxclusive")){
				values = longTypeDate(Long.parseLong(constraint.get("minInclusive")),Long.parseLong(constraint.get("maxIxclusive")));
			}else{
				values = longTypeDate(Long.parseLong(constraint.get("minInclusive")),Long.MAX_VALUE-1L);			
			}
		}else if(constraint.containsKey("maxExclusive")){
			if(constraint.containsKey("minExclusive")){
				values = longTypeDate(Long.parseLong(constraint.get("minExclusive"))+1L,Long.parseLong(constraint.get("maxExclusive"))-1L);
			}else if(constraint.containsKey("minInclusive")){
				values = longTypeDate(Long.parseLong(constraint.get("minInclusive")),Long.parseLong(constraint.get("maxExclusive"))-1L);
			}else{
				values = longTypeDate(Long.MIN_VALUE+1L,Long.parseLong(constraint.get("maxExclusive"))-1L);
			}
		}else if(constraint.containsKey("maxIxclusive")){
			if(constraint.containsKey("minExclusive")){
				values = longTypeDate(Long.parseLong(constraint.get("minExclusive"))+1L,Long.parseLong(constraint.get("maxIxclusive")));
			}else if(constraint.containsKey("minInclusive")){
				values = longTypeDate(Long.parseLong(constraint.get("minIxclusive")),Long.parseLong(constraint.get("maxIxclusive")));
			}else{
				values = longTypeDate(Long.MIN_VALUE+1L,Long.parseLong(constraint.get("maxIxclusive")));
			}
		}else{
			values = longTypeDate(Long.MIN_VALUE+1L,Long.MAX_VALUE-1L);
		}
		return values;
		
	}
	/**
	 * 生成边界值，以及边界值外，和中间值共5个
	 * @param l
	 * @param m
	 * @return
	 */
	private ArrayList<String> longTypeDate(long l, long m) {
		// TODO Auto-generated method stub
		ArrayList<String> t = new ArrayList<>();
		t.add(String.valueOf(l));
		t.add(String.valueOf(l-1));
		t.add(String.valueOf(m));
		t.add(String.valueOf(m+1));
		t.add(String.valueOf((m+l)/2));
		return t;
	}


}

