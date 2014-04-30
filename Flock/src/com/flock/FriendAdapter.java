package com.flock;

import com.flock.Friend;
import com.flock.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class FriendAdapter extends ArrayAdapter<Friend> {
	LayoutInflater inflater;
	
	FriendAdapter(Context context, Friend[] data){
		super(context, R.layout.friend_item, data);
		inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		
		
		if(convertView == null){
			view = inflater.inflate(R.layout.friend_item, parent, false);
		} else {
			view = convertView;
		}
		TextView txt = (TextView) view.findViewById(R.id.friendname);
		
		txt.setText(getItem(position).getName().toString());
		view.setOnClickListener(new toProfile(position));
		
		return view;
	}
	
	class toProfile implements OnClickListener{
		int position;
		
		toProfile(int pos){
			position = pos;
		}

		@Override
		public void onClick(View v) {
			Context here = getContext();
			Intent toProfile = new Intent(here, ViewProfile.class);
			Bundle data = new Bundle();
			data.putString("userId", getItem(position).mID.toString() );
			toProfile.putExtras(data);
			here.startActivity(toProfile);
			
		}
		
	}
	
	
	
	
	
	

}
