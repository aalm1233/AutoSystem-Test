package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import nwpu.autosysteamtest.tools.PatternAnalysis;
/**
 * 
 * @author Dengtong
 * @version 1.0,05/01/2018
 */
public class TokenData extends BaseData{
	public TokenData(){
		
	}

	public TokenData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}

	@Override
	public ArrayList<String> constraintAnalysis() throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		PatternAnalysis pa = null;
		if(constraint == null){
			pa = new PatternAnalysis("\\w{1,}");
			values = pa.getValues();
		}else{
			if(constraint.containsKey(Constraints.pattern.toString())){
				String pattern = constraint.get(Constraints.pattern.toString());
				pa = new PatternAnalysis(pattern);
				if(constraint.containsKey(Constraints.length.toString())){
					values = pa.getValues(Integer.parseInt(constraint.get(Constraints.length.toString())));
				}else{
					if(constraint.containsKey(Constraints.maxLength.toString())){
						if(constraint.containsKey(Constraints.minLength.toString())){
							values = pa.getValues(Integer.parseInt(constraint.get(Constraints.minLength.toString())),Integer.parseInt(constraint.get(Constraints.maxLength.toString())));
						}else{
							values = pa.getValues(0,Integer.parseInt(constraint.get(Constraints.maxLength.toString())));
						}
					}else if(constraint.containsKey(Constraints.minLength.toString())){
						values = pa.getValues(Integer.parseInt(constraint.get(Constraints.minLength.toString())),999999999);
					}else{
						values = pa.getValues();
					}
				}
			}else{
				if(constraint.containsKey(Constraints.length.toString())){
					pa = new PatternAnalysis("\\w{"+constraint.get(Constraints.length.toString())+"}");
					values = pa.getValues();
				}else{
					if(constraint.containsKey(Constraints.maxLength.toString())){
						if(constraint.containsKey(Constraints.minLength.toString())){
							pa = new PatternAnalysis("\\w{"+constraint.get(Constraints.minLength.toString())+","+constraint.get(Constraints.maxLength.toString())+"}");
							values = pa.getValues();
						}else{
							pa = new PatternAnalysis("\\w{1,"+constraint.get(Constraints.maxLength.toString())+"}");
							values = pa.getValues();
						}
					}else if(constraint.containsKey(Constraints.minLength.toString())){
						pa = new PatternAnalysis("\\w{"+constraint.get(Constraints.minLength.toString())+",}");
						values = pa.getValues();
					}
				}
			}
		}
		return values;
	}
}
