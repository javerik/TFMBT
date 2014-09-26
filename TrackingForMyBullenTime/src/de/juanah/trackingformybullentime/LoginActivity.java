package de.juanah.trackingformybullentime;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import de.juanah.trackingformybullentime.helper.LoginHelper;
import de.juanah.trackingformybullentime.helper.data.LoginResult;
import de.juanah.trackingformybullentime.io.DataObject;
import de.juanah.trackingformybullentime.io.FileHelper;

public class LoginActivity extends Activity {

	private final String UserUUIDFile = "tfmbtUser.tf";
	
	private volatile String _username = "";
	private volatile String _password = "";
	private volatile LoginResult _loginResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
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
	
	public void onSubmitLogin(View view)
	{
		_username = ((TextView)findViewById(R.id.editText_loginUsername)).getText().toString();
		_password = ((TextView)findViewById(R.id.editText_loginPassword)).getText().toString();
		
		performLogin();
	}
	
	private void performLogin()
	{
		
		new Thread(new Runnable() {
			
        	private final Handler handler = new Handler(){
        		
        		public void handleMessage(Message msg)
        		{
        			Toast.makeText(getApplicationContext(), msg.getData().getString("success"), Toast.LENGTH_LONG).show();
        			if (_loginResult.isSuccess()) {
						WriteLoginUUID(_loginResult);
						Intent userdataIntent = new Intent(LoginActivity.this,UserdataActivity.class);
						startActivity(userdataIntent);
					}
        		}
        	};
			
			@Override
			public void run() {
				Bundle b = new Bundle();
				try {
					_loginResult = LoginHelper.GetLoginResult(_username, _password);
					if (_loginResult == null) {
						b.putString("success", "interner Fehler");
					}
					else
					{
						b.putString("success", _loginResult.getLoginMessage());
						
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
	
	private void WriteLoginUUID(LoginResult result)
	{
    	//Versuche UUID zu schreiben
    	DataObject uuidData = new DataObject("loginUUID", result.getLoginUUID(), UserUUIDFile);
    	
    	if (!FileHelper.WriteData(this, uuidData)) {
    		Toast.makeText(getApplicationContext(), "Fatal ERROR", Toast.LENGTH_LONG).show();
		}
	}
	
}
