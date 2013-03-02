package dataModels;

import java.util.ArrayList;


public class Card {

	private Suit suit;
	private Number number;
	
	public Card(Suit suit, Number number)
	{
		this.suit = suit;
		this.number = number;
	}
	
	/**
	 * H# 2-9, HT, HJ, HQ, HK, HA
	 * D# 2-9, HT, DJ, DQ, DK, DA
	 * C# 2-9, HT, CJ, CQ, CK, CA
	 * S# 2-9, HT, SJ, SQ, SK, SA
	 * 
	 * @param card
	 */
	public Card(String card)
	{
		if(card == null || isValidCard(card))
		{
			this.suit = Suit.UNKNOWN;
			this.number = Number.UNKNOWN;
		}
		
		suit = Suit.getSuit(card.charAt(0));
		number = Number.getNumber(card.charAt(1));
			
	}
	
	
	
	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public Number getNumber() {
		return number;
	}

	public void setNumber(Number number) {
		this.number = number;
	}
	
	public static ArrayList<Card> generateCards(String cards)
	{
		ArrayList<Card> cardList = new ArrayList<Card>();
		String[] cardArray = cards.split(";");

		if(cards != null && cards.length() > 1)
			for(String card : cardArray)
			{
				cardList.add(new Card(card));
			}
			
		return cardList;
		
	}
	
	public static boolean validCardString(String cards)
	{
		if(cards == null || cards.length() < 2) return false;
		
		String[] cardArray = cards.split(";");

		for(String card : cardArray)
		{
			if(!isValidCard(card))
				return false;
		}
		
		return true;
		
	}
	
	public static boolean isValidCard(String card)
	{
		if(card == null || card.length() != 2)
		{
			return false;
		}
		
		Suit suit = Suit.getSuit(card.charAt(0));
		Number number = Number.getNumber(card.charAt(1));
		
		if(suit == Suit.UNKNOWN || number == Number.UNKNOWN)
		{
			return false;
		}
		
		return true;
		
		
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(suit.getValue());
		builder.append(number.getValue());
		
		return builder.toString();
	}
	
	public enum Number
	{
		TWO('2'), THREE('3'), FOUR('4'), FIVE('5'), SIX('6'), SEVEN('7'), EIGHT('8'), NINE('9'), TEN('T'),
		JACK('J'), QUEEN('Q'), KING('K'), ACE('A'), UNKNOWN('U');
		
		private Character value = 'U';
		
		Number(char suit)
		{
			this.value = suit;
		}
		
	    public char getValue() {
	        return value;
	    }
	    
	    public static Number getNumber(char character)
	    {
	    	for (Number num : Number.values()) {
	            if (num.getValue() == character) {
	                return num;
	            }
	        }
	    	
	    	return Number.UNKNOWN;
	    }
	}
	
	public enum Suit
	{
		HEARTS('H'), DIAMONDS('D'), CLUBS('C'), SPADES('S'), UNKNOWN('U');
		
		private Character value = 'U';
		
		Suit(char suit)
		{
			this.value = suit;
		}
		
	    public char getValue() {
	        return value;
	    }
	    
	    public static Suit getSuit(char character)
	    {
	    	for (Suit suit : Suit.values()) {
	            if (suit.getValue() == character) {
	                return suit;
	            }
	        }
	    	
	    	return Suit.UNKNOWN;
	    }
		
	}
	
}
