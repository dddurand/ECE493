package util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import dataModels.Card;
import dataModels.Card.Number;
import dataModels.Card.Suit;

/**
 * This class provides an efficient means to determine poker hands rankings,
 * and types.
 * 
 * The class uses a lookup table derived from:
 * This code forms the basis of the "Two Plus Two 7-Card Evaluator" developed by the 2+2
   crowd and packaged into reference form by Ray Wotton.
 *  
 * Adapted from:
 * http://www.codingthewheel.com/archives/poker-hand-evaluator-roundup/#2p2
 * http://pokerai.org/pf3/viewtopic.php?f=3&t=4388
 * https://gist.github.com/eluctari/832122
 *   
 * @SRS 3.2.2.8
 * @author dddurand
 *
 */
public class PokerHandRanker {
	
	//private static final String[] HANDRANKS = {"BAD!!","High Card","Pair","Two Pair","Three of a Kind","Straight","Flush","Full House","Four of a Kind","Straight Flush"};
	private static final int TABLE_SIZE = 32487834;
	private static final int INT_SIZE = 4;
	private static final int[] HR = new int[TABLE_SIZE];
	
	private static final HashMap<Suit, Integer> suits = new HashMap<Suit, Integer>();
	private static final HashMap<Number, Integer> numbers = new HashMap<Number, Integer>();
	
	/**
	 * Load table into memory when class is initially created
	 * Everything is read only, so we don't worry about sync. issues.
	 * 
	 * Adapted from:
	 * https://gist.github.com/eluctari/832122
	 * 
	 */
	static {
        int tableSize = TABLE_SIZE * INT_SIZE;
        byte[] b = new byte[tableSize];
        InputStream input = null;
        try {
        	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        	input = classLoader.getResourceAsStream("../tables/HandRanks.dat");
        	
        	//For testing
        	if(input == null)
        	{	
        		input = new FileInputStream(new File("./tables/HandRanks.dat"));
        	}
        	
        	input.read(b, 0, tableSize);
        } catch (FileNotFoundException e) {
           e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            try {
            	input.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
            
            for (int i = 0; i < TABLE_SIZE; i++) {
                HR[i] = littleEndianByteArrayToInt(b, i * 4);
            }
            
            initCardConversion();
        }
    }
	
	private static void initCardConversion()
	{
		suits.put(Suit.CLUBS,new Integer(0));
		suits.put(Suit.DIAMONDS,new Integer(1));
		suits.put(Suit.HEARTS,new Integer(2));
		suits.put(Suit.SPADES,new Integer(3));
		
		numbers.put(Number.TWO,new Integer(0));
		numbers.put(Number.THREE,new Integer(1));
		numbers.put(Number.FOUR,new Integer(2));
		numbers.put(Number.FIVE,new Integer(3));
		numbers.put(Number.SIX,new Integer(4));
		numbers.put(Number.SEVEN,new Integer(5));
		numbers.put(Number.EIGHT,new Integer(6));
		numbers.put(Number.NINE,new Integer(7));
		numbers.put(Number.TEN,new Integer(8));
		numbers.put(Number.JACK,new Integer(9));
		numbers.put(Number.QUEEN,new Integer(10));
		numbers.put(Number.KING,new Integer(11));
		numbers.put(Number.ACE,new Integer(12));
		
	}
	
	/**
     * Converts a little-endian byte array to a Java (big-endian) integer.
     * We need this because the HandRanks.dat file was generated using
     * a little-endian C program and we want to maintain compatibility.
     * 
     * Cited:
     * https://gist.github.com/eluctari/832122
     * 
     * @param b
     * @param offset
     * @return
     */
    private static final int littleEndianByteArrayToInt(byte[] b, int offset) {
        return (b[offset + 3] << 24) + ((b[offset + 2] & 0xFF) << 16)
                + ((b[offset + 1] & 0xFF) << 8) + (b[offset] & 0xFF);
    }
	
    /**
     * An adapted algorithm for determining a cards ranking & type.
     * 
     * Adapted from:
     * http://pokerai.org/pf3/viewtopic.php?f=3&t=4388
     * 
     * @param pCards
     * @return
     */
	public static int lookupHand(int[] pCards)
	{
	   int numCards = pCards.length;
	   int position = 0;
	   int initialOffset = 53;
	   
	   for(int i = 0; i < numCards; ++i)
	      {
		   initialOffset = HR[initialOffset + pCards[position++]];
	      }
	   
	   // Extra lookup pre-river
	   if(numCards < 7)
	   {
		   initialOffset= HR[initialOffset];
	   }
	      
	   return initialOffset;
	}
	
	/**
	 * Converts a given card into its int equivilent in the lookup table
	 * 
	 * @param card Card to be converted
	 * @return Card's int representation
	 */
	public static int convertCard(Card card)
	{
		return numbers.get(card.getNumber()) * 4 + suits.get(card.getSuit()) + 1;
	}
	
	/**
	 * Converts a given list of cards into and int array of equivilent cards in the lookup table
	 * 
	 * @param card Card to be converted
	 * @return Card's int representation
	 */
	public static int[] convertHand(ArrayList<Card> cards)
	{
		int cardSize = cards.size();
		int cardsArray[] = new int[cardSize];
		
		for(int i = 0; i < cardSize; i++)
		{
			cardsArray[i] = convertCard(cards.get(i));
		}
		
		return cardsArray;
	}

}
