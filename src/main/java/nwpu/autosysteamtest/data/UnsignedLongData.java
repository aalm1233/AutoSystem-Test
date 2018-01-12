package nwpu.autosysteamtest.data;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;

import nwpu.autosysteamtest.tools.PatternAnalysisNum;
/**
 * 
 * @author Dengtong
 * @version 1.0,05/01/2018
 */
public class UnsignedLongData extends BaseData{
	
	public static final BigInteger MAX_UNSIGNEDLONG = (new BigInteger(String.valueOf(Long.MAX_VALUE)).add(new BigInteger(String.valueOf(Long.MAX_VALUE)))).add(new BigInteger("1"));

	ArrayList<String> dataGeneration(String pattern, BigInteger minValue, BigInteger maxValue) {
		if (pattern == null) {
			pattern = "[0-9]{1,}";
		}
		ArrayList<String> t = new ArrayList<>();
		PatternAnalysisNum pa = new PatternAnalysisNum(pattern);
		t.addAll(pa.getValues(minValue, maxValue));
		t.add(minValue.toString());
		t.add((minValue.subtract(new BigInteger("1"))).toString());
		t.add(maxValue.toString());
		t.add((maxValue.add(new BigInteger("1"))).toString());
		t.add((((maxValue.add(minValue))).divide(new BigInteger("2"))).toString());
		return t;
	}


	@Override
	public ArrayList<String> constraintAnalysis() throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		String pattern = null;
		if (constraint == null) {
			values = dataGeneration(pattern,new BigInteger("1") , MAX_UNSIGNEDLONG.subtract(new BigInteger("1")));
		} else {
			if(constraint.containsKey(Constraints.pattern.toString())){
				pattern = constraint.get(Constraints.pattern.toString());
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minExclusive.toString())).add(new BigInteger("1")),
								new BigInteger(constraint.get(Constraints.maxExclusive.toString())).subtract(new BigInteger("1")));
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minExclusive.toString())).add(new BigInteger("1")),
								new BigInteger(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minExclusive.toString())).add(new BigInteger("1")),
								MAX_UNSIGNEDLONG.subtract(new BigInteger("1")));
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minInclusive.toString())),
								new BigInteger(constraint.get(Constraints.maxExclusive.toString())).subtract(new BigInteger("1")));
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minInclusive.toString())),
								new BigInteger(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minInclusive.toString())),
								MAX_UNSIGNEDLONG.subtract(new BigInteger("1")));
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,new BigInteger("1") ,
							new BigInteger(constraint.get(Constraints.maxExclusive.toString())).subtract(new BigInteger("1")));
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,new BigInteger("1") ,
							new BigInteger(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,new BigInteger("1") , MAX_UNSIGNEDLONG.subtract(new BigInteger("1")));
				}
			}else{
				if (constraint.containsKey(Constraints.minExclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minExclusive.toString())).add(new BigInteger("1")),
								new BigInteger(constraint.get(Constraints.maxExclusive.toString())).subtract(new BigInteger("1")));
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minExclusive.toString())).add(new BigInteger("1")),
								new BigInteger(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minExclusive.toString())).add(new BigInteger("1")),
								MAX_UNSIGNEDLONG.subtract(new BigInteger("1")));
					}
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					if (constraint.containsKey(Constraints.maxExclusive.toString())) {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minInclusive.toString())),
								new BigInteger(constraint.get(Constraints.maxExclusive.toString())).subtract(new BigInteger("1")));
					} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minInclusive.toString())),
								new BigInteger(constraint.get(Constraints.maxInclusive.toString())));
					} else {
						values = dataGeneration(pattern,new BigInteger(constraint.get(Constraints.minInclusive.toString())),
								MAX_UNSIGNEDLONG.subtract(new BigInteger("1")));
					}
				} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					values = dataGeneration(pattern,new BigInteger("1") ,
							new BigInteger(constraint.get(Constraints.maxExclusive.toString())).subtract(new BigInteger("1")));
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					values = dataGeneration(pattern,new BigInteger("1") ,
							new BigInteger(constraint.get(Constraints.maxInclusive.toString())));
				} else {
					values = dataGeneration(pattern,new BigInteger("1") , MAX_UNSIGNEDLONG.subtract(new BigInteger("1")));
				}
			}
		}
		return values;
	}

}
