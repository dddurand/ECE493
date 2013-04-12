package dataModels;

import java.util.ArrayList;

/**
 * Representation for a game card.
 * This class also contains a variety of utility function for dealing
 * with card hands & collections.
 * 
 * @SRS 3.2.2.3 -> 3.2.2.6
 * @author dddurand
 *
 */
public class Card {

	private Suit suit;
	private Number number;
	
	/**
	 * General Constructor
	 * 
	 * @param suit Suit of the card
	 * @param number Number of the card
	 */
	public Card(Suit suit, Number number)
	{
		this.suit = suit;
		this.number = number;
	}
	
	/**
	 * Constructor based on a string representation of the card.
	 * 
	 * H# 2-9, HT, HJ, HQ, HK, HA
	 * D# 2-9, HT, DJ, DQ, DK, DA
	 * C# 2-9, HT, CJ, CQ, CK, CA
	 * S# 2-9, HT, SJ, SQ, SK, SA
	 * 
	 * @param card
	 */
	public Card(String card)
	{
		if(card == null || !isValidCard(card))
		{
			this.suit = Suit.UNKNOWN;
			this.number = Number.UNKNOWN;
			return;
		}
		
		suit = Suit.getSuit(card.charAt(0));
		number = Number.getNumber(card.charAt(1));
			
	}
	
	
	/**
	 * Retrieves the suit for the given card
	 * @return
	 */
	public Suit getSuit() {
		return suit;
	}

	/**
	 * Sets the suit for the given card
	 * @param suit
	 */
	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	/**
	 * Gets the number for the current card
	 * @return
	 */
	public Number getNumber() {
		return number;
	}

	/**
	 * Sets the number for the current card
	 * @param number
	 */
	public void setNumber(Number number) {
		this.number = number;
	}
	
	/**
	 * Converts a collection of Card objects into
	 * a string representation delimited by ; between each card
	 * 
	 * @param cards Collection of card objects
	 * @return String representation of the card collection
	 */
	public static String getCardsAsString(ArrayList<Card> cards)
	{
		StringBuilder builder = new StringBuilder();
		
		for(Card card : cards)
		{
			builder.append(card.toString());
			builder.append(";");
		}
		
		String cardResult = builder.toString();
		
		if(cardResult.length() > 0)
			cardResult = cardResult.substring(0, cardResult.length()-1);
		
		return cardResult;
	}
	
	/**
	 * Generates a collection of cards based on a ; delimited card representation.
	 * H2:D7
	 * 
	 * @param cards A string representation of a collection of cards
	 * @return A collection of cards
	 */
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
	
	/**
	 * Determines if the provided string is a valid string representation of
	 * a collection of cards
	 * 
	 * @param cards
	 * @return
	 */
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
	
	/**
	 * Determines if the given string is a valid representation of a card
	 * @param card A string
	 * @return true if represents a card, false otherwise
	 */
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
	
	/**
	 * Enum representing all possible numbers for a card
	 * @author dddurand
	 *
	 */
	public enum Number
	{
		TWO('2'), THREE('3'), FOUR('4'), FIVE('5'), SIX('6'), SEVEN('7'), EIGHT('8'), NINE('9'), TEN('T'),
		JACK('J'), QUEEN('Q'), KING('K'), ACE('A'), UNKNOWN('U');
		
		private Character value = 'U';
		
		/**
		 * Enum constructor
		 * @param suit
		 */
		Number(char suit)
		{
			this.value = suit;
		}
		
		/**
		 * Returns char representation of the card number
		 * 
		 * @return
		 */
	    public char getValue() {
	        return value;
	    }
	    
	    public String getStringValue() {
	        return new StringBuilder().append(value).toString();
	    }
	    
	    /**
	     * Retrieves the enum Number for a given char
	     * Unknown is returned for an invalid char
	     * 
	     * @param character
	     * @return
	     */
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
	
	/**
	 * Enum representing all possible suits for a card
	 * @author dddurand
	 *
	 */
	public enum Suit
	{
		HEARTS('H'), DIAMONDS('D'), CLUBS('C'), SPADES('S'), UNKNOWN('U');
		
		private Character value = 'U';
		
		/**
		 * General Enum Constructor
		 * @param suit
		 */
		Suit(char suit)
		{
			this.value = suit;
		}
		
		/**
		 * Returns the char representation of the suit
		 * 
		 * @return
		 */
	    public char getValue() {
	        return value;
	    }
	    
	    public String getStringValue() {
	        return new StringBuilder().append(value).toString();
	    }
	    
	    /**
	     * Returns the suit for a given char. If invalid, returns unknown.
	     * 
	     * @param character
	     * @return
	     */
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
