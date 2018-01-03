package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.tools.PatternAnalysis;

public class DoubleData extends DecimalData{
	public DoubleData(){
		super();
	}
	public DoubleData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}
	@Override
	ArrayList<String> dataGeneration(double minValue, double maxValue) {
		int mint = (int)minValue;
		int maxt = (int)maxValue;
		int minlenth =minValue < 0 ?1 : String.valueOf(Math.abs(mint)).split("\\.")[0].length();
		int maxlenth =String.valueOf(Math.abs(mint)).split("\\.")[0].length() > String.valueOf(Math.abs(maxt)).split("\\.")[0].length() ?String.valueOf(Math.abs(mint)).split("\\.")[0].length() : String.valueOf(Math.abs(maxt)).split("\\.")[0].length();
		ArrayList<String> t = new ArrayList<>();
		PatternAnalysis pa = new PatternAnalysis("-{0,1}[0-9]{"+minlenth+","+maxlenth+"}\\.[0-9]{4,12}");
		ArrayList<String> t1  = pa.getValues();
		for(int i = 0;i<t1.size();i++){
			try{
				Double temp = Double.parseDouble(t1.get(i));
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
			values = dataGeneration(Double.MIN_VALUE, Double.MAX_VALUE - 1);
		} else {
			if (constraint.containsKey(Constraints.minExclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(Double.parseDouble(constraint.get(Constraints.minExclusive.toString())) + 1,
							Double.parseDouble(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(Double.parseDouble(constraint.get(Constraints.minExclusive.toString())) + 1,
							Double.parseDouble(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(Double.parseDouble(constraint.get(Constraints.minExclusive.toString())) + 1,
							Double.MAX_VALUE - 1);
				}
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(Double.parseDouble(constraint.get(Constraints.minInclusive.toString())),
							Double.parseDouble(constraint.get(Constraints.maxExclusive.toString())) - 1);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(Double.parseDouble(constraint.get(Constraints.minInclusive.toString())),
							Double.parseDouble(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(Double.parseDouble(constraint.get(Constraints.minInclusive.toString())),
							Double.MAX_VALUE - 1);
				}
			} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				values = dataGeneration(Double.MIN_VALUE + 1,
						Double.parseDouble(constraint.get(Constraints.maxExclusive.toString())) - 1);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				values = dataGeneration(Double.MIN_VALUE + 1,
						Double.parseDouble(constraint.get(Constraints.maxInclusive.toString())));
			} else {
				values = dataGeneration(Double.MIN_VALUE + 1, Double.MAX_VALUE - 1);
			}
		}
		return values;
	}
}
