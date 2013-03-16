package game;
import java.util.*;

public class Card {
	private int rank, suit;
	
	private static String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
    private static String[] suits = {"Diamonds", "Clubs", "Hearts", "Spades"};
    
    public Card(int rank, int suit) {
    	this.rank = rank;
    	this.suit = suit;
    }
    
    public void setRank(int rank){
    	this.rank = rank;
    }
    
    public void setSuit(int suit){
    	this.suit = suit;
    }
    
    public int getSuit() {
    	return this.suit;
    }
    
    public int getRank() {
    	return this.rank;
    }
    public String toString() {
    	return this.ranks[this.rank]+ " of " + this.suits[this.suit]; 
    }
	
}

class rankComparator implements Comparator<Object>{
    public int compare(Object card1, Object card2) throws ClassCastException{
        // verify two Card objects are passed in
        if (!((card1 instanceof Card) && (card2 instanceof Card))){
            throw new ClassCastException("A Card object was expected.  Parameter 1 class: " + card1.getClass() 
                    + " Parameter 2 class: " + card2.getClass());
        }

        int rank1 = ((Card)card1).getRank();
        int rank2 = ((Card)card2).getRank();

        return rank1 - rank2;
    }
}

class suitComparator implements Comparator<Object>{
    public int compare(Object card1, Object card2) throws ClassCastException{
        // verify two Card objects are passed in
        if (!((card1 instanceof Card) && (card2 instanceof Card))){
            throw new ClassCastException("A Card object was expected.  Parameter 1 class: " + card1.getClass() 
                    + " Parameter 2 class: " + card2.getClass());
        }

        int suit1 = ((Card)card1).getSuit();
        int suit2 = ((Card)card2).getSuit();

        return suit1 - suit2;
    }
}