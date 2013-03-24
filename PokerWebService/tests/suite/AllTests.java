package suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import dataModels.AccountTest;
import dataModels.CardTest;
import dataModels.Filter;
import dataModels.FilterTest;
import dataModels.GameAction;
import dataModels.GameActionTest;
import dataModels.GameTest;
import dataModels.MiscGameDataTest;
import dataModels.OptimalityTest;
import dataModels.RankingStatisticsTest;
import dataModels.UploadData;
import dataModels.UploadDataTest;

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
	UploadDataTest.class
})

public class AllTests {

}
