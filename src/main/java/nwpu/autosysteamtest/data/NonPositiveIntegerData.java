package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class NonPositiveIntegerData extends IntData {
	
	public NonPositiveIntegerData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
		// TODO Auto-generated constructor stub
	}
	@Override
	public ArrayList<String> constraintAnalysis() {
		ArrayList<String> values = new ArrayList<>();
		if (constraint.containsKey(Constraints.minExclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1, 0);
			}
		} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
			if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())), 0);
			}
		} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = dataGeneration(Integer.MIN_VALUE + 1, 
						Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
		} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = dataGeneration(Integer.MIN_VALUE + 1, 
						Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
		} else {
			values = dataGeneration(Integer.MIN_VALUE + 1, 0);
		}
		return values;
	}

}
