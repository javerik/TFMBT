package de.juanah.trackingformybullentime;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.juanah.trackingformybullentime.helper.UserdataHelper;
import de.juanah.trackingformybullentime.io.DataObject;
import de.juanah.trackingformybullentime.io.FileHelper;
import de.juanah.trackingformybullentime.track.TrackData;
import de.juanah.trackingformybullentime.userdata.UserData;

public class UserdataActivity extends Activity {

	//Class Variables
	
	private volatile UserData _userData; 
	
	private final String UserUUIDFile = "tfmbtUser.tf";
	
	private volatile String MyUserUUID = null;
	
	private volatile String LoginUUID = ""; 
	
	private volatile ArrayAdapter<String> myList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userdata);
		StartUp();
		LoadData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.juanah.de"));
			startActivity(browserIntent);
		}
		if (id == R.id.m_register) {
			Intent registerIntent = new Intent(this, RegisterAytivity.class);
			startActivity(registerIntent);
		}
		if (id == R.id.m_userdata) {
			Intent userdataIntent = new Intent(this,UserdataActivity.class);
			startActivity(userdataIntent);
		}
		if (id == R.id.m_login) {
			Intent userdataIntent = new Intent(this,LoginActivity.class);
			startActivity(userdataIntent);
		}

		return super.onOptionsItemSelected(item);
	}
	
    private void StartUp()
    {
    	//Versuche daten zu lesen
    	ArrayList<DataObject> data = FileHelper.GetDataObjects(this, UserUUIDFile);
    	if (data.size() != 0) {
    		for (DataObject dataObject : data) {
				if (dataObject.getKey().equals("userUUID")) {
					MyUserUUID = dataObject.getValue();
				}else if(dataObject.getKey().equals("loginUUID"))
				{
					LoginUUID = dataObject.getValue();
				}
			}
			return;
		}
    	//Versuche UUID zu schreiben
    	DataObject uuidData = new DataObject("userUUID", UUID.randomUUID().toString(), UserUUIDFile);
    	
    	if (!FileHelper.WriteData(this, uuidData)) {
    		Toast.makeText(getApplicationContext(), "Fatal ERROR", Toast.LENGTH_LONG).show();
		}
    }
	
	
	/**
	 * Lädt die Userdata vom Server
	 */
	private void LoadData()
	{
		if (MyUserUUID == null) {
			Toast.makeText(getApplicationContext(), "Benutzer konnte nicht ausgelesen werden", Toast.LENGTH_LONG).show();
			return;
		}
		
		new Thread(new Runnable() {
			
        	private final Handler handler = new Handler(){
        		
        		public void handleMessage(Message msg)
        		{
        			Toast.makeText(getApplicationContext(), msg.getData().getString("success"), Toast.LENGTH_LONG).show();
        			if (msg.getData().getString("success").equals("Benutzerdaten wurden gelesen")) {
						SetData();
					}
        		}
        	};
			
			@Override
			public void run() {
				Bundle b = new Bundle();
				try {
					_userData = UserdataHelper.GetUserData(MyUserUUID,LoginUUID);
					if (_userData == null) {
						b.putString("success", "Fehler! vielleicht nicht eingeloggt?");
					}
					else
					{
						b.putString("success", "Benutzerdaten wurden gelesen");
						
					}
				} catch (Exception e) {
					b.putString("success", "Interner Fehler");
				}finally
				{
					Message msgObj = handler.obtainMessage();
					msgObj.setData(b);
					handler.sendMessage(msgObj);
				}
				
			}
		}).start();
		
	}
	
	/**
	 * Lädt die Daten in das UI
	 */
	private void SetData()
	{
		TextView username = (TextView)findViewById(R.id.textView_UsernameValue);
		username.setText(_userData.getUsername());
		
		TextView email = (TextView)findViewById(R.id.textView_emailValue);
		email.setText(_userData.getEmail());
		FillListView();
		
	}
	
	private void FillListView()
	{
		
		new Thread(new Runnable() {
			
        	private final Handler handler = new Handler(){
        		
        		public void handleMessage(Message msg)
        		{
        			ListView listview = (ListView) findViewById(R.id.listView1);
        			
        			
        			listview.setAdapter(myList);;
        		}
        	};
			
			@Override
			public void run() {
				ArrayList<TrackData> tempTrackData = _userData.getAllTracks();
				
				String[] values = new String[tempTrackData.size()];
				
				for (int i = 0; i < tempTrackData.size(); i++) {
					
					String tempValue = tempTrackData.get(i).getTime();
					values[i] = tempValue;
					
				}
				
			    
				 myList = new ArrayAdapter<String>(UserdataActivity.this,
					       android.R.layout.simple_list_item_1, android.R.id.text1, values);
			    
			}
		}).start();
		




	    
	    
	    
	}
	
}
