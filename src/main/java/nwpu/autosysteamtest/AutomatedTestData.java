package nwpu.autosysteamtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.org.apache.bcel.internal.generic.RET;

/**
 * 
 * @author Dengtong
 * @version 0.51
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
	static final long MAX_POINT_DIGIT = 15;// 小数点后最大位数
	static final long MAX_TOTAL_DIGIT = 15;// 小数最大位数

	public AutomatedTestData(String path) throws InterruptedException {
		this.path = path;
		documentPrepcessing = DocumentPrepcessing.getInstance();
		this.operaterTypesMap = documentPrepcessing.getOperaterTypesMap();
		this.addInterfaceSetMap = documentPrepcessing.getAddInterfaceSetMap();
		this.deleteInterfaceSetMap = documentPrepcessing.getDeleteInterfaceSetMap();
		this.UpdateInterfaceSetMap = documentPrepcessing.getUpdateInterfaceSetMap();
		this.findInterfaceSetMap = documentPrepcessing.getFindInterfaceSetMap();
		this.parameterConstrainsMap = documentPrepcessing.getParameterConstrainsMap();
		this.run1();
	}

	protected void run1() {
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
			}
		}
	}

	protected void run2() throws FileNotFoundException {
		xAutomated("add", this.addInterfaceSetMap);
		xAutomated("upData", this.UpdateInterfaceSetMap);
		xAutomated("delete", this.deleteInterfaceSetMap);
		// findDataAutomated();
	}

	private void findDataAutomated() {

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
					if (s.startsWith(resourceid + "-" + paramName)) {
						paramConstrains = s;
					}
				}
				analyticParameterComposition(path, param, paramName, paramConstrains);
			}
		}

	}

	private void analyticParameterComposition(String path, String param, String paramName, String paramConstrains) {
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
			case "string":
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
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> fileType(String[] constraints, String paramType) {
		ArrayList<String> values = new ArrayList<>();
		return values;
	}

	private ArrayList<String> boolType() {
		ArrayList<String> values = new ArrayList<>();
		values.add("true");
		values.add("false");
		return values;
	}

	private ArrayList<String> DateType(String[] constraints, String paramType) throws ParseException {
		String constraintses = constraints.toString();
		ArrayList<String> values = new ArrayList<>();
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
				values = dateType(constraint);
				break;
			case "DateTime":
				values = dateTimeType(constraint);
				break;
			case "duration":
				values = durationType(constraint);
				break;
			case "gDay":
				values = gDayType(constraint);
				break;
			case "gMonth":
				values = gMonthType(constraint);
				break;
			case "gMonthDay":
				values = gMonthDayType(constraint);
				break;
			case "gYear":
				values = gYearType(constraint);
				break;
			case "gYearMonth":
			case "time":
			}
		}
		return values;
	}

	private ArrayList<String> gYearType(ConcurrentHashMap<String, String> constraint) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
		ArrayList<String> values = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.YEAR, -1);
				Date minDate = calendar.getTime();
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.YEAR, +1);
				Date maxDate = calendar.getTime();
				values = gYearTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.YEAR, -1);
				Date minDate = calendar.getTime();
				Date maxDate = sdf.parse(constraint.get(Constraints.minInclusive.toString()));
				values = gYearTypeData(minDate,maxDate);
			} else {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.YEAR, -1);
				Date minDate = calendar.getTime();
				values = gYearTypeData(minDate,sdf.parse("9998"));
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.YEAR, +1);
				Date maxDate = calendar.getTime();
				values = gYearTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
				values = gYearTypeData(minDate,maxDate);
			} else {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				values = gYearTypeData(minDate,sdf.parse("9998"));
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
			calendar.add(Calendar.YEAR, +1);
			Date maxDate = calendar.getTime();
			values = gYearTypeData(sdf.parse("0002"),maxDate);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
			values = gYearTypeData(sdf.parse("0002"),maxDate);
		} else {
			values = gYearTypeData(sdf.parse("0002"),sdf.parse("9998"));
		}
		return values;
	}

	private ArrayList<String> gYearTypeData(Date lowtime, Date hightime) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
		Calendar calendar = Calendar.getInstance();
		ArrayList<String> t = new ArrayList<>();
		t.add(sdf.format(lowtime));
		calendar.setTime(lowtime);
		calendar.add(Calendar.YEAR, -1);
		t.add(sdf.format(calendar.getTime()));
		t.add(sdf.format(hightime));
		calendar.setTime(hightime);
		calendar.add(Calendar.YEAR, +1);
		t.add(sdf.format(calendar.getTime()));
		calendar.clear();
		return t;
	}

	private ArrayList<String> gMonthDayType(ConcurrentHashMap<String, String> constraint) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		ArrayList<String> values = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date minDate = calendar.getTime();
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, +1);
				Date maxDate = calendar.getTime();
				values = gMonthDayTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date minDate = calendar.getTime();
				Date maxDate = sdf.parse(constraint.get(Constraints.minInclusive.toString()));
				values = gMonthDayTypeData(minDate,maxDate);
			} else {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date minDate = calendar.getTime();
				values = gMonthDayTypeData(minDate,sdf.parse("12-30"));
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, +1);
				Date maxDate = calendar.getTime();
				values = gMonthDayTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
				values = gMonthDayTypeData(minDate,maxDate);
			} else {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				values = gMonthDayTypeData(minDate,sdf.parse("12-30"));
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
			calendar.add(Calendar.DAY_OF_MONTH, +1);
			Date maxDate = calendar.getTime();
			values = gMonthDayTypeData(sdf.parse("01-02"),maxDate);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
			values = gMonthDayTypeData(sdf.parse("01-02"),maxDate);
		} else {
			values = gMonthDayTypeData(sdf.parse("01-02"),sdf.parse("12-30"));
		}
		return values;
	}

	private ArrayList<String> gMonthDayTypeData(Date lowtime, Date hightime) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		Calendar calendar = Calendar.getInstance();
		ArrayList<String> t = new ArrayList<>();
		t.add(sdf.format(lowtime));
		calendar.setTime(lowtime);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		t.add(sdf.format(calendar.getTime()));
		t.add(sdf.format(hightime));
		calendar.setTime(hightime);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		t.add(sdf.format(calendar.getTime()));
		calendar.clear();
		return t;
	}

	private ArrayList<String> gMonthType(ConcurrentHashMap<String, String> constraint) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		ArrayList<String> values = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.MONTH, -1);
				Date minDate = calendar.getTime();
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.MONTH, +1);
				Date maxDate = calendar.getTime();
				values = gMonthTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.MONTH, -1);
				Date minDate = calendar.getTime();
				Date maxDate = sdf.parse(constraint.get(Constraints.minInclusive.toString()));
				values = gMonthTypeData(minDate,maxDate);
			} else {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.MONTH, -1);
				Date minDate = calendar.getTime();
				values = gMonthTypeData(minDate,sdf.parse("11"));
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.MONTH, +1);
				Date maxDate = calendar.getTime();
				values = gMonthTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
				values = gMonthTypeData(minDate,maxDate);
			} else {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				values = gMonthTypeData(minDate,sdf.parse("11"));
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
			calendar.add(Calendar.MONTH, +1);
			Date maxDate = calendar.getTime();
			values = gMonthTypeData(sdf.parse("02"),maxDate);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
			values = gMonthTypeData(sdf.parse("02"),maxDate);
		} else {
			values = gMonthTypeData(sdf.parse("02"),sdf.parse("11"));
		}
		return values;
	}

	private ArrayList<String> gMonthTypeData(Date lowtime, Date hightime) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Calendar calendar = Calendar.getInstance();
		ArrayList<String> t = new ArrayList<>();
		t.add(sdf.format(lowtime));
		calendar.setTime(lowtime);
		calendar.add(Calendar.MONTH, -1);
		t.add(sdf.format(calendar.getTime()));
		t.add(sdf.format(hightime));
		calendar.setTime(hightime);
		calendar.add(Calendar.MONTH, +1);
		t.add(sdf.format(calendar.getTime()));
		calendar.clear();
		return t;
	}

	private ArrayList<String> gDayType(ConcurrentHashMap<String, String> constraint) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		ArrayList<String> values = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date minDate = calendar.getTime();
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, +1);
				Date maxDate = calendar.getTime();
				values = gDayTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date minDate = calendar.getTime();
				Date maxDate = sdf.parse(constraint.get(Constraints.minInclusive.toString()));
				values = gDayTypeData(minDate,maxDate);
			} else {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date minDate = calendar.getTime();
				values = gDayTypeData(minDate,sdf.parse("30"));
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, +1);
				Date maxDate = calendar.getTime();
				values = gDayTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
				values = gDayTypeData(minDate,maxDate);
			} else {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				values = gDayTypeData(minDate,sdf.parse("30"));
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
			calendar.add(Calendar.DAY_OF_MONTH, +1);
			Date maxDate = calendar.getTime();
			values = gDayTypeData(sdf.parse("02"),maxDate);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
			values = gDayTypeData(sdf.parse("02"),maxDate);
		} else {
			values = gDayTypeData(sdf.parse("02"),sdf.parse("30"));
		}
		return values;
	}

	private ArrayList<String> gDayTypeData(Date lowtime, Date hightime) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Calendar calendar = Calendar.getInstance();
		ArrayList<String> t = new ArrayList<>();
		t.add(sdf.format(lowtime));
		calendar.setTime(lowtime);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		t.add(sdf.format(calendar.getTime()));
		t.add(sdf.format(hightime));
		calendar.setTime(hightime);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		t.add(sdf.format(calendar.getTime()));
		calendar.clear();
		return t;
	}

	private ArrayList<String> durationType(ConcurrentHashMap<String, String> constraint) {
		return null;
	}

	private ArrayList<String> dateTimeType(ConcurrentHashMap<String, String> constraint)  throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");
		ArrayList<String> values = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.SECOND, -1);
				Date minDate = calendar.getTime();
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.SECOND, +1);
				Date maxDate = calendar.getTime();
				values = dateTimeTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.SECOND, -1);
				Date minDate = calendar.getTime();
				Date maxDate = sdf.parse(constraint.get(Constraints.minInclusive.toString()));
				values = dateTimeTypeData(minDate,maxDate);
			} else {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.SECOND, -1);
				Date minDate = calendar.getTime();
				values = dateTimeTypeData(minDate,sdf.parse("9999-12-31T24:59:58"));
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.SECOND, +1);
				Date maxDate = calendar.getTime();
				values = dateTimeTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
				values = dateTimeTypeData(minDate,maxDate);
			} else {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				values = dateTimeTypeData(minDate,sdf.parse("9999-12-31T24:59:58"));
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
			calendar.add(Calendar.SECOND, +1);
			Date maxDate = calendar.getTime();
			values = dateTimeTypeData(sdf.parse("0001-01-01T00:00:02"),maxDate);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
			values = dateTimeTypeData(sdf.parse("0001-01-01T00:00:02"),maxDate);
		} else {
			values = dateTimeTypeData(sdf.parse("0001-01-01T00:00:02"),sdf.parse("9999-12-31T24:59:58"));
		}
		return values;
	}

	private ArrayList<String> dateTimeTypeData(Date lowtime, Date hightime) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		ArrayList<String> t = new ArrayList<>();
		t.add(sdf.format(lowtime));
		calendar.setTime(lowtime);
		calendar.add(Calendar.SECOND, -1);
		t.add(sdf.format(calendar.getTime()));
		t.add(sdf.format(hightime));
		calendar.setTime(hightime);
		calendar.add(Calendar.SECOND, +1);
		t.add(sdf.format(calendar.getTime()));
		calendar.clear();
		return t;
	}

	private ArrayList<String> dateType(ConcurrentHashMap<String, String> constraint) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		ArrayList<String> values = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date minDate = calendar.getTime();
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, +1);
				Date maxDate = calendar.getTime();
				values = dateTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date minDate = calendar.getTime();
				Date maxDate = sdf.parse(constraint.get(Constraints.minInclusive.toString()));
				values = dateTypeData(minDate,maxDate);
			} else {
				calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date minDate = calendar.getTime();
				values = dateTypeData(minDate,sdf.parse("9999-12-30"));
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.DAY_OF_MONTH, +1);
				Date maxDate = calendar.getTime();
				values = dateTypeData(minDate,maxDate);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
				values = dateTypeData(minDate,maxDate);
			} else {
				Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
				values = dateTypeData(minDate,sdf.parse("9999-12-30"));
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
			calendar.add(Calendar.DAY_OF_MONTH, +1);
			Date maxDate = calendar.getTime();
			values = dateTypeData(sdf.parse("0001-01-02"),maxDate);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
			values = dateTypeData(sdf.parse("0001-01-02"),maxDate);
		} else {
			values = dateTypeData(sdf.parse("0001-01-02"),sdf.parse("9999-12-30"));
		}
		return values;
	}

	private ArrayList<String> dateTypeData(Date lowtime, Date hightime) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		Calendar calendar = Calendar.getInstance();
		ArrayList<String> t = new ArrayList<>();
		t.add(sdf.format(lowtime));
		calendar.setTime(lowtime);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		t.add(sdf.format(calendar.getTime()));
		t.add(sdf.format(hightime));
		calendar.setTime(hightime);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		t.add(sdf.format(calendar.getTime()));
		calendar.clear();
		return t;
	}

	private ArrayList<String> stringType(String[] constraints, String paramType) {

		String constraintses = constraints.toString();
		ArrayList<String> values = new ArrayList<>();
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
			case "string":
				values = stringType(constraint);
				break;
			case "token":
				values = tokenType(constraint);
				break;
			default:
				break;
			}
		}
		return values;
	}

	private ArrayList<String> tokenType(ConcurrentHashMap<String, String> constraint) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> stringType(ConcurrentHashMap<String, String> constraint) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> numericalType(String[] constraints, String paramType) {

		String constraintses = constraints.toString();
		ArrayList<String> values = new ArrayList<>();
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
				values = shortType(constraint);
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
				//
				break;
			case "float":
				//
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

	private ArrayList<String> shortType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = shortTypeData(Short.parseShort(constraint.get(Constraints.minExclusive.toString())) + 1,
						Short.parseShort(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = shortTypeData(Short.parseShort(constraint.get(Constraints.minExclusive.toString())) + 1,
						Short.parseShort(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = shortTypeData(Short.parseShort(constraint.get(Constraints.minExclusive.toString())) + 1, Short.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = shortTypeData(Short.parseShort(constraint.get(Constraints.minInclusive.toString())),
						Short.parseShort(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = shortTypeData(Short.parseShort(constraint.get(Constraints.minInclusive.toString())),
						Short.parseShort(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = shortTypeData(Short.parseShort(constraint.get(Constraints.minInclusive.toString())), Short.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			values = shortTypeData(Short.MIN_VALUE + 1, Short.parseShort(constraint.get(Constraints.maxExclusive.toString())) - 1);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			values = shortTypeData(Short.MIN_VALUE + 1, Short.parseShort(constraint.get(Constraints.maxInclusive.toString())));
		} else {
			values = shortTypeData(Short.MIN_VALUE + 1, Short.MAX_VALUE - 1);
		}
		return values;
	}

	private ArrayList<String> shortTypeData(int minValue, int maxValue) {
		ArrayList<String> t = new ArrayList<>();
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue - 1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue + 1));
		t.add(String.valueOf((minValue + maxValue) / 2));
		return t;
	}

	private ArrayList<String> PositivIntType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1, Integer.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())), Integer.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(1, Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(1, Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
		} else {
			values = intTypeData(1, Integer.MAX_VALUE - 1);
		}
		return values;
	}

	private ArrayList<String> nonPositivIntType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1, 0);
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())), 0);
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.MIN_VALUE + 1, 
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.MIN_VALUE + 1, 
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
		} else {
			values = intTypeData(Integer.MIN_VALUE + 1, 0);
		}
		return values;
	}

	private ArrayList<String> nonNegativeIntType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1, 
						Integer.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())), 
						Integer.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(0, Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(0, Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
		} else {
			values = intTypeData(0, Integer.MAX_VALUE - 1);
		}
		return values;
	}

	private ArrayList<String> negativeIntType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1, -1);
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())), -1);
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.MIN_VALUE + 1, 
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.MIN_VALUE + 1, 
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
		} else {
			values = intTypeData(Integer.MIN_VALUE + 1, -1);
		}
		return values;
	}

	private ArrayList<String> byteType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = byteTypeData(Byte.parseByte(constraint.get(Constraints.minExclusive.toString())) + 1,
						Byte.parseByte(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = byteTypeData(Byte.parseByte(constraint.get(Constraints.minExclusive.toString())) + 1,
						Byte.parseByte(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = byteTypeData(Byte.parseByte(constraint.get(Constraints.minExclusive.toString())) + 1, Byte.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = byteTypeData(Byte.parseByte(constraint.get(Constraints.minInclusive.toString())),
						Byte.parseByte(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = byteTypeData(Byte.parseByte(constraint.get(Constraints.minInclusive.toString())),
						Byte.parseByte(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = byteTypeData(Byte.parseByte(constraint.get(Constraints.minInclusive.toString())), Byte.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			values = byteTypeData(Byte.MIN_VALUE + 1, Byte.parseByte(constraint.get(Constraints.maxExclusive.toString())) - 1);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			values = byteTypeData(Byte.MIN_VALUE + 1, Byte.parseByte(constraint.get(Constraints.maxInclusive.toString())));
		} else {
			values = byteTypeData(Byte.MIN_VALUE + 1, Byte.MAX_VALUE - 1);
		}
		return values;
	}

	private ArrayList<String> byteTypeData(int minValue, int maxValue) {

		ArrayList<String> t = new ArrayList<>();
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue - 1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue + 1));
		t.add(String.valueOf((minValue + maxValue) / 2));
		return t;
	}

	private ArrayList<String> intType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1, Integer.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = intTypeData(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())), Integer.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			values = intTypeData(Integer.MIN_VALUE + 1, Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			values = intTypeData(Integer.MIN_VALUE + 1, Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
		} else {
			values = intTypeData(Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
		}
		return values;
	}

	private ArrayList<String> intTypeData(int minValue, int maxValue) {

		ArrayList<String> t = new ArrayList<>();
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue - 1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue + 1));
		t.add(String.valueOf((minValue + maxValue) / 2));
		return t;
	}

	private ArrayList<String> longType(ConcurrentHashMap<String, String> constraint) {
		ArrayList<String> values = new ArrayList<>();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = longTypeData(Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
						Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = longTypeData(Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
						Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = longTypeData(Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L, Long.MAX_VALUE - 1L);
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = longTypeData(Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
						Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = longTypeData(Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
						Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = longTypeData(Long.parseLong(constraint.get(Constraints.minInclusive.toString())), Long.MAX_VALUE - 1L);
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			values = longTypeData(Long.MIN_VALUE + 1L, Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			values = longTypeData(Long.MIN_VALUE + 1L, Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
		} else {
			values = longTypeData(Long.MIN_VALUE + 1L, Long.MAX_VALUE - 1L);
		}
		return values;
	}

	/**
	 * 生成边界值，以及边界值外，和中间值共5个
	 * 
	 * @param l
	 * @param m
	 * @return
	 */
	private ArrayList<String> longTypeData(long l, long m) {

		ArrayList<String> t = new ArrayList<>();
		t.add(String.valueOf(l));
		t.add(String.valueOf(l - 1));
		t.add(String.valueOf(m));
		t.add(String.valueOf(m + 1));
		t.add(String.valueOf((m + l) / 2));
		return t;
	}

}
enum Constraints{
	enumeration
	,totalDigits
	,fractionDigit
	,minExclusive
	,maxExclusive
	,minInclusive
	,maxInclusive
	,length
	,minLength
	,maxLength
	,pattern
	,format
	,size
	,minSize
	,maxSize
	,whiteSpace
}