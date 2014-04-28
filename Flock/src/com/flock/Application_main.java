package com.flock;

import com.flock.MainActivity.loginRequest;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class Application_main extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_application_main);
	}
	
	public void toEvents(View view){
		Intent moveToEvent = new Intent(this,Events.class  );
		startActivity(moveToEvent);
    	
    }
	
    public void toFriends(View view){	
    	Intent moveToFriends = new Intent(this,  Friends.class);
    	startActivity(moveToFriends);
    	
    }

    public void toGroups(View view){
    	
    	Intent moveToGroups = new Intent(this, Groups.class);
    	startActivity(moveToGroups);
    	
    	
    	
    	
    	
    	
    	
    }

}
