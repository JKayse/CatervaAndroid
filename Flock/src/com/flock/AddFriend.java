package com.flock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.os.Build;

public class AddFriend extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);
		
		findViewById(R.id.addByNameButton).setOnClickListener(new onUserName());
		findViewById(R.id.submit_button).setOnClickListener(new searchUsername());
		

	}
	
	void nfcShare(){
		
	}
	
	void searchForName(){
		
	}
	class searchUsername implements OnClickListener{

		@Override
		public void onClick(View v) {
			EditText username = (EditText) findViewById(R.id.submit_username);
			
			String text = null;
			text = username.getText().toString();
			
			if(!text.isEmpty()){
				addFriendByName add = new addFriendByName();
				add.execute(text);
			} else {
				username.setError("Please enter a username");
			}
			
		}
		
	}
	class onUserName implements OnClickListener{

		@Override
		public void onClick(View v) {
			RelativeLayout main = (RelativeLayout) findViewById(R.id.main_layout);
			RelativeLayout byId = (RelativeLayout) findViewById(R.id.byName);
			
			
			main.setVisibility(View.GONE);
			byId.setVisibility(View.VISIBLE);
			
		}
		
		
	}// end of OnUserName click
	
	class addFriendByName extends AsyncTask<String, Void, Void>{
		String url = "http://54.200.98.199/flock/api/SearchFriend";
		String addUrl = "http://54.200.98.199/flock/api/AndroidAddFriendRequest";

		@Override
		protected Void doInBackground(String... params) {
			
			String output = null;
			JSONObject array = null; 
			String user = null;
			UserData userData = UserData.getInstance();
			
			//set up client
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			HttpPost addPost= new HttpPost(addUrl);
			HttpResponse response = null;
			HttpResponse addResponse = null;
			

			try{
				//add data
				List<NameValuePair> data = new ArrayList<NameValuePair>();
				data.add(new BasicNameValuePair("username", params[0]));
				post.setEntity(new UrlEncodedFormEntity(data));
				
				//execute POST request
				response = client.execute(post);
				
				//parse data to string
				output = EntityUtils.toString(response.getEntity());
				
				//put data in Json
				array = new JSONObject(output);
				
				JSONArray temp =((JSONArray)array.get("Friend"));
				if(temp.toString().contentEquals("[]")){
					user = null;
				} else {
					user = ((JSONObject)temp.get(0)).getString("userId");
				}
				//user = temp.toString();
				
				
			} catch (Exception e){
				Log.d("TGE", "someting broke in addFriendByName while checking username");
			}
			
			
			 
			if(user != null){
				//success! add friend 
				Log.d("TGEllingoton", user.toString() );
				
				try{
					//add data
					List<NameValuePair> addData = new ArrayList<NameValuePair>();
					addData.add(new BasicNameValuePair("id", Integer.toString(userData.getUserId()) ));
					addData.add(new BasicNameValuePair("friendId", user.toString() ));
					
					addPost.setEntity( new UrlEncodedFormEntity(addData));
					
					
					//execute post
					addResponse = client.execute(addPost);
					
					String responseString = EntityUtils.toString( addResponse.getEntity() );
					if( responseString.isEmpty() ){
						Log.d("TGE", "Friend added");
					} else {
						Log.d("TGE", "error in adding friend");
					}
					
				} catch (Exception e){
					Log.d("TGE", "something broke in addFriendByName while adding friend");
				}
				
				
				
				
			} else {
				//TODO toast on no user
				Log.d("TGEllingoton", "no match found" );
				
			}
			
			
			
			return null;
		}
		
		
		
	}
	
	
	


}
