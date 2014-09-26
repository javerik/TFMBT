package de.juanah.trackingformybullentime;

import java.util.ArrayList;
import java.util.UUID;

import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.juanah.trackingformybullentime.helper.TimeHelper;
import de.juanah.trackingformybullentime.io.DataObject;
import de.juanah.trackingformybullentime.io.FileHelper;
import de.juanah.trackingformybullentime.track.GetSimpleTracksResult;
import de.juanah.trackingformybullentime.track.SimpleTrack;
import de.juanah.trackingformybullentime.track.SimpleTrackResult;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TrackViewActivity extends FragmentActivity {

	private volatile Location myLocation;
	
	private GoogleMap map;
	
	private ArrayList<Marker> _marker = new ArrayList<Marker>();
	
	private volatile GetSimpleTracksResult _result;
	
	private final String UserUUIDFile = "tfmbtUser.tf";
	
	private volatile String MyUserUUID = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.trackview);
		
		StartUp();
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		//map.setMyLocationEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		//map.animateCamera(CameraUpdateFactory.zoomBy(13));
		
		centerMapOnMyLocation();
		GetCoords();
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
        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.juanah.de"));
        	startActivity(browserIntent);
        }
        if (id == R.id.m_register) {
        	Intent registerIntent = new Intent(this,RegisterAytivity.class);
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
     * Wird ausgeführt wenn der Refresh Button gedrückt worden ist
     * @param view
     */
	public void onRefresh(View view)
	{
		SetMyPosition();
		if (myLocation == null) {
			return;
		}
		GetCoords();
	}
	
	
	
	/**
	 * Wird gefeuert, wenn der Track Button gedrückt worden ist
	 * @param view
	 */
	public void onTrackClicked(View view)
	{
		//Ermittel die Position
		
		 myLocation = map.getMyLocation();
		
		new Thread(new Runnable() {
			
        	private final Handler handler = new Handler(){
        		
        		public void handleMessage(Message msg)
        		{
        			
        			Toast.makeText(getApplicationContext(), msg.getData().getString("success"), Toast.LENGTH_LONG).show();
        			GetCoords();
        		}
        		
        	};
			@Override
			public void run() {
				Bundle b = new Bundle();
				try {
					SimpleTrackResult result = SimpleTrack.SendTrack(MyUserUUID,TimeHelper.GetTime(),myLocation.getLatitude(), myLocation.getLongitude());
					if (result == null || !result.isSuccess()) {
						b.putString("success", "Track konnte nicht übertragen werden!");
					}else
					{
						b.putString("success", "Track wurde erfolgreich übertragen");
						
					}
				} catch (Exception e) {
					b.putString("success", "Track konnte nicht übertragen werden!");
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
	 * Soll die Map Zentrieren tut es aber nicht wirklich
	 */
	private void centerMapOnMyLocation() {

	    map.setMyLocationEnabled(true);

	    Location location = map.getMyLocation();

	    LatLng myLocation;
	    
	    if (location != null) {
	        myLocation = new LatLng(location.getLatitude(),
	                location.getLongitude());
	        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
		    		15));
	    }
	    
	}
	
	/**
	 * Liest die Aktuelle Position aus
	 */
	private void SetMyPosition()
	{
		myLocation = map.getMyLocation();
	}
	
	/**
	 * Holt alle Koordinaten vom Server, die in der nähe sind
	 */
	private void GetCoords()
	{
			SetMyPosition();
			if (myLocation == null) {
				return;
			}
			new Thread(new Runnable() {
  			
          	private final Handler handler = new Handler(){
          		
          		public void handleMessage(Message msg)
          		{
          			Toast.makeText(getApplicationContext(), msg.getData().getString("success"), Toast.LENGTH_LONG).show();
          			if (msg.getData().getString("success").equals("Tracks wurden ausgelesen")) {
          				SetCoords();
					}
          		}
          	};
          	
  			@Override
  			public void run() {
  				Bundle b = new Bundle();
  				
  				try {
  	  				GetSimpleTracksResult result = SimpleTrack.GetSimpleTracks(myLocation.getLatitude(), myLocation.getLongitude());
  	  				if (result != null) {
  	  					_result = result;
  	  					b.putString("success", "Tracks wurden ausgelesen");
  	  				}else
  	  				{
  	  					_result = new GetSimpleTracksResult();
  	  					b.putString("success", "Tracks konnten nicht gelesen werden!");
  	  				}

				} catch (Exception e) {
					b.putString("success", "Tut mir leid aber es ist ein Fehler aufgetreten :(");
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
	 * Setzt die Koordinaten auf die Map
	 */
	private void SetCoords()
	{
		map.clear();
		try {
			String[] split = _result.getDataString().split("<");
			
			
			for (String string : split) {
				if (string.equals("")) {
					continue;
				}
				//Friemel den Kram außereinander
				String[] values = string.split("#");
				
				String description = "";
				
				String time = values[1];
				String user = "";
				
				if (values.length == 3) {
					user = values[2];
					description = "Wurde von:" + user + " um:" + time + "getracked";
				}else
				{
					description = "Wurde um:" + time + "getracked";
				}
				
				String[] cStr = values[0].split(";");
				double tempLat = Double.parseDouble(cStr[0]);
				double tempLon = Double.parseDouble(cStr[1]);
				LatLng tempPosition = new LatLng(tempLat, tempLon);
				
				if (map!=null){
				      Marker tempMarker = map.addMarker(new MarkerOptions()
				          .position(tempPosition)
				          .title("BULLE")
				          .snippet(description)
				          .icon(BitmapDescriptorFactory
				              .fromResource(R.drawable.ic_launcher)));
				      _marker.add(tempMarker);
				    }
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Es ist ein Fehler aufgestreten :(", Toast.LENGTH_LONG).show();
		}

	}

}
