package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class FileData extends Data{

	public FileData(ConcurrentHashMap<String, String> constraint){
		super(constraint);
	}
	public FileData(){
		super();
	}
	@Override
	public ArrayList<String> constraintAnalysis() throws ParseException {
		ArrayList<String> values = new ArrayList<>();
		if (constraint == null) {
			
		}else{
			
		}
		return values;
	}

}
