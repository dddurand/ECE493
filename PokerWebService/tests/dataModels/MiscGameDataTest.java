package dataModels;

import static org.junit.Assert.*;

import org.junit.Test;

public class MiscGameDataTest {

	@Test
	public void testMiscGameData() {
		@SuppressWarnings("unused")
		MiscGameData miscGameData = new MiscGameData();
		assertTrue(true);
	}

	@Test
	public void testMiscGameDataFull() {
		
		String name = "NAME";
		String value = "VALUE";

		MiscGameData miscGameData = new MiscGameData(name, value);
		assertTrue(miscGameData.getName().equals(name));
		assertTrue(miscGameData.getValue().equals(value));
		
	}
	
	@Test
	public void testSetName() {
		
		String name = "NAME";
		String value = "VALUE";

		MiscGameData miscGameData = new MiscGameData(name, value);
		miscGameData.setName("2");
		
		assertTrue(miscGameData.getName().equals("2"));
		assertTrue(miscGameData.getValue().equals(value));
		
	}
	
	@Test
	public void testSetValue() {
		
		String name = "NAME";
		String value = "VALUE";
		String value2 = "VALUE2";
		
		MiscGameData miscGameData = new MiscGameData(name, value);
		miscGameData.setValue(value2);
		
		assertTrue(miscGameData.getName().equals(name));
		assertTrue(miscGameData.getValue().equals(value2));
		
	}
	
}
