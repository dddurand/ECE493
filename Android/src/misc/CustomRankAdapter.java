package misc;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bluetoothpoker.R;

public class CustomRankAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private ArrayList<RankStatsRowObject> objects;
	
	//Internal class
	private class ViewHolder {
		TextView textView1;
		TextView textView2;
		TextView textView3;
	}
	
	//Constructor
	public CustomRankAdapter(Context context, ArrayList<RankStatsRowObject> objects){
		inflater = LayoutInflater.from(context);
		this.objects=objects;
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public RankStatsRowObject getItem(int pos) {
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
			convertView = inflater.inflate(R.layout.ranking_list_element, null);
			holder.textView1 = (TextView) convertView.findViewById(R.id.statsRankPosition);
			holder.textView2 = (TextView) convertView.findViewById(R.id.statsRankUser);
			holder.textView3 = (TextView) convertView.findViewById(R.id.statsRankScore);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textView1.setText(objects.get(pos).getPos());
		holder.textView2.setText(objects.get(pos).getUser());
		holder.textView3.setText(objects.get(pos).getScore());
		
		return convertView;

	}


}
