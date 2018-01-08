package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.tools.PatternAnalysisNum;
/**
 * 浮点数数据生成
 * @author Dengtong
 * @version 1.0,08/01/2018
 */
public class DecimalTypeData extends DecimalData{

	public DecimalTypeData(){
		super();
	}
	public DecimalTypeData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}
	@Override
	ArrayList<String> dataGeneration(String pattern,double minValue, double maxValue) {
		if (pattern == null) {
			pattern = "-{0,1}[0-9]{1,}\\.[0-9]{1,}";
		}
		ArrayList<String> t = new ArrayList<>();
		PatternAnalysisNum pa = new PatternAnalysisNum(pattern);
		t.addAll(pa.getValues(minValue, maxValue));
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue - 1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue + 1));
		t.add(String.valueOf((minValue + maxValue) / 2));
		return t;
	}

	@Override
	public ArrayList<String> constraintAnalysis() throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		String pattern = null;
		if (constraint == null) {
			values = dataGeneration(pattern,Float.MIN_VALUE, Float.MAX_VALUE - 1);
		} else {
			if (constraint.containsKey(Constraints.pattern.toString())) {
				pattern = constraint.get(Constraints.pattern.toString());
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minExclusive.toString())) + 1,
								Float.parseFloat(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minExclusive.toString())) + 1,
								Float.parseFloat(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minExclusive.toString())) + 1,
								Float.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minInclusive.toString())),
								Float.parseFloat(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minInclusive.toString())),
								Float.parseFloat(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minInclusive.toString())),
								Float.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,Float.MIN_VALUE + 1,
							Float.parseFloat(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,Float.MIN_VALUE + 1,
							Float.parseFloat(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,Float.MIN_VALUE + 1, Float.MAX_VALUE - 1);
				}
			}else{
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minExclusive.toString())) + 1,
								Float.parseFloat(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minExclusive.toString())) + 1,
								Float.parseFloat(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minExclusive.toString())) + 1,
								Float.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minInclusive.toString())),
								Float.parseFloat(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minInclusive.toString())),
								Float.parseFloat(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Float.parseFloat(constraint.get(Constraints.minInclusive.toString())),
								Float.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,Float.MIN_VALUE + 1,
							Float.parseFloat(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,Float.MIN_VALUE + 1,
							Float.parseFloat(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,Float.MIN_VALUE + 1, Float.MAX_VALUE - 1);
				}
			}
		}
		return values;
	}

}
