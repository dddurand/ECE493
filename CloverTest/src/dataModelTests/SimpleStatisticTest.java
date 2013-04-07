package dataModelTests;

import android.test.AndroidTestCase;
import dataModels.SimpleStatistic;

public class SimpleStatisticTest extends AndroidTestCase {

	public void testCons()
	{
		String id = "asd";
		String display = "zxvc";
		Double value = 23.5;
		
		
		SimpleStatistic ss = new SimpleStatistic(id, display, value);

		String id2 = ss.getIdentifier();
		String ds = ss.getDisplayName();
		double dv = ss.getValueAsDouble();
		float fv = ss.getValueAsFloat();
		int iv = ss.getValueAsInt();
		
		assertTrue(id.equals(id2));
		assertTrue(ds.equals(display));
		assertTrue(value.doubleValue() == dv);
		assertTrue(value.floatValue() == fv);
		assertTrue(value.intValue() == iv);
		
	}
	
	public void testSetters()
	{
		String id = "asd";
		String display = "zxvc";
		Double value = 23.5;

		String id_ = "asd1";
		String display_ = "zxvc1";
		Double value_ = 223.5;
		
		
		SimpleStatistic ss = new SimpleStatistic(id, display, value);

		ss.setDisplayName(display_);
		ss.setIdentifier(id_);
		ss.setValue(value_);
		
		String id2 = ss.getIdentifier();
		String ds = ss.getDisplayName();
		double dv = ss.getValueAsDouble();
		float fv = ss.getValueAsFloat();
		int iv = ss.getValueAsInt();
		
		assertTrue(id_.equals(id2));
		assertTrue(ds.equals(display_));
		assertTrue(value_.doubleValue() == dv);
		assertTrue(value_.floatValue() == fv);
		assertTrue(value_.intValue() == iv);
		
	}
	
}
