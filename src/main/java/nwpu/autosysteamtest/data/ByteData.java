package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ByteData extends NumericalData {
	public ByteData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}

	public ByteData() {
		super();
	}

	@Override
	public ArrayList<String> constraintAnalysis() {
		ArrayList<String> values = new ArrayList<>();
		if (constraint == null) {
			values = dataGeneration(Byte.MIN_VALUE + 1, Byte.MAX_VALUE - 1);
		} else {
			if (constraint.containsKey(Constraints.minExclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(Byte.parseByte(constraint.get(Constraints.minExclusive.toString())) + 1,
							Byte.parseByte(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(Byte.parseByte(constraint.get(Constraints.minExclusive.toString())) + 1,
							Byte.parseByte(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(Byte.parseByte(constraint.get(Constraints.minExclusive.toString())) + 1,
							Byte.MAX_VALUE - 1);
				}
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(Byte.parseByte(constraint.get(Constraints.minInclusive.toString())),
							Byte.parseByte(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(Byte.parseByte(constraint.get(Constraints.minInclusive.toString())),
							Byte.parseByte(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(Byte.parseByte(constraint.get(Constraints.minInclusive.toString())),
							Byte.MAX_VALUE - 1);
				}
			} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = dataGeneration(Byte.MIN_VALUE + 1,
						Byte.parseByte(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = dataGeneration(Byte.MIN_VALUE + 1,
						Byte.parseByte(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = dataGeneration(Byte.MIN_VALUE + 1, Byte.MAX_VALUE - 1);
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
		t.add(String.valueOf((minValue + maxValue) / 2));
		return t;
	}

}