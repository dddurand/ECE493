package suite;

import misc.CronTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import services.CommunityStatisticsDelegateTest;
import services.LoginDelegateTest;
import services.LogoutDelegateTest;
import services.PersonalStatisticsDelegateTest;
import services.RankingStatisticsDelegateTest;
import services.RegisterDelegateTest;
import services.SecureServiceTest;
import services.ServiceDelegateTest;
import services.ServiceFactoryTest;
import services.UploadDelegateTest;
import system.SystemTest;
import util.CodesTest;
import util.PasswordUtilTest;
import util.ServletConfigurationTest;
import dataModels.AccountTest;
import dataModels.CardTest;
import dataModels.CommunityStatisticsTest;
import dataModels.FilterTest;
import dataModels.GameActionTest;
import dataModels.GameTest;
import dataModels.MiscGameDataTest;
import dataModels.OptimalityTest;
import dataModels.PersonalStatisticsTest;
import dataModels.RankingStatisticsTest;
import dataModels.UploadDataTest;
import database.ResponseObjectTest;

@RunWith(Suite.class)
@SuiteClasses({

	AccountTest.class,
	CardTest.class,
	FilterTest.class,
	GameActionTest.class,
	GameTest.class,
	MiscGameDataTest.class,
	OptimalityTest.class,
	RankingStatisticsTest.class,
	UploadDataTest.class,
	CommunityStatisticsTest.class,
	PersonalStatisticsTest.class,
	ResponseObjectTest.class,
	CodesTest.class,
	PasswordUtilTest.class,
	ServletConfigurationTest.class,
	CommunityStatisticsDelegateTest.class,
	LoginDelegateTest.class,
	LogoutDelegateTest.class,
	PersonalStatisticsDelegateTest.class,
	RankingStatisticsDelegateTest.class,
	RegisterDelegateTest.class,
	UploadDelegateTest.class,
	SecureServiceTest.class,
	ServiceDelegateTest.class,
	ServiceFactoryTest.class,
	
})

public class AllTests {

}
