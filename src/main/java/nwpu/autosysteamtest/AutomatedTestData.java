package nwpu.autosysteamtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.data.*;

/**
 * 
 * @author Dengtong
 * @version 0.90,07/01/2018
 */
public class AutomatedTestData {
	DocumentPrepcessing documentPrepcessing;
	private ConcurrentHashMap<String, String> operaterTypesMap;
	protected ConcurrentHashMap<String, ArrayList<String>> addInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> deleteInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> UpdateInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> findInterfaceSetMap;
	protected ConcurrentHashMap<String, ArrayList<String>> parameterConstrainsMap;
	protected String resourcesid;
	protected String resourceid;
	protected String path;

	public AutomatedTestData(String path) throws InterruptedException {
		this.path = path;
		documentPrepcessing = DocumentPrepcessing.getInstance();
		this.operaterTypesMap = documentPrepcessing.getOperaterTypesMap();
		this.addInterfaceSetMap = documentPrepcessing.getAddInterfaceSetMap();
		this.deleteInterfaceSetMap = documentPrepcessing.getDeleteInterfaceSetMap();
		this.UpdateInterfaceSetMap = documentPrepcessing.getUpdateInterfaceSetMap();
		this.findInterfaceSetMap = documentPrepcessing.getFindInterfaceSetMap();
		this.parameterConstrainsMap = documentPrepcessing.getParameterConstrainsMap();
	}

	public void run1() {
		Set<String> key = operaterTypesMap.keySet();
		File file = null;
		try {
			file = new File(path + "Data");
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {

		} finally {
			file = null;
		}
		this.path = path + "Data\\";
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
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	protected void run2() throws FileNotFoundException, ParseException {
		xAutomated("add", this.addInterfaceSetMap);
		xAutomated("upData", this.UpdateInterfaceSetMap);
		xAutomated("delete", this.deleteInterfaceSetMap);
		xAutomated("find", this.findInterfaceSetMap);
	}

	private void xAutomated(String operation, ConcurrentHashMap<String, ArrayList<String>> xInterfaceSetMap)
			throws FileNotFoundException, ParseException {
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

	private void analyticParameter(String path, String xInterface, ArrayList<String> parameterConstrains) throws ParseException {
		String[] paramsplt = xInterface.split("->");
		if (paramsplt.length > 1) {
			String[] params = paramsplt[1].split("_");
			for (String param : params) {
				String paramName = param.split(",")[0];
				String paramConstrains = null;
				for (String s : parameterConstrains) {
					if (s.startsWith(resourceid + "-" + paramName)) {
						paramConstrains = s;
					}
				}
				analyticParameterComposition(path, param, paramName, paramConstrains);
			}
		}

	}

	private void analyticParameterComposition(String path, String param, String paramName, String paramConstrains) throws ParseException {
		String paramType = param.split(",")[1];
		String paramAtributte = param.split(",")[2];
		String paramStatus = param.split(",")[3];
		if ("false".equals(paramStatus)) {
			ArrayList<String> values = new ArrayList<>();
			String[] constraints = null;
			if (paramConstrains != null && paramConstrains.contains("#")) {
				constraints = paramConstrains.split("<")[1].split(">")[0].split("#");
			} else if (paramConstrains != null) {
				constraints = paramConstrains.split("<")[1].split(">");
			}
			switch (paramType) {
			case "byte":
			case "decimal":
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
				values = numericalType(constraints, paramType);
				break;
			case "String":
			case "token":
				values = stringType(constraints, paramType);
				break;
			case "Date":
			case "DateTime":
			case "duration":
			case "gDay":
			case "gMonth":
			case "gMonthDay":
			case "gYear":
			case "gYearMonth":
			case "time":
				try {
					values = DateType(constraints, paramType);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				break;
			case "boolean":
				values = boolType();
				break;
			case "anyURI":
				values = fileType(constraints, paramType);
				break;
			case "setType":
				values = setTypeType(constraints, paramType);
				break;
			default:
				break;
			}
			try {

				PrintWriter out = new PrintWriter(path + paramName + ".xml");
				out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				out.flush();
				out.println(
						"<param name=\"" + paramName + "\" attribute=\"" + paramAtributte + "\" status=\"generate\">");
				out.flush();
				for (String value : values) {
					out.println("	<value>" + value + "</value>");
					out.flush();
				}
				out.println("</param>");
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else if ("true".equals(paramStatus)) {
			try {
				PrintWriter out = new PrintWriter(path + paramName + ".xml");
				out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				out.flush();
				out.println(
						"<param name=\"" + paramName + "\" attribute=\"" + paramAtributte + "\" status=\"extend\">");
				out.flush();
				out.println("</param>");
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	private ArrayList<String> setTypeType(String[] constraints, String paramType) {
		return null;
	}

	private ArrayList<String> fileType(String[] constraints, String paramType) {
		ArrayList<String> values = new ArrayList<>();
		if (constraints != null) {
			String constraintses = constraints.toString();
			if (constraintses.contains("enumeration")) {
				for (String enumerations : constraints) {
					values.add(enumerations.split(":")[1]);
				}
			} else {
				ConcurrentHashMap<String, String> constraint = new ConcurrentHashMap<>();
				for (String t : constraints) {
					String[] tt = t.split(",");
					constraint.put(tt[0], tt[1]);
				}
				FileData fd = new FileData(constraint);
				try {
					values = fd.constraintAnalysis();
				} catch (ParseException e) {

					e.printStackTrace();
				}
			}

		}else{
			FileData fd = new FileData();
			try {
				values = fd.constraintAnalysis();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	private ArrayList<String> boolType() {
		ArrayList<String> values = new ArrayList<>();
		values.add("true");
		values.add("false");
		values.add("1");
		values.add("0");
		return values;
	}

	private ArrayList<String> DateType(String[] constraints, String paramType) throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		if (constraints != null) {
			String constraintses = constraints.toString();
			if (constraintses.contains("enumeration")) {
				for (String enumerations : constraints) {
					values.add(enumerations.split(":")[1]);
				}
			} else {
				ConcurrentHashMap<String, String> constraint = new ConcurrentHashMap<>();
				for (String t : constraints) {
					String[] tt = t.split(",");
					constraint.put(tt[0], tt[1]);
				}
				switch (paramType) {
				case "Date":
					DatesData dd = new DatesData(constraint);
					values = dd.constraintAnalysis();
					break;
				case "DateTime":
					DateTimeData dtd = new DateTimeData(constraint);
					values = dtd.constraintAnalysis();
					break;
				case "duration":
					values = durationType(constraint);
					break;
				case "gDay":
					GDayData gdd = new GDayData(constraint);
					values = gdd.constraintAnalysis();
					break;
				case "gMonth":
					GMonthData gmd = new GMonthData(constraint);
					values = gmd.constraintAnalysis();
					break;
				case "gMonthDay":
					GMonthDayData gmdd = new GMonthDayData(constraint);
					values = gmdd.constraintAnalysis();
					break;
				case "gYear":
					GYearData gyd = new GYearData(constraint);
					values = gyd.constraintAnalysis();
					break;
				case "gYearMonth":
					GYearMonthData gymd = new GYearMonthData(constraint);
					values = gymd.constraintAnalysis();
					break;
				case "time":
					TimeData td = new TimeData(constraint);
					values = td.constraintAnalysis();
					break;
				}
			}

		}else{
			switch (paramType) {
			case "Date":
				DatesData dd = new DatesData();
				values = dd.constraintAnalysis();
				break;
			case "DateTime":
				DateTimeData dtd = new DateTimeData();
				values = dtd.constraintAnalysis();
				break;
			case "duration":
				//values = durationType();
				break;
			case "gDay":
				GDayData gdd = new GDayData();
				values = gdd.constraintAnalysis();
				break;
			case "gMonth":
				GMonthData gmd = new GMonthData();
				values = gmd.constraintAnalysis();
				break;
			case "gMonthDay":
				GMonthDayData gmdd = new GMonthDayData();
				values = gmdd.constraintAnalysis();
				break;
			case "gYear":
				GYearData gyd = new GYearData();
				values = gyd.constraintAnalysis();
				break;
			case "gYearMonth":
				GYearMonthData gymd = new GYearMonthData();
				values = gymd.constraintAnalysis();
				break;
			case "time":
				TimeData td = new TimeData();
				values = td.constraintAnalysis();
				break;
			}
		}
		return values;
	}

	private ArrayList<String> durationType(ConcurrentHashMap<String, String> constraint) {
		return null;
	}

	private ArrayList<String> stringType(String[] constraints, String paramType) throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		if (constraints != null) {
			String constraintses = null;
			for(String p :constraints){
				constraintses += p;
			}
			if (constraintses.contains("enumeration")) {
				for (String enumerations : constraints) {
					values.add(enumerations.split(":")[1]);
				}
			} else {
				ConcurrentHashMap<String, String> constraint = new ConcurrentHashMap<>();
				for (String t : constraints) {
					String[] tt = t.split(",");
					constraint.put(tt[0], tt[1]);
				}
				switch (paramType) {
				case "String":
					StringData sd = new StringData(constraint);
					values = sd.constraintAnalysis();
					break;
				case "token":
					TokenData td = new TokenData(constraint);
					values = td.constraintAnalysis();
					break;
				default:
					break;
				}
			}
		} else {
			switch (paramType) {
			case "String":
				StringData sd = new StringData();
				values = sd.constraintAnalysis();
				break;
			case "token":
				TokenData td = new TokenData();
				values = td.constraintAnalysis();
				break;
			default:
				break;
			}
		}
		return values;
	}


	private ArrayList<String> numericalType(String[] constraints, String paramType) {
		ArrayList<String> values = new ArrayList<>();
		if (constraints != null) {
			String constraintses = constraints.toString();
			if (constraintses.contains("enumeration")) {
				for (String enumerations : constraints) {
					values.add(enumerations.split(":")[1]);
				}
			} else {
				ConcurrentHashMap<String, String> constraint = new ConcurrentHashMap<>();
				for (String t : constraints) {
					String[] tt = t.split(",");
					constraint.put(tt[0], tt[1]);
				}
				switch (paramType) {
				case "byte":
					ByteData bd = new ByteData(constraint);
					values = bd.constraintAnalysis();
					break;
				case "int":
				case "integer":
					IntData id = new IntData(constraint);
					values = id.constraintAnalysis();
					break;
				case "long":
					LongData ld = new LongData(constraint);
					values = ld.constraintAnalysis();
					break;
				case "negativeInteger":
					NegativeIntData nid = new NegativeIntData(constraint);
					values = nid.constraintAnalysis();
					break;
				case "nonNegativeInteger":
					NonNegativeIntData nnid = new NonNegativeIntData(constraint);
					values = nnid.constraintAnalysis();
					break;
				case "nonPositiveInteger":
					NonPositiveIntegerData nptd = new NonPositiveIntegerData(constraint);
					values = nptd.constraintAnalysis();
					break;
				case "positiveInteger":
					PositiveIntegerData pid = new PositiveIntegerData(constraint);
					values = pid.constraintAnalysis();
					break;
				case "short":
					ShortData sd = new ShortData(constraint);
					values = sd.constraintAnalysis();
					break;
				case "unsignedLong":
					values = unsignedLongType(constraint);
					break;
				case "unsignedInt":
					values = unsignedIntType(constraint);
					break;
				case "unsignedShort":
					values = unsignedShortType(constraint);
					break;
				case "unsignedByte":
					values = unsignedByteType(constraint);
					break;
				case "decimal":
				case "float":
					DecimalTypeData dtd = new DecimalTypeData(constraint);
					try {
						values = dtd.constraintAnalysis();
					} catch (ParseException e) {
	
						e.printStackTrace();
					}
					break;
				case "double":
					//
					break;
				default:
					break;
				}
			}
		} else {
			switch (paramType) {
			case "byte":
				ByteData bd = new ByteData();
				values = bd.constraintAnalysis();
				break;
			case "int":
			case "integer":
				IntData id = new IntData();
				values = id.constraintAnalysis();
				break;
			case "long":
				LongData ld = new LongData();
				values = ld.constraintAnalysis();
				break;
			case "negativeInteger":
				NegativeIntData nid = new NegativeIntData();
				values = nid.constraintAnalysis();
				break;
			case "nonNegativeInteger":
				NonNegativeIntData nnid = new NonNegativeIntData();
				values = nnid.constraintAnalysis();
				break;
			case "nonPositiveInteger":
				NonPositiveIntegerData nptd = new NonPositiveIntegerData();
				values = nptd.constraintAnalysis();
				break;
			case "positiveInteger":
				PositiveIntegerData pid = new PositiveIntegerData();
				values = pid.constraintAnalysis();
				break;
			case "short":
				ShortData sd = new ShortData();
				values = sd.constraintAnalysis();
				break;
			case "unsignedLong":
				// values = unsignedLongType();
				break;
			case "unsignedInt":
				// values = unsignedIntType();
				break;
			case "unsignedShort":
				// values = unsignedShortType();
				break;
			case "unsignedByte":
				// values = unsignedByteType();
				break;
			case "decimal":
			case "float":
				DecimalTypeData dtd = new DecimalTypeData();
				try {
					values = dtd.constraintAnalysis();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case "double":
				//
				break;
			default:
				break;
			}
		}
		return values;
	}

	private ArrayList<String> unsignedByteType(ConcurrentHashMap<String, String> constraint) {
		return null;
	}

	private ArrayList<String> unsignedShortType(ConcurrentHashMap<String, String> constraint) {
		return null;
	}

	private ArrayList<String> unsignedIntType(ConcurrentHashMap<String, String> constraint) {
		return null;
	}

	private ArrayList<String> unsignedLongType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		return values;
	}

}

enum Constraints {
	enumeration, totalDigits, fractionDigit, minExclusive, maxExclusive, minInclusive, maxInclusive, length, minLength, maxLength, pattern, format, size, minSize, maxSize, whiteSpace
}