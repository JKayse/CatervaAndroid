package com.flock;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.util.Log;


public class Event {

	String mEventName;
	String mEventDescription;
	String mHost;
	String mStartDate;
	String mStartTime;
	String mEndDate;
	String mEndTime;
	Boolean mIsCanceled;
	//Add the image later.
	Bitmap mHostImage;
	
	//The class constructor
	public Event(String event, String description, String host, String start, String end, Boolean canceled){
		mEventName = event;
		mEventDescription = description;
		mHost = host;
		//Temporary until i can get the proper units.
		mStartDate = start;
		mEndDate = end;
		mIsCanceled = canceled;
		

			try {
				Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
				mStartDate = new SimpleDateFormat("MM-dd-yyyy").format(startDate);
				mStartTime = new SimpleDateFormat("KK:mm a").format(startDate);
				Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
				mEndDate = new SimpleDateFormat("MM-dd-yyyy").format(endDate);
				mEndTime = new SimpleDateFormat("KK:mm a").format(endDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.d("JLK", mStartDate);
			Log.d("JLK", mStartTime);
			Log.d("JLK", mEndDate);
			Log.d("JLK", mEndTime);
		
		
		
	}

	//Returns the name.
	public String getEvent(){
		return mEventName;
	}
	
	public String getDescription(){
		return mEventDescription;
	}
	
	public String getHost(){
		return mHost;
	}
	
	public String getStartDate(){
		return mStartDate;
	}
	public String getEndDate(){
		return mEndDate;
	}
	public String getStartTime(){
		return mStartTime;
	}
	public String getEndTime(){
		return mEndTime;
	}

	public Boolean getCanceled(){
		return mIsCanceled;
	}
}
