package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Dengtong
 * @version 1.0,07/01/2018
 */
public class NegativeIntData extends IntData {

	public NegativeIntData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}

	public NegativeIntData() {
		super();
	}

	@Override
	public ArrayList<String> constraintAnalysis() {
		ArrayList<String> values = new ArrayList<>();
		String pattern = null;
		if (constraint == null) {
			values = dataGeneration(pattern,Integer.MIN_VALUE + 1, -1);
		} else {
			if (constraint.containsKey(Constraints.pattern.toString())) {
				pattern = constraint.get(Constraints.pattern.toString());
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								-1);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
								Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
								Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minInclusive.toString())), -1);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,Integer.MIN_VALUE + 1,
							Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,Integer.MIN_VALUE + 1,
							Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,Integer.MIN_VALUE + 1, -1);
				}
			}else{
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minExclusive.toString())) + 1,
								-1);
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
								Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minInclusive.toString())),
								Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,Integer.parseInt(constraint.get(Constraints.minInclusive.toString())), -1);
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,Integer.MIN_VALUE + 1,
							Integer.parseInt(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,Integer.MIN_VALUE + 1,
							Integer.parseInt(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,Integer.MIN_VALUE + 1, -1);
				}
			}
			
		}
		return values;
	}
}
