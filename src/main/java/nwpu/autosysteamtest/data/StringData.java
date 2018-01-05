package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.tools.PatternAnalysis;

public class StringData extends Data{
	
	public StringData(){
		
	}

	public StringData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}

	@Override
	public ArrayList<String> constraintAnalysis() throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		PatternAnalysis pa = null;
		if(constraint == null){
			pa = new PatternAnalysis("\\w{1,60}");
			values = pa.getValues();
		}else{
			if(constraint.containsKey(Constraints.pattern.toString())){
				String pattern = constraint.get(Constraints.pattern.toString());
				pa = new PatternAnalysis(pattern);
				values = pa.getValues();
			}else{
				if(constraint.containsKey(Constraints.length.toString())){
					pa = new PatternAnalysis("\\w{"+constraint.get(Constraints.length.toString())+"}");
				}else{
					if(constraint.containsKey(Constraints.maxLength.toString())){
						if(constraint.containsKey(Constraints.minLength.toString())){
							
						}else{
							
						}
					}else if(constraint.containsKey(Constraints.minLength.toString())){
						
					}
				}
			}
		}
		return values;
	}

}
