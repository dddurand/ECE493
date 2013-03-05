package dataModels;

import dataModels.GameAction.PokerAction;
import dataModels.RankingStatistics.RankedDataRow;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.DatabaseInterface.NumerableActionColumn;
import database.DatabaseInterface.NumerableActionOperation;

/**
 * A data model that holds and facilitates the generation of the personal statistics.
 * 
 * @author dddurand
 *
 */

public class PersonalStatistics {

	private transient DatabaseInterface dbInterface;
	private transient Account account;
	private transient Filter filter;
	
	/**
	 * Used in JSON Output
	 */
	@SuppressWarnings("unused")
	private int totalDollarsBetOnCalls;
	@SuppressWarnings("unused")
	private int totalDollarsBetOnBets;
	@SuppressWarnings("unused")
	private int totalDollarsBetOnRaises;
	@SuppressWarnings("unused")
	private int totalDollarsBetOnReRaises;
	@SuppressWarnings("unused")
	private int totalDollarsBet;
	@SuppressWarnings("unused")
	private int avgDollarsBetOnCalls;
	@SuppressWarnings("unused")
	private int avgDollarsBetOnBets;
	@SuppressWarnings("unused")
	private int avgDollarsBetOnRaises;
	@SuppressWarnings("unused")
	private int avgDollarsBetOnReRaises;
	@SuppressWarnings("unused")
	private int totalNumberOfBets;
	@SuppressWarnings("unused")
	private int totalNumberOfChecks;
	@SuppressWarnings("unused")
	private int totalNumberOfCalls;
	@SuppressWarnings("unused")
	private int totalNumberOfFolds;
	@SuppressWarnings("unused")
	private int totalNumberOfRaise;
	@SuppressWarnings("unused")
	private int totalNumberOfReRaise;
	@SuppressWarnings("unused")
	private int totalNumberOfPotsWon;
	@SuppressWarnings("unused")
	private int totalNumberOfPotsLoss;
	@SuppressWarnings("unused")
	private int totalNumberOfPots;
	@SuppressWarnings("unused")
	private int avgPotOnChecks;
	@SuppressWarnings("unused")
	private int avgPotOnCalls;
	@SuppressWarnings("unused")
	private int avgPotOnBets;
	@SuppressWarnings("unused")
	private int avgPotOnRaises;
	@SuppressWarnings("unused")
	private int avgPotOnReRaises;
	@SuppressWarnings("unused")
	private int avgPotOnFolds;
	@SuppressWarnings("unused")
	private int avgPotOnWins;
	@SuppressWarnings("unused")
	private int avgPotOnLoses;
	@SuppressWarnings("unused")
	private int totalDollarsWon;
	@SuppressWarnings("unused")
	private int totalDollarsLoss;
	@SuppressWarnings("unused")
	private int totalDollarsFolded;
	@SuppressWarnings("unused")
	private double winPercentage;
	@SuppressWarnings("unused")
	private int moneyGenerated;
	@SuppressWarnings("unused")
	private int gamesPlayed;
	@SuppressWarnings("unused")
	private int netMoney;
	@SuppressWarnings("unused")
	private int avgBet;
	@SuppressWarnings("unused")
	private int netMoneyRanking;
	
	/**
	 * General Constructor
	 * @param account
	 * @param dbInterface
	 */
	public PersonalStatistics(Account account, DatabaseInterface dbInterface, Filter filter)
	{
		this.dbInterface = dbInterface;
		this.account = account;
		this.filter = filter;
	}
	
	/**
	 * This method triggers the object to generate all statistics for the provided account.
	 * 
	 * 
	 * @throws DatabaseInterfaceException
	 */
	public void generateAllStatistics() throws DatabaseInterfaceException
	{
		totalDollarsBetOnCalls = this.getTotalDollarsBetOnCalls();
		totalDollarsBetOnBets = getTotalDollarsBetOnBets();
		totalDollarsBetOnRaises = getTotalDollarsBetOnRaises();
		totalDollarsBetOnReRaises = getTotalDollarsBetOnReRaises();
		totalDollarsBet = getTotalDollarsBet();
		avgDollarsBetOnCalls = getAvgDollarsBetOnCalls();
		avgDollarsBetOnBets = getAvgDollarsBetOnBets();
		avgDollarsBetOnRaises = getAvgDollarsBetOnRaises();
		avgDollarsBetOnReRaises = getAvgDollarsBetOnReRaises();
		totalNumberOfBets = getTotalNumberOfBets();
		totalNumberOfChecks = getTotalNumberOfChecks();
		totalNumberOfCalls = getTotalNumberOfCalls();
		totalNumberOfFolds = getTotalNumberOfFolds();
		totalNumberOfRaise = getTotalNumberOfRaise();
		totalNumberOfReRaise = getTotalNumberOfReRaise();
		totalNumberOfPotsWon = getTotalNumberOfPotsWon();
		totalNumberOfPotsLoss = getTotalNumberOfPotsLoss();
		avgPotOnChecks = getAvgDollarsPotOnChecks();
		avgPotOnCalls = getAvgDollarsPotOnCalls();
		avgPotOnBets = getAvgDollarsPotOnBets();
		avgPotOnRaises = getAvgDollarsPotOnRaises();
		avgPotOnReRaises = getAvgDollarsPotOnReRaises();
		avgPotOnFolds = getAvgDollarsPotOnFolds();
		avgPotOnWins = getAvgDollarsPotOnWins();
		avgPotOnLoses = getAvgDollarsPotOnLoses();
		totalDollarsWon = getTotalDollarsWon();
		totalDollarsLoss = getTotalDollarsLoss();
		totalDollarsFolded = getTotalDollarsFolded();
		winPercentage = getWinPercentage();
		moneyGenerated = getMoneyGenerate();
		gamesPlayed = getGamesPlayed();
		netMoney = getNetMoney();
		netMoneyRanking = getNetMoneyRank();
		avgBet = getAvgBet();
		totalNumberOfPots = getTotalNumberOfPots();
	}
	
	public RankedDataRow getMyRating() throws DatabaseInterfaceException
	{
		RankedDataRow dataRow;
		
		if(filter.getRankType() == Filter.RankType.NET_MONEY)
		{
			int netMoney = this.getNetMoney();
			int netMoneyRank = this.getNetMoneyRank();
			dataRow = new RankedDataRow(netMoneyRank, account.getUsername(), netMoney);
		}
		else
		{
			double optimality = this.getOptimality();
			int optimalityRating = this.getOptimalityRanking();
			dataRow = new RankedDataRow(optimalityRating, account.getUsername(), optimality);
		}
		
		return dataRow;	
	}
	
	public double getOptimality()
	{
		return 0;
	}
	
	public int getOptimalityRanking()
	{
		return 1;
	}
	
	/**
	 * Retrieves the ranking of the user based on the delta money
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getNetMoneyRank() throws DatabaseInterfaceException
	{
		return dbInterface.getUserNetMoneyRanking(account, filter);
	}
	
	/**
	 * Gets the total amount of money bet during calls
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsBetOnCalls() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.BET, 
													PokerAction.CALL, false, filter);
	}
	
	/**
	 * Gets the total amount of money bet during initial bet
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsBetOnBets() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.BET, 
													PokerAction.BET, false, filter);
	}
	
	/**
	 * Gets the total amount of money bet during raises
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsBetOnRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.BET, 
													PokerAction.RAISE, false, filter);
	}
	
	/**
	 * Gets the total amount of money bet through reraises
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsBetOnReRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.BET, 
													PokerAction.RERAISE, false, filter);
	}
	
	/**
	 * Gets the total amount of money bet
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsBet() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
				NumerableActionOperation.SUM, 
				NumerableActionColumn.BET, 
				null, false, filter);
	}
	
	/**
	 * Gets the total amount of money bet through calls
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsBetOnCalls() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.BET, 
													PokerAction.CALL, true, filter);
	}
	
	/**
	 * Gets the avg amount of money bet on each bet
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsBetOnBets() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.BET, 
													PokerAction.BET, true, filter);
	}
	
	/**
	 * Gets the avg amount of money bet on each raise
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsBetOnRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.BET, 
													PokerAction.RAISE, true, filter);
	}
	
	/**
	 * Gets the avg amount of money bet on each reraise
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsBetOnReRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.BET, 
													PokerAction.RERAISE, true, filter);
	}
	
	/**
	 * Gets the avg amount of money per bet
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgBet() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.BET, 
													null, true, filter);
	}
	
	/**
	 * Gets the number of bets made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfBets() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.BET, account, filter);
	}
	
	/**
	 * Gets the number of checks made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfChecks() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.CHECK, account, filter);
	}
	
	/**
	 * Gets the number of calls made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfCalls() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.CALL, account, filter);
	}
	
	/**
	 * Gets the number of folds made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfFolds() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.FOLD, account, filter);
	}
	
	/**
	 * Gets the number of raises made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfRaise() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.RAISE, account, filter);
	}
	
	/**
	 * Gets the number of reraises made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfReRaise() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.RERAISE, account, filter);
	}
	
	/**
	 * Gets the number of pots won
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfPotsWon() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.WIN, account, filter);
	}
	
	/**
	 * Gets the number of pots lost
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfPotsLoss() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.LOSS, account, filter);
	}
	
	/**
	 * Gets the avg pot size on check
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnChecks() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.CHECK, true, filter);
	}
	
	/**
	 * Gets the avg pot size on calls
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnCalls() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.CALL, true, filter);
	}
	
	/**
	 * Gets the avg pot size on bets
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnBets() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.BET, true, filter);
	}
	
	/**
	 * Gets the avg pot size on raises
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.RAISE, true, filter);
	}
	
	/**
	 * Gets the avg pot size on reraises
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnReRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.RERAISE, true, filter);
	}
	
	/**
	 * Gets the avg pot size on folds
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnFolds() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.FOLD, true, filter);
	}
	
	/**
	 * Gets the avg pot size on wins
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnWins() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.WIN, true, filter);
	}
	
	/**
	 * Gets the avg pot size on loses
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnLoses() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.LOSS, true, filter);
	}
	
	/**
	 * Gets the total amount of money won
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsWon() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.POT, 
													PokerAction.WIN, true, filter);
	}
	
	/**
	 * Gets the total amount of money losses
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsLoss() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.POT, 
													PokerAction.LOSS, true, filter);
	}
	
	/**
	 * Gets the total amount of pots played in
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsFolded() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.POT, 
													PokerAction.FOLD, true, filter);
	}
	
	/**
	 * Gets the winning pot percentage
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public double getWinPercentage() throws DatabaseInterfaceException
	{
		double denom = (double) getTotalNumberOfPots();
		double num = (double) this.getTotalNumberOfPotsWon();
		
		if(denom == 0 && num > 0)
			return 100;
		else if(denom == 0)
			return 0;
		else
			return (num/denom) * 100;

		
	}
	
	/**
	 * Gets the total amount of money generated
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getMoneyGenerate() throws DatabaseInterfaceException
	{
		return dbInterface.getMoneyGenerated(account, filter);
	}
	
	public int getTotalNumberOfPots() throws DatabaseInterfaceException
	{
		return (this.getTotalNumberOfFolds() + this.getTotalNumberOfPotsLoss()+this.getTotalNumberOfPotsWon());
	}
	
	/**
	 * Gets the total number of games played
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getGamesPlayed() throws DatabaseInterfaceException
	{
		return dbInterface.getGamesPlayed(account, filter);
	}
	
	/**
	 * Gets the total delta money generated by the user
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getNetMoney() throws DatabaseInterfaceException
	{
		return this.getTotalDollarsWon() - this.getTotalDollarsFolded() - this.getTotalDollarsLoss() - getMoneyGenerate();
	}

	
}
