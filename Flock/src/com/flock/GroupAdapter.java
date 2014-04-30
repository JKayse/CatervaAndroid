package com.flock;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class GroupAdapter  extends BaseExpandableListAdapter {

	LayoutInflater inflater;
	Context context;
	Group[] groups;

	GroupAdapter(Context context, Group[] data){
		super();
		this.context = context;
		groups = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public Friend getChild(int groupPosition, int childPosition) {
        return groups[groupPosition].getFriend(childPosition);
    }
	
	public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        final Friend currentFriend = getChild(groupPosition, childPosition);
         
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_friend_item, null);
        }
         
        TextView name = (TextView) convertView.findViewById(R.id.friendName);
        name.setText(currentFriend.getName());
        Log.d("JLK", currentFriend.getName());
        convertView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String currentId = currentFriend.getID();
				Toast toast = Toast.makeText(context, currentFriend.getID(), 5000);
				toast.show();
				Intent toProfile = new Intent(v.getContext(), ViewProfile.class);
				Bundle data = new Bundle();
				data.putString("userId", currentId);
				toProfile.putExtras(data);
				v.getContext().startActivity(toProfile);
				
				
			}
		});
        return convertView;
	}
	
	public View getGroupView(int groupPosition, boolean isExpanded,
	            View convertView, ViewGroup parent) {
	        String groupName = getGroup(groupPosition);
	        if (convertView == null) {
	            LayoutInflater secondInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = secondInflater.inflate(R.layout.group_item,null);
	        }
	        TextView item = (TextView) convertView.findViewById(R.id.groupname);
	        item.setText(groupName);
	        Log.d("JLK", groupName);
	        return convertView;
	    }

	@Override
	public int getGroupCount() {
		return groups.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups[groupPosition].getFriends().length;
	}

	@Override
	public String getGroup(int groupPosition) {
		return groups[groupPosition].getName();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	
}
