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
import nwpu.autosysteamtest.enity.Operation;
import nwpu.autosysteamtest.enity.RequestElement;
import nwpu.autosysteamtest.enity.RequestParam;
import nwpu.autosysteamtest.enity.Service;

/**
 * 
 * @author Dengtong
 * @version 0.90,07/01/2018
 */
public class AutomatedTestData {
	DocumentPrepcessing documentPrepcessing;
	private ConcurrentHashMap<String, String> operaterTypesMap;
	protected Service service;
	protected String resourcesid;
	protected String path;

	public AutomatedTestData(String path) throws InterruptedException {
		this.path = path;
		documentPrepcessing = DocumentPrepcessing.getInstance();
		this.operaterTypesMap = documentPrepcessing.getOperaterTypesMap();
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
			service = documentPrepcessing.searchServiceById(resourcesid);
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
		xAutomated("add", service.getAdds());
		xAutomated("upData", service.getUpdates());
		xAutomated("delete", service.getDeletes());
		xAutomated("find", service.getFinds());
	}

	private void xAutomated(String operation, ArrayList<Operation> xInterfaces)
			throws FileNotFoundException, ParseException {
		for (Operation xInterface : xInterfaces) {
			String resourceid = xInterface.getId();
			File file = null;
			try {
				file = new File(path + this.resourcesid + "\\" + operation + "\\" + resourceid);
				if (!file.exists()) {
					file.mkdirs();
				}
			} catch (Exception e) {

			} finally {
				file = null;
			}
			analyticParameter(this.path + this.resourcesid + "\\" + operation + "\\" + resourceid + "\\", xInterface);
		}
	}

	private void analyticParameter(String path, Operation xInterface) throws ParseException {
		ArrayList<RequestParam> params = xInterface.getRequestParams();
		PrintWriter out = null;
		for (RequestParam param : params) {
			try {
				out = new PrintWriter(path + param.getName() + ".xml");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String paramAtributte = param.getAttribute();
			String paramStatus = param.getLocation();
			String paramName = param.getName();
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.flush();
			if ("false".equals(paramStatus)) {
				out.println(
						"<param name=\"" + paramName + "\" attribute=\"" + paramAtributte + "\" status=\"generate\">");
				out.flush();
			} else {
				out.println("<param name=\"" + paramName + "\" attribute=\"" + paramAtributte + "\" status=\""
						+ paramStatus + "\">");
				out.flush();
			}
			analyticParameterComposition(path, param, out, paramStatus);
			out.println("</param>");
			out.flush();
			out.close();
		}
	}

	private void analyticParameterComposition(String path, RequestParam param, PrintWriter out, String paramStatus)
			throws ParseException {
		String paramType = param.getType();
		boolean flag = false;
		if ("false".equals(paramStatus)) {
			ArrayList<String> values = new ArrayList<>();
			ArrayList<String> constraints = param.getConstraint();
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
					values = dateType(constraints, paramType);
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
				values = ObjectType(param,out);
				flag = true;
				break;
			}
			for (String value : values) {
				if (!flag) {
					out.println("	<value>" + value + "</value>");
					out.flush();
				}
			}
		} else {
		}

	}

	private ArrayList<String> ObjectType(RequestParam param,PrintWriter out) {
		ArrayList<RequestElement> elements = param.getElements();
		for (RequestElement element : elements){
			String elementName = element.getName();
		}
		return null;
	}

	private ArrayList<String> setTypeType(ArrayList<String> constraints, String paramType) {
		return null;
	}

	private ArrayList<String> fileType(ArrayList<String> constraints, String paramType) {
		ArrayList<String> values = new ArrayList<>();
		if (constraints != null) {
			if (constraints.contains("enumeration")) {
				for (String enumerations : constraints) {
					values.add(enumerations.split(":")[1]);
				}
			} else {
				ConcurrentHashMap<String, String> constraint = new ConcurrentHashMap<>();
				for (String t : constraints) {
					String[] tt = t.split(":");
					constraint.put(tt[0], tt[1]);
				}
				FileData fd = new FileData(constraint);
				try {
					values = fd.constraintAnalysis();
				} catch (ParseException e) {

					e.printStackTrace();
				}
			}
		} else {
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

	private ArrayList<String> dateType(ArrayList<String> constraints, String paramType) throws ParseException {
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
					String[] tt = t.split(":");
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
				default:
					System.err.println("do not have type");
					break;
				}
			}

		} else {
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
				// values = durationType();
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
			default:
				System.err.println("do not have data type");
				break;
			}
		}
		return values;
	}

	private ArrayList<String> durationType(ConcurrentHashMap<String, String> constraint) {
		return null;
	}

	private ArrayList<String> stringType(ArrayList<String> constraints, String paramType) throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		if (constraints != null) {
			String constraintses = null;
			for (String p : constraints) {
				constraintses += p;
			}
			if (constraintses.contains("enumeration")) {
				for (String enumerations : constraints) {
					values.add(enumerations.split(":")[1]);
				}
			} else {
				ConcurrentHashMap<String, String> constraint = new ConcurrentHashMap<>();
				for (String t : constraints) {
					String[] tt = t.split(":");
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

	private ArrayList<String> numericalType(ArrayList<String> constraints, String paramType) {
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
					String[] tt = t.split(":");
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
					UnsignedLongData uld = new UnsignedLongData(constraint);
					try {
						values = uld.constraintAnalysis();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					break;
				case "unsignedInt":
					UnsignedIntData uid = new UnsignedIntData(constraint);
					values = uid.constraintAnalysis();
					break;
				case "unsignedShort":
					UnsignedShortData usd = new UnsignedShortData(constraint);
					values = usd.constraintAnalysis();
					break;
				case "unsignedByte":
					UnsignedByteData ubd = new UnsignedByteData(constraint);
					values = ubd.constraintAnalysis();
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
					DoubleData dd = new DoubleData(constraint);
					try {
						values = dd.constraintAnalysis();
					} catch (ParseException e) {
						e.printStackTrace();
					}
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
				UnsignedLongData uld = new UnsignedLongData();
				try {
					values = uld.constraintAnalysis();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				break;
			case "unsignedInt":
				UnsignedIntData uid = new UnsignedIntData();
				values = uid.constraintAnalysis();
				break;
			case "unsignedShort":
				UnsignedShortData usd = new UnsignedShortData();
				values = usd.constraintAnalysis();
				break;
			case "unsignedByte":
				UnsignedByteData ubd = new UnsignedByteData();
				values = ubd.constraintAnalysis();
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
				DoubleData dd = new DoubleData();
				try {
					values = dd.constraintAnalysis();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
		return values;
	}

}

enum Constraints {
	// 约束
	enumeration, totalDigits, fractionDigit, minExclusive, maxExclusive, minInclusive, maxInclusive, length, minLength, maxLength, pattern, format, size, minSize, maxSize, whiteSpace
}