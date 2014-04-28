package com.flock;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Groups extends ActionBarActivity {
	String mainURL =  "http://54.200.98.199/flock/api/AndroidGroups";
	getGroups task = null;
	ExpandableListView list;
	ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
		
		list = (ExpandableListView) findViewById(R.id.group_list);
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
				Friend[] friends = null;
				stuff[i] = new Group();
				try {
					stuff[i].setName(array.getJSONObject(i).getJSONObject("Group").getString("GroupName"));
					JSONArray users = array.getJSONObject(i).getJSONArray("Users");
					friends = new Friend[users.length()];
					for(int j=0; j < users.length(); j++){
						friends[j] = new Friend();
						String id = users.getJSONObject(j).getString("UserId");
						String friend = null;
						URL secondRequestURL = null;
						//Opens up a connection.
						try {
							
							secondRequestURL = new URL("http://54.200.98.199/flock/api/UserInfo/" + id );
						} catch (MalformedURLException e) {
							Log.e("JLK", "URL is bad");
							e.printStackTrace();
						}
						HttpURLConnection secondConnection = null;
						try {
							
							secondConnection = (HttpURLConnection) secondRequestURL.openConnection();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						secondConnection.setReadTimeout(10000);
						secondConnection.setConnectTimeout(15000);
						
						int secondStatus = 0;
						try {
							secondStatus = secondConnection.getResponseCode();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(secondStatus == HttpURLConnection.HTTP_UNAUTHORIZED){
							Log.d("JLK", "No authorization");
						}
						else if(secondStatus != HttpURLConnection.HTTP_OK){
							Log.d("JLK", "Status code: " + secondStatus);
						}
						else{
							//Gets all the data from the connection and saves it in a string.
							InputStream stream = null;
							try {
								stream = secondConnection.getInputStream();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Scanner scanner = new Scanner(stream);
							
							String jsonresults = scanner.useDelimiter("\\A").next();
							scanner.close();
							
							JSONObject json2 = new JSONObject(jsonresults);
							JSONArray values2 = json2.getJSONArray("User");
							JSONObject person = values2.getJSONObject(0);
							friend = person.getString("Firstname");
							friend += " " + person.getString("Lastname");
							
							
						}
						friends[j].setName(friend);
						friends[j].setID(id);	
						
					}
					stuff[i].setFriends(friends);
					
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
