package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;

public class StringData extends Data{
	
	public StringData(){
		
	}

	@Override
	public ArrayList<String> constraintAnalysis() throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		if(constraint == null){
			values.add("afhuijakbfiaudygbfadkufadbvgfshdakgfuyafvasfuaisfgabsfasoiufhauif");
		}else{
			if(constraint.containsKey(Constraints.pattern.toString())){
				
			}else{
				
			}
		}
		return values;
	}

}
