package com.flock;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdapter extends ArrayAdapter<Event> {

		public static final int EVENT_REQUEST = 1753;
		public static Event[] events;
		private final Context context;
		
		//Class constructor for the adapter.
		public EventAdapter(Context context, Event[] events) {
			super(context, R.layout.event_item, events);
		    this.events = events;
		    this.context = context;
		}
	
		
		public View getView(final int position, View convertView, ViewGroup parent) {
			 View view = convertView;
		
			 //Inflates the view if it hadn't been yet.
			 if (view == null) {
			      LayoutInflater inflater = LayoutInflater.from(context);
			      view = inflater.inflate(R.layout.event_item, null);  
			 }
			
			 TextView name = (TextView)view.findViewById(R.id.eventName);
			 TextView start = (TextView)view.findViewById(R.id.eventStart);
			 TextView host = (TextView)view.findViewById(R.id.eventHost);
			 ImageView canceled = (ImageView) view.findViewById(R.id.canceled);
			 view.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, EventInfo.class);
						ArrayList<Integer> eventNumber = new ArrayList(1);
						eventNumber.add(position);	
						intent.putIntegerArrayListExtra(EventInfo.EVENT_KEY, eventNumber);
						context.startActivity(intent);
					}
				});
	 
			 
			 name.setText(events[position].getEvent());
			 start.setText(events[position].getStartDate());
			 host.setText("Hosted By: " + events[position].getHost());
			 if(!events[position].getCanceled())
			 {
				 canceled.setVisibility(View.GONE);
			 }
			 
			 //Returns the view.
			 return view;
		  }

}
