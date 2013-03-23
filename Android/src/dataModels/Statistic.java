package dataModels;

import java.util.ArrayList;

import android.content.Context;

/**
 * An abstract class for the statistic web service data models.
 * 
 * @author dddurand
 *
 */
abstract class Statistic<T> {

	protected Context context;
	
	public Statistic(Context context)
	{
		this.context = context;
	}
	
	abstract ArrayList<T> getAllStatistics();
	
	/**
	 * Retrieve the display name based on the provided string identifier
	 * 
	 * @param resourceIdentifier The statistic identifier
	 * @return
	 */
	protected String getStringResourceByName(String identifier) {
	      String packageName = this.context.getPackageName();
	      int resId = this.context.getResources().getIdentifier(identifier, "string", packageName);
	      return this.context.getString(resId);
	    }


}
