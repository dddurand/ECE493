package util;

import static org.junit.Assert.*;

import mockObjects.MockServletContext;

import org.junit.Test;

import util.ServletConfiguration.Database;

public class ServletConfigurationTest {

	@Test
	public void testLoadA() {
		ServletConfiguration.reset();
		MockServletContext context = new MockServletContext("home", "true");
		ServletConfiguration.loadContext(context);
		
		assertTrue(ServletConfiguration.getDatabase() == Database.HOME);
		assertTrue(ServletConfiguration.isDebug());
		
	}
	
	@Test
	public void testLoadB() {
		
		ServletConfiguration.reset();
		MockServletContext context = new MockServletContext("uni", "false");
		ServletConfiguration.loadContext(context);
		
		
		
		assertTrue(ServletConfiguration.getDatabase() == Database.UNIVERSITY);
		assertTrue(!ServletConfiguration.isDebug());
		
	}
	
	@Test
	public void testLoadGB() {
		
		ServletConfiguration.reset();
		MockServletContext context = new MockServletContext("asdf", "asdf");
		ServletConfiguration.loadContext(context);
		
		
		
		assertTrue(ServletConfiguration.getDatabase() == Database.HOME);
		assertTrue(!ServletConfiguration.isDebug());
		
	}
	
	

}
