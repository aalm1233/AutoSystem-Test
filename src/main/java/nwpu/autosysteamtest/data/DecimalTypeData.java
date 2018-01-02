package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;

import nwpu.autosysteamtest.tools.PatternAnalysis;

public class DecimalTypeData extends DecimalData{

	@Override
	ArrayList<String> dataGeneration(double minValue, double maxValue) {
		int minlenth =minValue < 0 ?1 : String.valueOf(Math.abs(minValue)).split("\\.")[0].length();
		int maxlenth =String.valueOf(Math.abs(minValue)).split("\\.")[0].length() > String.valueOf(Math.abs(maxValue)).split("\\.")[0].length() ?String.valueOf(Math.abs(minValue)).split("\\.")[0].length() : String.valueOf(Math.abs(maxValue)).split("\\.")[0].length();
		ArrayList<String> t = new ArrayList<>();
		PatternAnalysis pa = new PatternAnalysis("-{0,1}[0-9]{"+minlenth+","+maxlenth+"}\\.[0-9]{8}");
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

	@Override
	public ArrayList<String> constraintAnalysis() throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		if (constraint == null) {
			values = dataGeneration(Float.MIN_VALUE, Float.MAX_VALUE - 1);
		} else {
			if (constraint.containsKey(Constraints.minExclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(Float.parseFloat(constraint.get(Constraints.minExclusive.toString())) + 1,
							Float.parseFloat(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(Float.parseFloat(constraint.get(Constraints.minExclusive.toString())) + 1,
							Float.parseFloat(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(Float.parseFloat(constraint.get(Constraints.minExclusive.toString())) + 1,
							Float.MAX_VALUE - 1);
				}
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(Float.parseFloat(constraint.get(Constraints.minInclusive.toString())),
							Float.parseFloat(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(Float.parseFloat(constraint.get(Constraints.minInclusive.toString())),
							Float.parseFloat(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(Float.parseFloat(constraint.get(Constraints.minInclusive.toString())),
							Float.MAX_VALUE - 1);
				}
			} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = dataGeneration(Float.MIN_VALUE + 1,
						Float.parseFloat(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = dataGeneration(Float.MIN_VALUE + 1,
						Float.parseFloat(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = dataGeneration(Float.MIN_VALUE + 1, Float.MAX_VALUE - 1);
			}
		}
		return values;
	}

}
