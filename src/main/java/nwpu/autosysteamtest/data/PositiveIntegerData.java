package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class PositiveIntegerData extends IntData {

	public PositiveIntegerData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}

	public PositiveIntegerData() {
		super();
	}

	@Override
	public ArrayList<String> constraintAnalysis() {
		ArrayList<String> values = new ArrayList<>();
		if(constraint == null){
			values = dataGeneration(1, Integer.MAX_VALUE - 1);
		}else{
			if (constraint.containsKey(Constraints.minExclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
							Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
							Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1, Integer.MAX_VALUE - 1);
				}
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
							Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
							Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(Integer.parseInt(constraint.get(Constraints.minInclusive.toString())), Integer.MAX_VALUE - 1);
				}
			} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(1, Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(1, Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = dataGeneration(1, Integer.MAX_VALUE - 1);
			}
		}
		return values;
	}


}
