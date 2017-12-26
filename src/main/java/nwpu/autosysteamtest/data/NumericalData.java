package nwpu.autosysteamtest.data;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public abstract class NumericalData extends Data{

	public NumericalData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
		// TODO Auto-generated constructor stub
	}
	abstract ArrayList<String> dataGeneration(long minValue, long maxValue);
}
