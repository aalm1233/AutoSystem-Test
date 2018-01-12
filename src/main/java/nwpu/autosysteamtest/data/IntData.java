package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.tools.PatternAnalysisNum;

/**
 * int型数据生成
 * 
 * @author Dengtong
 * @version 1.0,07/01/2018
 */
public class IntData extends BaseNumericalData {

	public IntData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}

	public IntData() {
		super();
	}

	@Override
	public ArrayList<String> constraintAnalysis() {
		ArrayList<String> values = new ArrayList<>();
		String pattern = null;
		if (constraint == null) {
			values = dataGeneration(pattern, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
		} else {
			if (constraint.containsKey(Constraints.pattern.toString())) {
				pattern = constraint.get(Constraints.pattern.toString());
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								Integer.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
								Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
								Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
								Integer.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern, Integer.MIN_VALUE + 1,
							Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern, Integer.MIN_VALUE + 1,
							Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
				}
			} else {
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								Integer.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
								Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
								Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,
								Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
								Integer.MAX_VALUE - 1);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern, Integer.MIN_VALUE + 1,
							Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern, Integer.MIN_VALUE + 1,
							Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
				}
			}
		}
		return values;
	}

	@Override
	ArrayList<String> dataGeneration(String pattern, long minValue, long maxValue) {
		if (pattern == null) {
			pattern = "-{0,1}[0-9]{1,}";
		}
		ArrayList<String> t = new ArrayList<>();
		PatternAnalysisNum pa = new PatternAnalysisNum(pattern);
		t.addAll(pa.getValues(minValue, maxValue));
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue - 1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue + 1));
		t.add(String.valueOf((minValue + maxValue) / 2));
		System.out.println(t.toString());
		return t;
	}

}
