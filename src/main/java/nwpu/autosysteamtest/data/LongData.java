package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.tools.PatternAnalysis;
import nwpu.autosysteamtest.tools.PatternAnalysisNum;

/**
 * Long型数据生成
 * 
 * @author Dengtong
 * @version 1.0,07/01/2018
 */
public class LongData extends NumericalData {

	public LongData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}

	public LongData() {
		super();
	}

	@Override
	public ArrayList<String> constraintAnalysis() {
		ArrayList<String> values = new ArrayList<>();
		String pattern = null;
		if (constraint == null) {
			values = dataGeneration(pattern,Long.MIN_VALUE + 1L, Long.MAX_VALUE - 1L);
		} else {
			if(constraint.containsKey(Constraints.pattern.toString())){
				pattern = constraint.get(Constraints.pattern.toString());
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
								Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
								Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
								Long.MAX_VALUE - 1L);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								Long.MAX_VALUE - 1L);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,Long.MIN_VALUE + 1L,
							Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,Long.MIN_VALUE + 1L,
							Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,Long.MIN_VALUE + 1L, Long.MAX_VALUE - 1L);
				}
			}else{
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
								Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
								Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
								Long.MAX_VALUE - 1L);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								Long.MAX_VALUE - 1L);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,Long.MIN_VALUE + 1L,
							Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,Long.MIN_VALUE + 1L,
							Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,Long.MIN_VALUE + 1L, Long.MAX_VALUE - 1L);
				}
			}
		}
		return values;
	}

	@Override
	ArrayList<String> dataGeneration(String pattern,long minValue, long maxValue) {
		if (pattern == null) {
			pattern = "-{0,1}[0-9]{1,}L";
		}
		ArrayList<String> t = new ArrayList<>();
		PatternAnalysisNum pa = new PatternAnalysisNum(pattern);
		t.addAll(pa.getValues(minValue, maxValue));
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue - 1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue + 1));
		t.add(String.valueOf((maxValue + minValue) / 2));
		return t;
	}

}
