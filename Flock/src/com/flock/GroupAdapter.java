package com.flock;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GroupAdapter  extends ArrayAdapter<Group>{

	LayoutInflater inflater;
	
	GroupAdapter(Context context, Group[] data){
		super(context, R.layout.group_item, data);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		Log.d("TGE", "Group GetView");
		if(convertView == null){
			view = inflater.inflate(R.layout.group_item, parent, false);
		} else {
			view = convertView;
		}
		
		TextView text = (TextView) view.findViewById(R.id.groupname);
		text.setText(getItem(position).getName());
		
		
		return view;
	}
	
	
	
}
