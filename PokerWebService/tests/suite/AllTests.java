package suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import util.CodesTest;
import util.PasswordUtil;
import util.PasswordUtilTest;

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
	PasswordUtilTest.class
})

public class AllTests {

}
