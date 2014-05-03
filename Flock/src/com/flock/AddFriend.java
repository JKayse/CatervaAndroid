package com.flock;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
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

public class AddFriend extends Activity {
	NfcAdapter mNfcAdapter;
	PendingIntent mNfcPendingIntent;
	IntentFilter [] mNdefExchangeFilters;
	UserData mUser;
	
	protected void onNewIntent() {
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);
		
		findViewById(R.id.addByNameButton).setOnClickListener(new onUserName());
		findViewById(R.id.submit_button).setOnClickListener(new searchUsername());
		
		mUser = UserData.getInstance();
		
		
		
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
        	
            Log.d("TGE", "found NFC!");
            
            NdefMessage message = new NdefMessage(
    				new NdefRecord [] {
    				createTextRecord(Integer.toString(mUser.getUserId() ), Locale.US, true),
    				createTextRecord(mUser.getUsername(), Locale.US, true)
    				});
    		
    		
    		
    		mNfcAdapter.setNdefPushMessage(message, this);
    		mNfcPendingIntent=  PendingIntent.getActivity(this, 0,   new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    	    //mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, 
    	       //  mNdefExchangeFilters, null);
            
            
        } else {
            Log.d("TGE","No NFC found");
        }
        
      
       
       IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
       try {
    	    ndefDetected.addDataType("text/plain");
    	} catch (MalformedMimeTypeException e) {
    	}
    	mNdefExchangeFilters = new IntentFilter[] { ndefDetected };
		

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
	

	 public static NdefRecord createNewTextRecord(String text, Locale locale, boolean encodeInUtf8) {
	        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
	 
	        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
	        byte[] textBytes = text.getBytes(utfEncoding);
	 
	        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
	        char status = (char)(utfBit + langBytes.length);
	 
	        byte[] data = new byte[1 + langBytes.length + textBytes.length];
	        data[0] = (byte)status;
	        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
	        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
	 
	        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
	    }
	 
	 public NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
		    byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
		    Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
		    byte[] textBytes = payload.getBytes(utfEncoding);
		    int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		    char status = (char) (utfBit + langBytes.length);
		    byte[] data = new byte[1 + langBytes.length + textBytes.length];
		    data[0] = (byte) status;
		    System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		    System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
		    NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
		    NdefRecord.RTD_TEXT, new byte[0], data);
		    return record;
		}


}