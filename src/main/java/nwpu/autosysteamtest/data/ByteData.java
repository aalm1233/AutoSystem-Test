package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.tools.PatternAnalysis;

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
		int minlenth =minValue < 0 ?1 : String.valueOf(Math.abs(minValue)).length();
		int maxlenth =String.valueOf(Math.abs(minValue)).length() > String.valueOf(Math.abs(maxValue)).length() ?String.valueOf(Math.abs(minValue)).length() : String.valueOf(Math.abs(maxValue)).length();
		ArrayList<String> t = new ArrayList<>();
		PatternAnalysis pa = new PatternAnalysis("-{0,1}[0-9]{"+minlenth+","+maxlenth+"}");
		ArrayList<String> t1  = pa.getValues();
		for(int i = 0;i<t1.size();i++){
			try{
				Byte temp = Byte.parseByte(t1.get(i));
				if(temp < minValue||temp >maxValue){
					continue;
				}
				t.add(String.valueOf(temp));
			}catch (NumberFormatException e) {
			}
		}
		t.add(String.valueOf(minValue));
		t.add(String.valueOf(minValue - 1));
		t.add(String.valueOf(maxValue));
		t.add(String.valueOf(maxValue + 1));
		t.add(String.valueOf((minValue + maxValue) / 2));
		return t;
	}

}
