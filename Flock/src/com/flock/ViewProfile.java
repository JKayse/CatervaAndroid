package com.flock;




import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewProfile extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_profile);
		
		Bundle data = getIntent().getExtras();
		String user = data.getString("userId");
		
		getAndSetData dataSet = new getAndSetData();
		dataSet.execute(user);

	}
	
	
	class getAndSetData extends AsyncTask<String, Void, Void>{
		String url = "http://54.200.98.199/flock/api/UserInfo/";
		String nFirst= "Error";
		String nLast= "Error";
		String nUser = "Error";
		String nEmail = "Error";
		String nDesc = "Error";
		String nCompleteName = "Error";
		String nImageName = "Error";
		@Override
		protected Void doInBackground(String... params) {
			
			
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url + params[0]);
			HttpResponse response = null;
			String output = null;

			
			JSONObject json;
			
			try{
				response = client.execute(request);
				output = EntityUtils.toString(response.getEntity());
				
				json = new JSONObject(output);
				JSONObject tempHolder = (JSONObject) json.getJSONArray("User").get(0);
				nFirst = tempHolder.getString("Firstname");
				nLast = tempHolder.getString("Lastname");
				nUser = tempHolder.getString("Username");
				nEmail = tempHolder.getString("Email");
				nDesc = tempHolder.getString("Description");
				nImageName = tempHolder.getString("PictureName");
				nCompleteName = nFirst + " " + nLast + "'s Profile";
				if(nDesc.equals("null")){
					nDesc = "None";
				}
				
			}catch(Exception e){
				
			}
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			TextView complete = (TextView)findViewById(R.id.completeName);
			TextView user =(TextView)findViewById(R.id.user_name);
			TextView email = (TextView)findViewById(R.id.email);
			TextView desc = (TextView)findViewById(R.id.description);
			View container = (View)findViewById(R.id.mainInfo);
			ProgressBar progress = (ProgressBar)findViewById(R.id.progress);
			
			

			complete.setText(nCompleteName);
			user.setText("Username: " + nUser);
			email.setText("Email: " + nEmail);
			desc.setText(nDesc);
			container.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			
			
			
			
			super.onPostExecute(result);
		}
		
	}

	
}
