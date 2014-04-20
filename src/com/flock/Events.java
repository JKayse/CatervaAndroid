package com.flock;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Events extends ActionBarActivity {

	String mainURL = "http://54.200.98.199/flock/api/AndroidEvents";
	DownloadJson task = new DownloadJson(this);
	ListView list;
	ProgressBar progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		task.execute(mainURL);
		list = (ListView) findViewById(R.id.list);
		progress = (ProgressBar) findViewById(R.id.progress);
		
	}

	//Sub class for the asynchronous task.
		public class DownloadJson extends AsyncTask<String, Void, Event[]>{
			
			//The list of variables used for this class.
			Event[] events;
			EventAdapter adapter;
			HttpURLConnection connection;
			URL requestURL;
			HttpURLConnection secondConnection;
			URL secondRequestURL;
			InputStream stream;
			Scanner scanner;
			String jsonresults;
			JSONObject event;
			Context context;
			
			Bitmap image;
			String host;
			String name;
			String description;
			Boolean isCanceled;
			String start;
			String end;

			
			//Constructor for the subclass to take in the proper context.
			public DownloadJson(Context context){
				this.context = context;	
			}
			
			protected Event[] doInBackground(String... urls){
				
				//Opens up a connection.
				try {
					requestURL = new URL(urls[0]);
				} catch (MalformedURLException e) {
					Log.e("JLK", "URL is bad");
					e.printStackTrace();
				}
				try {
					connection = (HttpURLConnection) requestURL.openConnection();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				connection.setReadTimeout(10000);
				connection.setConnectTimeout(15000);
				try {
					connection.setRequestMethod("POST");
				} catch (ProtocolException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				connection.setRequestProperty("Accept", "application/json");
				
				//will eventually change the userid to the proper one.
				String urlParameters = "userId=1";
				try {
					connection.getOutputStream().write(urlParameters.getBytes());
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					connection.getOutputStream().flush();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					connection.connect();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				int statusCode;
				try {
					statusCode = connection.getResponseCode();
					
					if(statusCode == HttpURLConnection.HTTP_UNAUTHORIZED){
						Log.d("JLK", "No authorization");
					}
					else if(statusCode != HttpURLConnection.HTTP_OK){
						Log.d("JLK", "Status code: " + statusCode);
					}
					else{
						//Gets all the data from the connection and saves it in a string.
						stream = connection.getInputStream();
						scanner = new Scanner(stream);
						
						jsonresults = scanner.useDelimiter("\\A").next();
						scanner.close();
						
						JSONObject json = new JSONObject(jsonresults);
						JSONArray values = json.getJSONArray("Events");
						
						events = new Event[values.length()];
						
						//Loops through every object to get the proper values and sets them.
						for(int i = 0; i < values.length(); i++){
							event = values.getJSONObject(i);
							name = event.getString("EventName");
							description = event.getString("EventDescription");
							end = event.getString("EndTime");
							start = event.getString("StartTime");
							String hostId = event.getString("OwnerId");
							String canceled = event.getString("Cancel");
							if(canceled.equals("1")){
								isCanceled = true;
							}
							else{
								isCanceled = false;
							}
							
							//Opens up a connection.
							try {
								secondRequestURL = new URL("http://54.200.98.199/flock/api/UserInfo/" + hostId );
							} catch (MalformedURLException e) {
								Log.e("JLK", "URL is bad");
								e.printStackTrace();
							}
							try {
								
								secondConnection = (HttpURLConnection) secondRequestURL.openConnection();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							secondConnection.setReadTimeout(10000);
							secondConnection.setConnectTimeout(15000);
							
							int secondStatus;
							secondStatus = secondConnection.getResponseCode();
							
							if(secondStatus == HttpURLConnection.HTTP_UNAUTHORIZED){
								Log.d("JLK", "No authorization");
							}
							else if(secondStatus != HttpURLConnection.HTTP_OK){
								Log.d("JLK", "Status code: " + secondStatus);
							}
							else{
								//Gets all the data from the connection and saves it in a string.
								stream = secondConnection.getInputStream();
								scanner = new Scanner(stream);
								
								jsonresults = scanner.useDelimiter("\\A").next();
								scanner.close();
								
								JSONObject json2 = new JSONObject(jsonresults);
								JSONArray values2 = json2.getJSONArray("User");
								JSONObject person = values2.getJSONObject(0);
								host = person.getString("Firstname");
								host += " " + person.getString("Lastname");
								
								
							}
							

							events[i] = new Event(name, description, host, start, end, isCanceled);
						}
					}
					
				} catch (IOException e) {
					Log.e("JLK", "Problem");
					e.printStackTrace();
				} catch (JSONException e) {
					Log.e("JLK", "Problem2");
					e.printStackTrace();
				}
				
				return events;
			}
			
			
			protected void onPostExecute(Event[] result){
				//Sets the adapter to the list view in the activity_main file.
				//Also makes the progress bar disappear.
				progress.setVisibility(View.GONE);
				adapter = new EventAdapter(context , events);
				list.setAdapter(adapter);
			}

		}
	

}
