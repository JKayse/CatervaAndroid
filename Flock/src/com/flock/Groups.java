package com.flock;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flock.Friends.getFriends;

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

public class Groups extends ActionBarActivity {
	String mainURL =  "http://54.200.98.199/flock/api/AndroidGroups";
	getGroups task = null;
	ListView list;
	ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
		
		list = (ListView) findViewById(R.id.group_list);
		progress = (ProgressBar) findViewById(R.id.group_progress);
		task = new getGroups(this);
	    task.execute(mainURL);


	}
	
	public class getGroups extends AsyncTask<String, Void, Group[]>{
		Context context;
		HttpResponse response;
		
		public  getGroups(Context context) {
			this.context = context;
		}
			
		@Override
		protected Group[] doInBackground(String... params) {
			UserData userData = UserData.getInstance();
			String responseString = "";
			HttpEntity temp = null;
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(params[0]);
			
			//include user id in post
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("id", Integer.toString(  (userData.getUserId()) )  ));
			
			//hardcoded test 
			//pairs.add(new BasicNameValuePair("id", "3"  ));
		
			
			
			//go get  friends list 
			try {
				post.setEntity(new UrlEncodedFormEntity(pairs));
				response = client.execute(post);
				temp = response.getEntity();
				responseString = EntityUtils.toString(temp);
				//Log.d("TGE", responseString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("TGE", "Bad stuff occuring in doInBackground in getGroup");
			} 
			
			
			
			JSONArray array = null;
			try {
				array = new JSONObject(responseString).getJSONArray("Groups");
			} catch (JSONException e) {
				Log.d("TGE", "Something wrong with JSON");
			}
			
		Log.d("TGE", array.toString());
			
			
			Group [] stuff = new Group[array.length()];
			String word = null;
			
			for(int i = 0; i< stuff.length ; i++){
				
				stuff[i] = new Group();
				try {
					stuff[i].setName(   array.getJSONObject(i).getJSONObject("Group").getString("GroupName")    );
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			
			
			return stuff;
		}
		
		
		
		protected void onPostExecute(Group[] result) {
			// TODO Auto-generated method stub
			
			progress.setVisibility(View.INVISIBLE);
			list.setVisibility(View.VISIBLE);
			
			GroupAdapter adapter = new GroupAdapter(context, result);
			list.setAdapter(adapter);
			
			
			
			super.onPostExecute(result);
		}
		
		
		
		
		
		
		
	}

}
