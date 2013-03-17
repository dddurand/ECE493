package misc;

/**
 * An interface that is used by the AmountDialog to call an update to the view.
 * 
 * @author dddurand
 *
 */
public interface BalanceUpdatable {
	
	/**
	 * Method called when AmountDialog update's a users balance.
	 */
	public void updateBalance();
}
