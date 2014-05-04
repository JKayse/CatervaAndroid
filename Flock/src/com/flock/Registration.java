package com.flock;

import java.io.UnsupportedEncodingException;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.flock.MainActivity.loginRequest;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class Registration extends ActionBarActivity {
	
	 EditText usernameField;
  	 EditText fnameField;
  	 EditText lanemField;
  	 EditText emailField;
  	 EditText passwordField;
  	 EditText verifypasswordField;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

	}
	
	public void registerAttempt(View view){
   	 usernameField = (EditText)findViewById(R.id.new_username);
   	 fnameField = (EditText)findViewById(R.id.new_first_name);
   	 lanemField = (EditText)findViewById(R.id.new_last_name);
   	 emailField = (EditText)findViewById(R.id.new_email);
   	 passwordField = (EditText)findViewById(R.id.new_password);
   	 verifypasswordField = (EditText)findViewById(R.id.new_password_confirm);
   	 
   	 
   	  String newUserName=usernameField.getText().toString();
   	  String newFName=fnameField.getText().toString();
   	  String newLName=lanemField.getText().toString();
   	  String newEMail=emailField.getText().toString();
   	  String newPass=passwordField.getText().toString();
   	  String newPass2=verifypasswordField.getText().toString();
   	 
   	  //TODO valadation goes here
   	if(newUserName.length() == 0){
   		 usernameField.setError("Enter a username.");
		 return;
	 }
   	else if(newFName.length() == 0){
   		 fnameField.setError("Enter your first name.");
		 return;
	 }
   	else if(newLName.length() == 0){
   		 lanemField.setError("Enter your last name.");
		 return;
	 }
	 else if(newEMail.length() == 0 || !newEMail.contains("@")){
		 emailField.setError("The email must be valid.");
		 return;
	 }
	 else if(newPass.length() < 8){
		 passwordField.setError("The password must be at least 8 characters long.");
		 return;
	 }
	 else if(newPass2.length() < 8){
		 verifypasswordField.setError("The password must be at least 8 characters long.");
		 return;
	 }
	 else if(!newPass.equals(newPass2)){
		 verifypasswordField.setError("The two passwords must match.");
		 return;
	 }
   	 
   	
   	  new registerRequest(getApplicationContext()).execute(newUserName,  newFName, newLName, newEMail, newPass );
   	
   }
	
	
	
	class registerRequest extends AsyncTask<String, Void, String>{
    	Context context;
        private registerRequest(Context context) {
            this.context = context.getApplicationContext();
        }
    	
		@Override
		protected String doInBackground(String... params) {
			
			
			
			HttpResponse response = null;
			HttpClient client= new DefaultHttpClient();
			HttpPost post = new HttpPost("http://54.200.98.199/flock/api/Users");
			
		    
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("username", params[0]));
			pairs.add(new BasicNameValuePair("firstname", params[1]));
			pairs.add(new BasicNameValuePair("lastname", params[2]));
			pairs.add(new BasicNameValuePair("email", params[3]));
			pairs.add(new BasicNameValuePair("password", params[4]));
			
			try {
				post.setEntity(new UrlEncodedFormEntity(pairs));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				response = client.execute(post);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String responseString = "Sigh, nope";
			HttpEntity temp = response.getEntity();
			try {
				responseString = EntityUtils.toString(temp);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			return responseString;
	
		}
		
		
		@Override
		protected void onPostExecute(String response) {
			CharSequence text = "";
			Log.d("JLK", response);
			if(response.equals("error_username")){
				usernameField.setError("This username already exists.");		
			}else if(response.equals("error_email")){
				emailField.setError("This email already exists.");
			}else{
				 text = "Success! Login to continue";
				 int duration = Toast.LENGTH_LONG;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				
			}
			
			
			return;
			
			
	     }  //end of onPostExecute

    	
    } // end of the async

	
}
