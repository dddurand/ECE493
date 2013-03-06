package dataModels;

import dataModels.GameAction.PokerAction;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.DatabaseInterface.NumerableActionColumn;
import database.DatabaseInterface.NumerableActionOperation;

/**
 * A data model that holds and facilitates the generation of the community statistics.
 * 
 * @author dddurand
 *
 */

public class CommunityStatistics {

	private transient DatabaseInterface dbInterface;
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
	private int moneyGenerated;
	@SuppressWarnings("unused")
	private int gamesPlayed;
	@SuppressWarnings("unused")
	private int netMoney;
	@SuppressWarnings("unused")
	private int avgBet;
	
	/**
	 * General Constructor
	 * @param account
	 * @param dbInterface
	 */
	public CommunityStatistics(DatabaseInterface dbInterface, Filter filter)
	{
		this.dbInterface = dbInterface;
		this.filter = filter;
	}
	
	/**
	 * This method triggers the object to generate all statistics for the community
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
		moneyGenerated = getMoneyGenerate();
		gamesPlayed = getGamesPlayed();
		netMoney = getNetMoney();
		avgBet = getAvgBet();
		totalNumberOfPots = getTotalNumberOfPots();
	}
	
	/**
	 * Gets the total amount of money bet during calls for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsBetOnCalls() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.BET, 
													PokerAction.CALL, false, filter);
	}
	
	/**
	 * Gets the total amount of money bet during initial bet for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsBetOnBets() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.BET, 
													PokerAction.BET, false, filter);
	}
	
	/**
	 * Gets the total amount of money bet during raises for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsBetOnRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.BET, 
													PokerAction.RAISE, false, filter);
	}
	
	/**
	 * Gets the total amount of money bet through reraises for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsBetOnReRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.BET, 
													PokerAction.RERAISE, false, filter);
	}
	
	/**
	 * Gets the total amount of money bet for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsBet() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
				NumerableActionOperation.SUM, 
				NumerableActionColumn.BET, 
				null, false, filter);
	}
	
	/**
	 * Gets the total amount of money bet through calls for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsBetOnCalls() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.BET, 
													PokerAction.CALL, true, filter);
	}
	
	/**
	 * Gets the avg amount of money bet on each bet for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsBetOnBets() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.BET, 
													PokerAction.BET, true, filter);
	}
	
	/**
	 * Gets the avg amount of money bet on each raise for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsBetOnRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.BET, 
													PokerAction.RAISE, true, filter);
	}
	
	/**
	 * Gets the avg amount of money bet on each reraise for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsBetOnReRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.BET, 
													PokerAction.RERAISE, true, filter);
	}
	
	/**
	 * Gets the avg amount of money per bet for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgBet() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.BET, 
													null, true, filter);
	}
	
	/**
	 * Gets the number of bets made for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfBets() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.BET, null, filter);
	}
	
	/**
	 * Gets the number of checks made for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfChecks() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.CHECK, null, filter);
	}
	
	/**
	 * Gets the number of calls made for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfCalls() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.CALL, null, filter);
	}
	
	/**
	 * Gets the number of folds made for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfFolds() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.FOLD, null, filter);
	}
	
	/**
	 * Gets the number of raises made for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfRaise() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.RAISE, null, filter);
	}
	
	/**
	 * Gets the number of reraises made for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfReRaise() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.RERAISE, null, filter);
	}
	
	/**
	 * Gets the number of pots won for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfPotsWon() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.WIN, null, filter);
	}
	
	/**
	 * Gets the number of pots lost for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfPotsLoss() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.LOSS, null, filter);
	}
	
	/**
	 * Gets the avg pot size on check for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnChecks() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.CHECK, true, filter);
	}
	
	/**
	 * Gets the avg pot size on calls for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnCalls() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.CALL, true, filter);
	}
	
	/**
	 * Gets the avg pot size on bets for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnBets() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.BET, true, filter);
	}
	
	/**
	 * Gets the avg pot size on raises for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.RAISE, true, filter);
	}
	
	/**
	 * Gets the avg pot size on reraises for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnReRaises() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.RERAISE, true, filter);
	}
	
	/**
	 * Gets the avg pot size on folds for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnFolds() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
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
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.WIN, true, filter);
	}
	
	/**
	 * Gets the avg pot size on loses for all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getAvgDollarsPotOnLoses() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.AVG, 
													NumerableActionColumn.POT, 
													PokerAction.LOSS, true, filter);
	}
	
	/**
	 * Gets the total amount of money won by all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsWon() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.POT, 
													PokerAction.WIN, true, filter);
	}
	
	/**
	 * Gets the total amount of money losses by all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsLoss() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.POT, 
													PokerAction.LOSS, true, filter);
	}
	
	/**
	 * Gets the total amount of pots played in by all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsFolded() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(null, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.POT, 
													PokerAction.FOLD, true, filter);
	}
	
	/**
	 * Gets the total amount of money generated by all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getMoneyGenerate() throws DatabaseInterfaceException
	{
		return dbInterface.getMoneyGenerated(null, filter);
	}
	
	public int getTotalNumberOfPots() throws DatabaseInterfaceException
	{
		return (this.getTotalNumberOfFolds() + this.getTotalNumberOfPotsLoss()+this.getTotalNumberOfPotsWon());
	}
	
	/**
	 * Gets the total number of games played by all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getGamesPlayed() throws DatabaseInterfaceException
	{
		return dbInterface.getGamesPlayed(null, filter);
	}
	
	/**
	 * Gets the total delta money generated by all users
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getNetMoney() throws DatabaseInterfaceException
	{
		return this.getTotalDollarsWon() - this.getTotalDollarsFolded() - this.getTotalDollarsLoss() - getMoneyGenerate();
	}

	
}
