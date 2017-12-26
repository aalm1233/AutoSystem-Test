package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ShortData extends NumericalData {

	public ShortData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<String> constraintAnalysis() {
		ArrayList<String> values = new ArrayList<>();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = dataGeneration(Short.parseShort(constraint.get(Constraints.minExclusive.toString())) + 1,
						Short.parseShort(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = dataGeneration(Short.parseShort(constraint.get(Constraints.minExclusive.toString())) + 1,
						Short.parseShort(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = dataGeneration(Short.parseShort(constraint.get(Constraints.minExclusive.toString())) + 1, Short.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = dataGeneration(Short.parseShort(constraint.get(Constraints.minInclusive.toString())),
						Short.parseShort(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = dataGeneration(Short.parseShort(constraint.get(Constraints.minInclusive.toString())),
						Short.parseShort(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = dataGeneration(Short.parseShort(constraint.get(Constraints.minInclusive.toString())), Short.MAX_VALUE - 1);
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
			values = dataGeneration(Short.MIN_VALUE + 1, Short.parseShort(constraint.get(Constraints.maxExclusive.toString())) - 1);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
			values = dataGeneration(Short.MIN_VALUE + 1, Short.parseShort(constraint.get(Constraints.maxInclusive.toString())));
		} else {
			values = dataGeneration(Short.MIN_VALUE + 1, Short.MAX_VALUE - 1);
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
		t.add(String.valueOf((minValue + maxValue) / 2));
		return t;
	}

}
