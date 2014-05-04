package com.flock;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flock.Events.DownloadJson;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.os.Build;

public class Friends extends ActionBarActivity {
	String mainURL = "http://54.200.98.199/flock/api/ViewFriends";
	getFriends task =null;
	ListView list;
	ProgressBar progress;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		
		list = (ListView) findViewById(R.id.friends_list);
		progress = (ProgressBar) findViewById(R.id.friends_progress);
		task = new getFriends(this);
	    task.execute(mainURL);
	}
	
	public class getFriends extends AsyncTask<String, Void, Friend[]>{
		Context context;
		HttpResponse response;
		
		
		
		public getFriends(Context context) {
			this.context = context;
		}

		@Override
		protected Friend[] doInBackground(String... params) {
			UserData userData = UserData.getInstance();
			String responseString = "";
			HttpEntity temp = null;
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(params[0]);
			
			//include user id in post
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("id", Integer.toString(  (userData.getUserId()) )  ));
			
			
		
			
			
			//go get  friends list 
			try {
				post.setEntity(new UrlEncodedFormEntity(pairs));
				response = client.execute(post);
				temp = response.getEntity();
				responseString = EntityUtils.toString(temp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("TGE", "Bad stuff occuring in doInBackground in getFriends");
			} 
			
			
			
			JSONArray array = null;
			try {
				array = new JSONObject(responseString).getJSONArray("FriendsList");
			} catch (JSONException e) {
				Log.d("TGE", "Something wrong with JSON");
			}
			
			
			Friend [] stuff = new Friend[array.length()];
			String word = null;
			for(int i = 0; i < stuff.length ; i++){
				
				try {
					word = array.getJSONObject(i).getString("FriendId");
					Log.d("TGE", word);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					stuff[i] = new Friend();
				try {
					
					stuff[i].setID(array.getJSONObject(i).getString("FriendId"));
					stuff[i].setName(array.getJSONObject(i).getString("Firstname") + " "+ array.getJSONObject(i).getString("Lastname") );
				} catch (JSONException e) {

				}
				
				
			}
			
			
			
			
			
			return stuff;
		}

		@Override
		protected void onPostExecute(Friend[] result) {
			// TODO Auto-generated method stub
			
			progress.setVisibility(View.INVISIBLE);
			list.setVisibility(View.VISIBLE);
			
			FriendAdapter adapter = new FriendAdapter(context, result);
			list.setAdapter(adapter);
			
			
			
			super.onPostExecute(result);
		}
		
		
		
	}// end getFriends async 

	

}
