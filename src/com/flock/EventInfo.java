package com.flock;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EventInfo extends ActionBarActivity{
	
	public static final String EVENT_KEY = "EventKey";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);
		Intent intent = getIntent();
		ArrayList<Integer> eventItem = intent.getIntegerArrayListExtra(EVENT_KEY);
		Integer current = eventItem.get(0);
		
		TextView event = (TextView) findViewById(R.id.eventName);
		TextView host = (TextView) findViewById(R.id.host);
		TextView startDate = (TextView) findViewById(R.id.startDate);
		TextView startTime = (TextView) findViewById(R.id.startTime);
		TextView endDate = (TextView) findViewById(R.id.endDate);
		TextView endTime = (TextView) findViewById(R.id.endTime);
		TextView description = (TextView) findViewById(R.id.description);
		ImageView image = (ImageView) findViewById(R.id.canceled);
		
		event.setText(EventAdapter.events[current].getEvent());
		host.setText("Hosted By: " + EventAdapter.events[current].getHost());
		startDate.setText("Start: " + EventAdapter.events[current].getStartDate());
		startTime.setText(EventAdapter.events[current].getStartTime());
		endDate.setText("End: " + EventAdapter.events[current].getEndDate());
		endTime.setText(EventAdapter.events[current].getEndTime());
		description.setText(EventAdapter.events[current].getDescription());
		if(!EventAdapter.events[current].getCanceled()){
			image.setVisibility(View.GONE);
		}
		
	}


}
