package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

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
		if (constraint == null) {
			values = dataGeneration(Long.MIN_VALUE + 1L, Long.MAX_VALUE - 1L);
		} else {
			if (constraint.containsKey(Constraints.minExclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
							Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
							Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(Long.parseLong(constraint.get(Constraints.minExclusive.toString())) + 1L,
							Long.MAX_VALUE - 1L);
				}
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
							Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
							Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(Long.parseLong(constraint.get(Constraints.minInclusive.toString())),
							Long.MAX_VALUE - 1L);
				}
			} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = dataGeneration(Long.MIN_VALUE + 1L,
						Long.parseLong(constraint.get(Constraints.maxExclusive.toString())) - 1L);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = dataGeneration(Long.MIN_VALUE + 1L,
						Long.parseLong(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = dataGeneration(Long.MIN_VALUE + 1L, Long.MAX_VALUE - 1L);
			}
		}
		return values;
	}

	@Override
	ArrayList<String> dataGeneration(long minValue, long maxValue) {
		ArrayList<String> t = new ArrayList<>();
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue - 1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue + 1));
		t.add(String.valueOf((maxValue + minValue) / 2));
		return t;
	}

}
