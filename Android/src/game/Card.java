package game;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
/**
 * Simple card class that is used to hold rank and suit
 * @author Lawton
 * @SRS 3.2.1.9
 */
public class Card implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2383050262268152649L;

	private int rank, suit;

	private static String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
	private static String[] suits = {"D", "C", "H", "S"};

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
		return suits[this.suit] + ranks[this.rank];
	}

	public static String getCardsString(Card cards[])
	{
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < cards.length; i++)
		{
			Card card = cards[i];
			if(card==null) continue;

			builder.append(card.toString());
			builder.append(";");
		}

		String result = builder.toString();

		if(result.length() > 0)
			result = result.substring(0, result.length()-1);

		return result;
	}

	/**
	 * This method is used in the serialization of the object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(this.rank);
		out.writeInt(this.suit);
	}


	/**
	 * This method is used in the deserialization of the object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException,
	ClassNotFoundException {
		this.rank = in.readInt();
		this.suit = in.readInt();
	}





	public static class RankComparator implements Comparator<Object>{
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

	public static class SuitComparator implements Comparator<Object>{
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

}