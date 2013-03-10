package Game;


public class Deck {
	private Card[] currentDeck = new Card[52];
	private int top = -1;
	public Deck() {
		this.shuffle();
	}
	protected Card getCard() {
		if (this.top ==-1) {
			return null;
		} else {
			return this.currentDeck[this.top--];
		}
	}
	
	protected void shuffle() {
		this.resetDeck();
		for(int j=0; j<20;j++) {
			int randomNum = (int) (Math.random() * 52);
			for (int i=51; i>0; i--) {
				randomNum = (int) (Math.random() * (i+1));
				this.swap(i, randomNum);
			}
		}
		
	}
	
	public void resetDeck() {
		this.top =51;
		for (int i =0; i<4; i++) {
			for (int j=0; j<13;j++) {
				int spot = (i*13) +j; 
				this.currentDeck[spot] = new Card(j,i);
			}
		}
	}
	
	private void swap(int i, int j) {
		Card temp = this.currentDeck[i];
		this.currentDeck[i] =this.currentDeck[j];
		this.currentDeck[j] = temp;
	}
	
}
