package misc;

/**
 * An interface that is used by the AmountDialog to call an update to the view.
 * 
 * @SRS 3.2.1.4
 * @author dddurand
 *
 */
public interface BalanceUpdatable {
	
	/**
	 * Method called when AmountDialog update's a users balance.
	 */
	public void updateBalance();
}
