package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class IntData extends NumericalData{

	public IntData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
		// TODO Auto-generated constructor stub
	}
	public IntData() {
		super();
	}
	@Override
	public ArrayList<String> constraintAnalysis() {
		ArrayList<String> values = new ArrayList<>();
		if(constraint == null){
			values = dataGeneration(Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
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
				values = dataGeneration(Integer.MIN_VALUE + 1, Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = dataGeneration(Integer.MIN_VALUE + 1, Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = dataGeneration(Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
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
