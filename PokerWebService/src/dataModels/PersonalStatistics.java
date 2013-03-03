package dataModels;

import dataModels.GameAction.PokerAction;
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
	
	private int totalDollarsBetOnCalls;
	private int totalDollarsBetOnBets;
	private int totalDollarsBetOnRaises;
	private int totalDollarsBetOnReRaises;
	private int totalDollarsBet;
	private int avgDollarsBetOnCalls;
	private int avgDollarsBetOnBets;
	private int avgDollarsBetOnRaises;
	private int avgDollarsBetOnReRaises;
	private int totalNumberOfBets;
	private int totalNumberOfChecks;
	private int totalNumberOfCalls;
	private int totalNumberOfFolds;
	private int totalNumberOfRaise;
	private int totalNumberOfReRaise;
	private int totalNumberOfPotsWon;
	private int totalNumberOfPotsLoss;
	private int avgPotOnChecks;
	private int avgPotOnCalls;
	private int avgPotOnBets;
	private int avgPotOnRaises;
	private int avgPotOnReRaises;
	private int avgPotOnFolds;
	private int avgPotOnWins;
	private int avgPotOnLoses;
	private int totalDollarsWon;
	private int totalDollarsLoss;
	private int totalDollarsFolded;
	private double winPercentage;
	private int moneyGenerated;
	private int gamesPlayed;
	private int deltaMoney;
	private int avgBet;
	private int deltaMoneyRanking;
	
	/**
	 * General Constructor
	 * @param account
	 * @param dbInterface
	 */
	public PersonalStatistics(Account account, DatabaseInterface dbInterface)
	{
		this.dbInterface = dbInterface;
		this.account = account;
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
		deltaMoney = deltaMoney();
		deltaMoneyRanking = getDeltaMoneyRank();
		avgBet = getAvgBet();
	}
	
	/**
	 * Retrieves the ranking of the user based on the delta money
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getDeltaMoneyRank() throws DatabaseInterfaceException
	{
		return dbInterface.getUserDeltaMoneyRanking(account);
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
													PokerAction.CALL, false);
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
													PokerAction.BET, false);
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
													PokerAction.RAISE, false);
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
													PokerAction.RERAISE, false);
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
				null, false);
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
													PokerAction.CALL, true);
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
													PokerAction.BET, true);
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
													PokerAction.RAISE, true);
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
													PokerAction.RERAISE, true);
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
													null, true);
	}
	
	/**
	 * Gets the number of bets made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfBets() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.BET, account);
	}
	
	/**
	 * Gets the number of checks made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfChecks() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.CHECK, account);
	}
	
	/**
	 * Gets the number of calls made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfCalls() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.CALL, account);
	}
	
	/**
	 * Gets the number of folds made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfFolds() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.FOLD, account);
	}
	
	/**
	 * Gets the number of raises made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfRaise() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.RAISE, account);
	}
	
	/**
	 * Gets the number of reraises made
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfReRaise() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.RERAISE, account);
	}
	
	/**
	 * Gets the number of pots won
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfPotsWon() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.WIN, account);
	}
	
	/**
	 * Gets the number of pots lost
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalNumberOfPotsLoss() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsCount(PokerAction.LOSS, account);
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
													PokerAction.CHECK, true);
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
													PokerAction.CALL, true);
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
													PokerAction.BET, true);
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
													PokerAction.RAISE, true);
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
													PokerAction.RERAISE, true);
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
													PokerAction.FOLD, true);
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
													PokerAction.WIN, true);
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
													PokerAction.LOSS, true);
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
													PokerAction.WIN, true);
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
													PokerAction.LOSS, true);
	}
	
	/**
	 * Gets the total amount of money folded away
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getTotalDollarsFolded() throws DatabaseInterfaceException
	{
		return dbInterface.getGameActionsTypedNum(account, 
													NumerableActionOperation.SUM, 
													NumerableActionColumn.POT, 
													PokerAction.FOLD, true);
	}
	
	/**
	 * Gets the winning pot percentage
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public double getWinPercentage() throws DatabaseInterfaceException
	{
		double denom = (double) (this.getTotalNumberOfFolds() + this.getTotalNumberOfPotsLoss());
		double num = (double) this.getTotalDollarsWon();
		
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
		return dbInterface.getMoneyGenerated(account);
	}
	
	/**
	 * Gets the total number of games played
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getGamesPlayed() throws DatabaseInterfaceException
	{
		return dbInterface.getGamesPlayed(account);
	}
	
	/**
	 * Gets the total delta money generated by the user
	 * 
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int deltaMoney() throws DatabaseInterfaceException
	{
		return this.getTotalDollarsWon() - this.getTotalDollarsFolded() - this.getTotalDollarsLoss() - getMoneyGenerate();
	}

	
}
