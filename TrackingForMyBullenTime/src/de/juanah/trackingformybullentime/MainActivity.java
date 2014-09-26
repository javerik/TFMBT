package de.juanah.trackingformybullentime;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import de.juanah.trackingformybullentime.gps.GpsHelper;
import de.juanah.trackingformybullentime.track.GetSimpleTracksResult;
import de.juanah.trackingformybullentime.track.OnTaskCompleted;
import de.juanah.trackingformybullentime.track.SimpleTrack;
import de.juanah.trackingformybullentime.track.SimpleTrackResult;


public class MainActivity extends ActionBarActivity implements OnTaskCompleted {

	/**
	 * Class Variablen
	 */
	
	private volatile GetSimpleTracksResult _result;

	
	
	private ProgressBar bar; 
	
	//Erstellt den Network helper und verbindet sich dadurch mit dem Server
	private de.juanah.trackingformybullentime.network.BaseHelper _networkHelper = new de.juanah.trackingformybullentime.network.BaseHelper();
	
	// Aktuelle Position vom nutzer
	private volatile double lat =0;
	private volatile double lon= 0;
	
	private volatile String test;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        
        bar  = (ProgressBar)findViewById(R.id.progressBar_status);
        
        bar.setVisibility(View.INVISIBLE);
        
    }

    /**
     * Führt einen SimpleTrack aus
     * @param view
     */
    public void onTrack(View view)
    {
    
        
    }
    
    public void onAroundMe(View view)
    {
    	Intent intent = new Intent(this,TrackViewActivity.class);
    	
    	startActivity(intent);
    	
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onTaskCompleted(SimpleTrackResult result) {
		// TODO Auto-generated method stub
		
	}


}




