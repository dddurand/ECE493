package misc;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bluetoothpoker.R;

/**
 * Custom adapter for adding items to a list with two labels on the same row.
 * @author kennethrodas
 */

public class CustomAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private ArrayList<StatsRowObject> objects;
	
	//Internal class
	private class ViewHolder {
		TextView textView1;
		TextView textView2;
	}
	
	//Constructor
	public CustomAdapter(Context context, ArrayList<StatsRowObject> objects){
		inflater = LayoutInflater.from(context);
		this.objects=objects;
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public StatsRowObject getItem(int pos) {
		return objects.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.stats_list_element, null);
			holder.textView1 = (TextView) convertView.findViewById(R.id.statsEntryText);
			holder.textView2 = (TextView) convertView.findViewById(R.id.statsDataText);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textView1.setText(objects.get(pos).getProp1());
		holder.textView2.setText(objects.get(pos).getProp2());
		return convertView;

	}

}
