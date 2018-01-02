package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;

import nwpu.autosysteamtest.tools.PatternAnalysis;

public class StringData extends Data{
	
	public StringData(){
		
	}

	@Override
	public ArrayList<String> constraintAnalysis() throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		PatternAnalysis pa = null;
		if(constraint == null){
			pa = new PatternAnalysis("\\d{1,60}");
			values = pa.getValues();
		}else{
			if(constraint.containsKey(Constraints.pattern.toString())){
				String pattern = constraint.get(Constraints.pattern.toString());
				pa = new PatternAnalysis(pattern);
				values = pa.getValues();
			}else{
				
			}
		}
		return values;
	}

}
