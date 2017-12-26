package nwpu.autosysteamtest.data;


import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DateData extends Data{

	public DateData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
		// TODO Auto-generated constructor stub
	}
	abstract ArrayList<String> dataGeneration(Date lowtime, Date hightime);
}
