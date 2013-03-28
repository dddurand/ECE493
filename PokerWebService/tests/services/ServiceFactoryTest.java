package services;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import mockObjects.MockDbInterface;

import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseInterface.DatabaseInterfaceException;

import servlets.CommunityStatisticsServlet;
import servlets.ConfigHttpServlet;
import servlets.LoginServlet;
import servlets.LogoutServlet;
import servlets.PersonalStatisticsServlet;
import servlets.RankingStatisticsServlet;
import servlets.RegisterServlet;
import servlets.TestServlet;
import servlets.UploadServlet;

public class ServiceFactoryTest {

	private static ServiceFactory factory;
	private static MockDbInterface dbInterface;
	
	@BeforeClass
	public static void setup()
	{
		factory = new ServiceFactory();
		try {
			dbInterface = new MockDbInterface(false);
		} catch (DatabaseInterfaceException e) {
			fail();
		}
	}
	
	@Test
	public void test() {
		
		ConfigHttpServlet servlet = new LoginServlet();
		checkDelegate(LoginDelegate.class ,servlet);
		
		servlet = new LogoutServlet();
		checkDelegate(LogoutDelegate.class ,servlet);
		
		servlet = new RegisterServlet();
		checkDelegate(RegisterDelegate.class ,servlet);
		
		servlet = new UploadServlet();
		checkDelegate(UploadDelegate.class ,servlet);
		
		servlet = new PersonalStatisticsServlet();
		checkDelegate(PersonalStatisticsDelegate.class ,servlet);
		
		servlet = new RankingStatisticsServlet();
		checkDelegate(RankingStatisticsDelegate.class ,servlet);
		
		servlet = new CommunityStatisticsServlet();
		checkDelegate(CommunityStatisticsDelegate.class ,servlet);
		
		servlet = new TestServlet();
		checkDelegate(TestDelegate.class ,servlet);
		
		servlet = new ConfigHttpServlet();
		checkDelegate(ServiceDelegate.class ,servlet);
				
	}
	
	public void checkDelegate(Class<? extends ServiceDelegate> clasz, ConfigHttpServlet servlet)
	{
		try {
			SecureService service = factory.getService(servlet, dbInterface);
			Field field = service.getClass().getDeclaredField("service");
			field.setAccessible(true);
			
			
			try {
				Object obj = field.get(service);
				
				if(!(obj.getClass() == clasz)) fail();
				
			} catch (IllegalArgumentException e) {
				fail();
			} catch (IllegalAccessException e) {
				fail();
			}
			
		} catch (DatabaseInterfaceException e) {
			fail();
		} catch (SecurityException e1) {
			fail();
		} catch (NoSuchFieldException e1) {
			fail();
		}
	}

}
