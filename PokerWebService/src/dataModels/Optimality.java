package dataModels;

import java.util.ArrayList;
import java.util.Hashtable;

import util.PokerHandRanker;
import dataModels.GameAction.PokerAction;

public class Optimality {

	
	public static final double POSTIVE_ACTION_VALUE = 1;
	public static final double NEGATIVE_ACTION_VALUE = 0;
	
	/**
	 * In the case of two cards (pre-flop), we will use precalculated EV's.
	 * These values are based on:
	 * 
	 * The statistics are based on 115,591,080 pair of pocket cards dealt at the real money tables. 
	 * The unit for EV is average profit in big bets.
	 * 
	 * Because they are based on big bets, they might be more optimistic for our cases,
	 * but they will work.
	 * 
	 * http://www.tightpoker.com/poker_hands.html
	 * 
	 */
	private static final Hashtable<String, Double> dealtHandEVs = new Hashtable<String, Double>();
	
	static
	{
		dealtHandEVs.put("AA", 2.32 );
    	dealtHandEVs.put("KK", 1.67 );
    	dealtHandEVs.put("QQ", 1.22 );
    	dealtHandEVs.put("JJ", 0.86 );
    	dealtHandEVs.put("AK s", 0.78 );
    	dealtHandEVs.put("AQ s", 0.59 );
    	dealtHandEVs.put("TT", 0.58 );
    	dealtHandEVs.put("AK", 0.51 );
    	dealtHandEVs.put("AJ s", 0.44 );
    	dealtHandEVs.put("KQ s", 0.39 );
    	dealtHandEVs.put("99", 0.38 );
    	dealtHandEVs.put("AT s", 0.32 );
    	dealtHandEVs.put("AQ", 0.31 );
    	dealtHandEVs.put("KJ s", 0.29 );
    	dealtHandEVs.put("88", 0.25 );
    	dealtHandEVs.put("QJ s", 0.23 );
    	dealtHandEVs.put("KT s", 0.20 );
    	dealtHandEVs.put("A9 s", 0.19 );
    	dealtHandEVs.put("AJ", 0.19 );
    	dealtHandEVs.put("QT s", 0.17 );
    	dealtHandEVs.put("KQ", 0.16 );
    	dealtHandEVs.put("77", 0.16 );
    	dealtHandEVs.put("JT s", 0.15 );
    	dealtHandEVs.put("A8 s", 0.10 );
    	dealtHandEVs.put("K9 s", 0.09 );
    	dealtHandEVs.put("AT", 0.08 );
    	dealtHandEVs.put("A5 s", 0.08 );
    	dealtHandEVs.put("A7s", 0.08 );
    	dealtHandEVs.put("KJ", 0.08 );
    	dealtHandEVs.put("66", 0.07 );
    	dealtHandEVs.put("T9 s", 0.05 );
    	dealtHandEVs.put("A4 s", 0.05 );
    	dealtHandEVs.put("Q9 s", 0.05 );
    	dealtHandEVs.put("J9 s", 0.04 );
    	dealtHandEVs.put("QJ", 0.03 );
    	dealtHandEVs.put("A6 s", 0.03 );
    	dealtHandEVs.put("55", 0.02 );
    	dealtHandEVs.put("A3 s", 0.02 );
    	dealtHandEVs.put("K8 s", 0.01 );
    	dealtHandEVs.put("KT", 0.01 );
    	dealtHandEVs.put("98 s", 0.00 );
    	dealtHandEVs.put("T8 s", -0.00 );
    	dealtHandEVs.put("K7 s", -0.00 );
    	dealtHandEVs.put("A2 s", 0.00 );
    	dealtHandEVs.put("87 s", -0.02 );
    	dealtHandEVs.put("QT", -0.02 );
    	dealtHandEVs.put("Q8 s", -0.02 );
    	dealtHandEVs.put("44", -0.03 );
    	dealtHandEVs.put("A9", -0.03 );
    	dealtHandEVs.put("J8 s", -0.03 );
    	dealtHandEVs.put("76 s", -0.03 );
    	dealtHandEVs.put("JT", -0.03 );
    	dealtHandEVs.put("97 s", -0.04 );
    	dealtHandEVs.put("K6 s", -0.04 );
    	dealtHandEVs.put("K5 s", -0.05 );
    	dealtHandEVs.put("K4 s", -0.05 );
    	dealtHandEVs.put("T7 s", -0.05 );
    	dealtHandEVs.put("Q7 s", -0.06 );
    	dealtHandEVs.put("K9", -0.07 );
    	dealtHandEVs.put("65 s", -0.07 );
    	dealtHandEVs.put("T9", -0.07 );
    	dealtHandEVs.put("86 s", -0.07 );
    	dealtHandEVs.put("A8", -0.07 );
    	dealtHandEVs.put("J7 s", -0.07 );
    	dealtHandEVs.put("33", -0.07 );
    	dealtHandEVs.put("54 s", -0.08 );
    	dealtHandEVs.put("Q6 s", -0.08 );
    	dealtHandEVs.put("K3 s", -0.08 );
    	dealtHandEVs.put("Q9", -0.08 );
    	dealtHandEVs.put("75 s", -0.09 );
    	dealtHandEVs.put("22", -0.09 );
    	dealtHandEVs.put("J9", -0.09 );
    	dealtHandEVs.put("64 s", -0.09 );
    	dealtHandEVs.put("Q5 s", -0.09 );
    	dealtHandEVs.put("K2 s", -0.09 );
    	dealtHandEVs.put("96 s", -0.09 );
    	dealtHandEVs.put("Q3 s", -0.10 );
    	dealtHandEVs.put("J8", -0.10 );
    	dealtHandEVs.put("98", -0.10 );
    	dealtHandEVs.put("T8", -0.10 );
    	dealtHandEVs.put("97", -0.10 );
    	dealtHandEVs.put("A7", -0.10 );
    	dealtHandEVs.put("T7", -0.10 );
    	dealtHandEVs.put("Q4 s", -0.10 );
    	dealtHandEVs.put("Q8", -0.11 );
    	dealtHandEVs.put("J5 s", -0.11 );
    	dealtHandEVs.put("T6", -0.11 );
    	dealtHandEVs.put("75", -0.11 );
    	dealtHandEVs.put("J4 s", -0.11 );
    	dealtHandEVs.put("74 s", -0.11 );
    	dealtHandEVs.put("K8", -0.11 );
    	dealtHandEVs.put("86", -0.11 );
    	dealtHandEVs.put("53 s", -0.11 );
    	dealtHandEVs.put("K7", -0.11 );
    	dealtHandEVs.put("63 s", -0.11 );
    	dealtHandEVs.put("J6 s", -0.11 );
    	dealtHandEVs.put("85", -0.11 );
    	dealtHandEVs.put("T6 s", -0.11 );
    	dealtHandEVs.put("76", -0.11 );
    	dealtHandEVs.put("A6", -0.12 );
    	dealtHandEVs.put("T2", -0.12 );
    	dealtHandEVs.put("95 s", -0.12 );
    	dealtHandEVs.put("84", -0.12 );
    	dealtHandEVs.put("62", -0.12 );
    	dealtHandEVs.put("T5 s", -0.12 );
    	dealtHandEVs.put("95", -0.12 );
    	dealtHandEVs.put("A5", -0.12 );
    	dealtHandEVs.put("Q7", -0.12 );
    	dealtHandEVs.put("T5", -0.12 );
    	dealtHandEVs.put("87", -0.12 );
    	dealtHandEVs.put("83", -0.12 );
    	dealtHandEVs.put("65", -0.12 );
    	dealtHandEVs.put("Q2 s", -0.12 );
    	dealtHandEVs.put("94", -0.12 );
    	dealtHandEVs.put("74", -0.12 );
    	dealtHandEVs.put("54", -0.12 );
    	dealtHandEVs.put("A4", -0.12 );
    	dealtHandEVs.put("T4", -0.12 );
    	dealtHandEVs.put("82", -0.12 );
    	dealtHandEVs.put("64", -0.12 );
    	dealtHandEVs.put("42", -0.12 );
    	dealtHandEVs.put("J7", -0.12 );
    	dealtHandEVs.put("93", -0.12 );
    	dealtHandEVs.put("85 s", -0.12 );
    	dealtHandEVs.put("73", -0.12 );
    	dealtHandEVs.put("53", -0.12 );
    	dealtHandEVs.put("T3", -0.12 );
    	dealtHandEVs.put("63", -0.12 );
    	dealtHandEVs.put("K6", -0.12 );
    	dealtHandEVs.put("J6", -0.12 );
    	dealtHandEVs.put("96", -0.12 );
    	dealtHandEVs.put("92", -0.12 );
    	dealtHandEVs.put("72", -0.12 );
    	dealtHandEVs.put("52", -0.12 );
    	dealtHandEVs.put("Q4", -0.13 );
    	dealtHandEVs.put("K5", -0.13 );
    	dealtHandEVs.put("J5", -0.13 );
    	dealtHandEVs.put("43 s", -0.13 );
    	dealtHandEVs.put("Q3", -0.13 );
    	dealtHandEVs.put("43", -0.13 );
    	dealtHandEVs.put("K4", -0.13 );
    	dealtHandEVs.put("J4", -0.13 );
    	dealtHandEVs.put("T4 s", -0.13 );
    	dealtHandEVs.put("Q6", -0.13 );
    	dealtHandEVs.put("Q2", -0.13 );
    	dealtHandEVs.put("J3 s", -0.13 );
    	dealtHandEVs.put("J3", -0.13 );
    	dealtHandEVs.put("T3 s", -0.13 );
    	dealtHandEVs.put("A3", -0.13 );
    	dealtHandEVs.put("Q5", -0.13 );
    	dealtHandEVs.put("J2", -0.13 );
    	dealtHandEVs.put("84 s", -0.13 );
    	dealtHandEVs.put("82 s", -0.14 );
    	dealtHandEVs.put("42 s", -0.14 );
    	dealtHandEVs.put("93 s", -0.14 );
    	dealtHandEVs.put("73 s", -0.14 );
    	dealtHandEVs.put("K3", -0.14 );
    	dealtHandEVs.put("J2 s", -0.14 );
    	dealtHandEVs.put("92 s", -0.14 );
    	dealtHandEVs.put("52 s", -0.14 );
    	dealtHandEVs.put("K2", -0.14 );
    	dealtHandEVs.put("T2 s", -0.14 );
    	dealtHandEVs.put("62 s", -0.14 );
    	dealtHandEVs.put("32", -0.14 );
    	dealtHandEVs.put("A2", -0.15 );
    	dealtHandEVs.put("83 s", -0.15 );
    	dealtHandEVs.put("94 s", -0.15 );
    	dealtHandEVs.put("72 s", -0.15 );
    	dealtHandEVs.put("32 s", -0.15 );
	}
	
	
	public double getOptimalRatingForGame(Game game)
	{
		ArrayList<Double> opValues = new ArrayList<Double>();
		
		ArrayList<GameAction> actions = game.getGameActions();
		
		for(GameAction gameAction : actions)
		{
			PokerAction action = gameAction.getAction();
			
			/*
			 * In the case of checks, wins, loses, etc
			 * where actions are not dependent on a change in
			 * pot value, we are not going to model the interaction.
			 * 
			 * Because we explicitly wanted or model to look at only
			 * the knowledge the player has at the time. Other actions
			 * will not judge additional risk(ex: check), or actions such as fold 
			 * are not not tracked, since we tracked all risky actions
			 * previous to this action. In essence we are assuming most
			 * players have a general idea when to fold, and this is a much
			 * less important measure than the other actions.
			 */
			if(action != PokerAction.BET && 
			   action != PokerAction.CALL && 
			   action != PokerAction.RAISE && 
			   action != PokerAction.RERAISE)
				continue;
			
			ArrayList<Card> commCards = gameAction.getCommunityCards();
			ArrayList<Card> handCards = gameAction.getHand();
			
			int pot = gameAction.getPot();
			int bet = gameAction.getBet();
			
			if(this.isPositiveEV(pot, bet, handCards, commCards))
				opValues.add(new Double(1));
			else
				opValues.add(new Double(0));
			
		}
		
		double sum = 0;
		int count = 0;
		for(double opValue : opValues)
		{
			sum += opValue;
			count++;
		}
		
		if(count == 0) return 0;
		
		return (sum / count) * 100;
		
	}
	
	public double preFlopOptimalityRating(ArrayList<Card> hand)
	{
		double EV = this.getPreFlopEV(hand);
		
		if(EV > 0)
			return POSTIVE_ACTION_VALUE;
		
		
		return NEGATIVE_ACTION_VALUE;
		
	}
	
	public double getPreFlopEV(ArrayList<Card> hand)
	{
		if(hand == null || hand.size() != 2)
		{
			return Double.NEGATIVE_INFINITY;
		}
		
		String suitExt = "";
		Card card1 = hand.get(0);
		Card card2 = hand.get(1);
		String card1String = card1.getNumber().getStringValue();
		String card2String = card2.getNumber().getStringValue();
		
		
		if(card1.getSuit() == card2.getSuit())
			suitExt = " s";
		
		String handID = card1String+card2String+suitExt;
		Double value = dealtHandEVs.get(handID);
		
		if(value == null)
			{
			handID = card2String+card1String+suitExt;
			value = dealtHandEVs.get(handID);
			}
		
		if(value == null) return Double.NEGATIVE_INFINITY;
		
		return value;
		
	}
	
	public static double getNextCardFutureRankings(int pot, int bet, int comm[], int hand[])
	{
		int commLength = comm.length;
		int totalCards = commLength + hand.length;
		double ev = 0;
		
		double leftOverCards = 52-totalCards;
				//card 1
				//each suit
				for(int c1S = 0; c1S < 4; c1S++)
				{
					//each number
					for(int c1N = 0; c1N < 13;c1N++)
					{
						int card1 = c1N * 4 + c1S + 1;
						
						boolean skip = false;

						for(int commCard : comm)
							if(commCard == card1)
								{skip = true;break;}
						if(!skip)
						for(int handCard : hand)
							if(handCard == card1)
								{skip = true;break;}
						
						if(skip) continue;
						
						
						
						int[] newComm = new int[commLength+1];
						
						for(int i = 0; i < commLength; i++)
							newComm[i] = comm[i];

						newComm[commLength] = card1;
								
							ev+= (1/(leftOverCards))*getHandEV(pot, bet, newComm, hand);
					}
					
				}
				
				return ev;
	}
	
	public boolean isPositiveEV(int pot, int bet, ArrayList<Card> handCards, ArrayList<Card> commCards)
	{
		
		double ev = getTotalEv(pot, bet, handCards, commCards);
		
		if(ev > 0)
			return true;
		else 
			return false;
		
	}
	
	public double getTotalEv(int pot, int bet, ArrayList<Card> handCards, ArrayList<Card> commCards)
	{
		
		int hand[] = PokerHandRanker.convertHand(handCards);
		int comm[] = PokerHandRanker.convertHand(commCards);
		
		int totalCards = commCards.size() + handCards.size();

		/*
		 * Pre-Flop
		 */
		if(totalCards == 2)
			return preFlopOptimalityRating(handCards);
		
		/*
		 * Post Flop or Post Turn
		 *
		 * deal with 2 + 3 Looking at 4th (flop)
		 * or deal with 2 + 4 Looking at 5th
		 */
		if(totalCards==5 || totalCards==6)
		{
			return getNextCardFutureRankings(pot, bet, comm, hand);
		}
		
		/*
		 * Post River
		 */
		//deal with 7 Looking at Everything.
		if(totalCards==7)
			return getHandEV(pot, bet, comm, hand);
		
		
		return 0;
		
	}
	
	private static double getHandEV(int pot, int bet, int comm[], int hand[])
	{
		double ev = 0;
		int cardCount = comm.length + hand.length;
		int[] myHand = new int[cardCount];
		
		double leftOverCards = 52-cardCount;
		
		System.arraycopy(comm, 0, myHand, 0, comm.length);
		System.arraycopy(hand, 0, myHand, comm.length, hand.length);
		
		int myHandRating = PokerHandRanker.lookupHand(myHand);
		
		//card 1
		//each suit
		for(int c1S = 0; c1S < 4; c1S++)
		{
			//each number
			for(int c1N = 0; c1N<13;c1N++)
			{
				//card 2
				//each suit
				for(int c2S = 0; c2S < 4; c2S++)
				{
					//each number
					for(int c2N = 0; c2N<13;c2N++)
					{
						int card1 = c1N * 4 + c1S + 1;
						int card2 = c2N * 4 + c2S + 1;
						
						boolean skip = false;
						if(card1==card2)
							skip=true;
						
						if(!skip)
						for(int commCard : comm)
							if(commCard == card1 || commCard == card2)
								{skip = true;break;}
						if(!skip)
						for(int handCard : hand)
							if(handCard == card1 || handCard == card2)
								{skip = true;break;}
						
						
						if(skip) continue;
						
						int[] newHand = {card1, card2};
						int[] oppHand = new int[cardCount];
						System.arraycopy(comm, 0, oppHand, 0, comm.length);
						System.arraycopy(newHand, 0, oppHand, comm.length, newHand.length);
						
						int oppHandRating = PokerHandRanker.lookupHand(oppHand);
						
						//System.out.println(l++);
						
						if(myHandRating > oppHandRating)
							ev+= (1/leftOverCards)*(1/(leftOverCards-1))* pot;
						else
							ev+= (1/leftOverCards)*(1/(leftOverCards-1))* -bet;
						
					}
				}
				
				
			}
		}
			
			return ev;
	}
	
	
	
}
