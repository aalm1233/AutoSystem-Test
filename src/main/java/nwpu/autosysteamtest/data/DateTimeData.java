package nwpu.autosysteamtest.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 
 * @author Dengtong
 * @version 1.0,05/01/2018
 */
public class DateTimeData extends AbstractDateData{

	public DateTimeData(ConcurrentHashMap<String, String> constraint) {
		super(constraint);
	}

	public DateTimeData() {
		super();
	}

	@Override
	ArrayList<String> dataGeneration(Date lowtime, Date hightime) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		ArrayList<String> t = new ArrayList<>();
		t.add(sdf.format(lowtime));
		calendar.setTime(lowtime);
		calendar.add(Calendar.SECOND, -1);
		t.add(sdf.format(calendar.getTime()));
		t.add(sdf.format(hightime));
		calendar.setTime(hightime);
		calendar.add(Calendar.SECOND, +1);
		t.add(sdf.format(calendar.getTime()));
		calendar.clear();
		return t;
	}

	@Override
	public ArrayList<String> constraintAnalysis() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");
		ArrayList<String> values = new ArrayList<>();
		if(constraint == null){
			values = dataGeneration(sdf.parse("0001-01-01T00:00:02"),sdf.parse("9999-12-31T23:59:58"));
		}else{
			Calendar calendar = Calendar.getInstance();
			if (constraint.containsKey(Constraints.minExclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
					calendar.add(Calendar.SECOND, -1);
					Date minDate = calendar.getTime();
					calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
					calendar.add(Calendar.SECOND, +1);
					Date maxDate = calendar.getTime();
					values = dataGeneration(minDate,maxDate);
				} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
					calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
					calendar.add(Calendar.SECOND, -1);
					Date minDate = calendar.getTime();
					Date maxDate = sdf.parse(constraint.get(Constraints.minInclusive.toString()));
					values = dataGeneration(minDate,maxDate);
				} else {
					calendar.setTime(sdf.parse(constraint.get(Constraints.minExclusive.toString())));
					calendar.add(Calendar.SECOND, -1);
					Date minDate = calendar.getTime();
					values = dataGeneration(minDate,sdf.parse("9999-12-31T23:59:58"));
				}
			} else if (constraint.containsKey(Constraints.minInclusive.toString())) {
				if (constraint.containsKey(Constraints.maxExclusive.toString())) {
					Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
					calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
					calendar.add(Calendar.SECOND, +1);
					Date maxDate = calendar.getTime();
					values = dataGeneration(minDate,maxDate);
				} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
					Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
					Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
					values = dataGeneration(minDate,maxDate);
				} else {
					Date minDate = sdf.parse(constraint.get(Constraints.minExclusive.toString()));
					values = dataGeneration(minDate,sdf.parse("9999-12-31T23:59:58"));
				}
			} else if (constraint.containsKey(Constraints.maxExclusive.toString())) {
				calendar.setTime(sdf.parse(constraint.get(Constraints.maxExclusive.toString())));
				calendar.add(Calendar.SECOND, +1);
				Date maxDate = calendar.getTime();
				values = dataGeneration(sdf.parse("0001-01-01T00:00:02"),maxDate);
			} else if (constraint.containsKey(Constraints.maxInclusive.toString())) {
				Date maxDate = sdf.parse(constraint.get(Constraints.maxExclusive.toString()));
				values = dataGeneration(sdf.parse("0001-01-01T00:00:02"),maxDate);
			} else {
				values = dataGeneration(sdf.parse("0001-01-01T00:00:02"),sdf.parse("9999-12-31T23:59:58"));
			}
		}
		return values;
	}

}
