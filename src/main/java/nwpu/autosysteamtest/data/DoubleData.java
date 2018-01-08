package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.tools.PatternAnalysis;
import nwpu.autosysteamtest.tools.PatternAnalysisNum;
/**
 * Double型数据生成
 * @author Dengtong
 * @version 1.0,08/01/2018
 */
public class DoubleData extends DecimalData{
	public DoubleData(){
		super();
	}
	public DoubleData(ConcurrentHashMap<String, String> constraint) {
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
			values = dataGeneration(pattern,Double.MIN_VALUE, Double.MAX_VALUE - 1);
		} else {
			if (constraint.containsKey(Constraints.pattern.toString())) {
				pattern = constraint.get(Constraints.pattern.toString());
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minExclusive.toString())) + 1,
								Double.parseDouble(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minExclusive.toString())) + 1,
								Double.parseDouble(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minExclusive.toString())) + 1,
								Double.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minInclusive.toString())),
								Double.parseDouble(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minInclusive.toString())),
								Double.parseDouble(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minInclusive.toString())),
								Double.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,Double.MIN_VALUE + 1,
							Double.parseDouble(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,Double.MIN_VALUE + 1,
							Double.parseDouble(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,Double.MIN_VALUE + 1, Double.MAX_VALUE - 1);
				}
			}else{
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minExclusive.toString())) + 1,
								Double.parseDouble(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minExclusive.toString())) + 1,
								Double.parseDouble(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minExclusive.toString())) + 1,
								Double.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minInclusive.toString())),
								Double.parseDouble(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minInclusive.toString())),
								Double.parseDouble(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Double.parseDouble(constraint.get(Constraints.minInclusive.toString())),
								Double.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,Double.MIN_VALUE + 1,
							Double.parseDouble(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,Double.MIN_VALUE + 1,
							Double.parseDouble(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,Double.MIN_VALUE + 1, Double.MAX_VALUE - 1);
				}
			}
		}
		return values;
	}
}
