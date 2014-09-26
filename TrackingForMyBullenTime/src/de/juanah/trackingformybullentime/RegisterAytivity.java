package de.juanah.trackingformybullentime;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import de.juanah.trackingformybullentime.helper.RegisterHelper;
import de.juanah.trackingformybullentime.helper.data.RegisterData;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import de.juanah.trackingformybullentime.io.DataObject;
import de.juanah.trackingformybullentime.io.FileHelper;


public class RegisterAytivity extends Activity {

	//Class Variables
	private volatile String MyUUID;
	private final String UserUUIDFile = "tfmbtUser.tf";
	private volatile String _username;
	private volatile String _password;
	private volatile String _email;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		StartUp();
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
        return super.onOptionsItemSelected(item);
    }
	
	
	private void StartUp()
	{
    	//Versuche daten zu lesen
    	
    	ArrayList<DataObject> data = FileHelper.GetDataObjects(this, UserUUIDFile);
    	
    	if (data.size() != 0) {
    		MyUUID = data.get(0).getValue();
			return;
		}
	}
	
	public void onSubmit(View view)
	{
		DoRegister();
	}
	
	private void DoRegister()
	{
		_username = ((EditText)findViewById(R.id.editText_loginUsername)).getText().toString();
		_password = ((EditText)findViewById(R.id.editText_loginPassword)).getText().toString();
		_email = ((EditText)findViewById(R.id.editText_email)).getText().toString();
		String passwordRepeat = ((EditText)findViewById(R.id.editText_repeatPassword)).getText().toString();
		
		if (!_password.equals(passwordRepeat)) {
			Toast.makeText(getApplicationContext(), "Passwort ist nicht gleich!", Toast.LENGTH_LONG).show();
			return;
		}
		
		new Thread(new Runnable() {
			
			Handler handler = new Handler(){
				public void handleMessage(Message msg)
        		{
        			
        			Toast.makeText(getApplicationContext(), msg.getData().getString("success"), Toast.LENGTH_SHORT).show();
        			
        		}
			};
			@Override
			public void run() {
				
				Bundle b = new Bundle();
				
				if (RegisterHelper.Register(new RegisterData(MyUUID, _username, _password,_email))) {
					b.putString("success", "Erfolgreich registriert");
				}else
				{
					b.putString("success", "Leider hat es nicht geklappt");
				}
				
				Message msgObj = handler.obtainMessage();
				msgObj.setData(b);
				handler.sendMessage(msgObj);
			}
		}).start();
	}
	

}
