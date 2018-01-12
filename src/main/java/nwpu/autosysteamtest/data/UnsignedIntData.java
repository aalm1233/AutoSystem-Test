package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.tools.PatternAnalysisNum;

public class UnsignedIntData extends NumericalData{
	
	public static final Long MAX_UNSIGNEDINT = (long) ((Integer.MAX_VALUE ))  + (long) ((Integer.MAX_VALUE )) +1;
	public UnsignedIntData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}
	public UnsignedIntData() {
		super();
	}

	@Override
	ArrayList<String> dataGeneration(String pattern, long minValue, long maxValue) {
		if (pattern == null) {
			pattern = "[0-9]{1,}";
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

	@Override
	public ArrayList<String> constraintAnalysis() {
		ArrayList<String> values = new ArrayList<>();
		String pattern = null;
		if (constraint == null) {
			values = dataGeneration(pattern,0 + 1, MAX_UNSIGNEDINT - 1);
		} else {
			if(constraint.containsKey(Constraints.pattern.toString())){
				pattern = constraint.get(Constraints.pattern.toString());
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1,
								Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1,
								Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1,
								MAX_UNSIGNEDINT - 1);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								MAX_UNSIGNEDINT - 1);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,0 + 1,
							Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,0 + 1,
							Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,0 + 1, MAX_UNSIGNEDINT - 1);
				}
			}else{
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1,
								Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1,
								Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1,
								MAX_UNSIGNEDINT - 1);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
								MAX_UNSIGNEDINT - 1);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,0 + 1,
							Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,0 + 1,
							Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,0 + 1, MAX_UNSIGNEDINT - 1);
				}
			}
		}
		return values;
	}

}