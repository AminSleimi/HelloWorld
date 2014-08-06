package com.asleimi.helloworld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class DescriptionSite extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{
	
	private Polyline newPolyline;
	private GoogleMap map;
	private SupportMapFragment fragment;
	private static LatLng UserLocation = new LatLng(36.802316,10.179781);
	private static LatLng siteLocation = new LatLng(48.856132, 2.352448);
	private LatLngBounds latlngBounds;
	private int width, height;
	
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST=9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_description_site);
		
		final TextView nomSiteTextView = (TextView) findViewById(R.id.nomSitetextView);
		final TextView descriptionSiteTextView = (TextView) findViewById(R.id.descriptionSiteTextView);
		//final ImageView siteImageView = (ImageView) findViewById(R.id.siteImageView);
		final Button directionButton = (Button) findViewById(R.id.directionButton);
	    fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
		map = fragment.getMap();
		/*
		LocationClient mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();
		Location mCurrentLocation;
		try {
			mCurrentLocation = mLocationClient.getLastLocation();
			UserLocation = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		*/
		Serializable extra = getIntent().getSerializableExtra("nomSite");
		Serializable extraDesc = getIntent().getSerializableExtra("descSite");
		Serializable extraLat = getIntent().getSerializableExtra("latSite");
		Serializable extraLon = getIntent().getSerializableExtra("lonSite");
		
		if(extra != null && extraDesc != null && extraLat != null && extraLon != null)
		{
			Double lat = (Double)extraLat;
			Double lon = (Double)extraLon;
			nomSiteTextView.setText(extra.toString());
			descriptionSiteTextView.setText(extraDesc.toString());
			siteLocation = new LatLng(lat, lon);
		}
		
		findDirections(UserLocation.latitude, UserLocation.longitude,siteLocation.latitude, siteLocation.longitude, GMapV2Direction.MODE_DRIVING);
		
		directionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findDirections(UserLocation.latitude, UserLocation.longitude,siteLocation.latitude, siteLocation.longitude, GMapV2Direction.MODE_DRIVING);
				directionButton.setVisibility(android.view.View.INVISIBLE);
			}
		});
		
	}

	public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);
		
		GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
		asyncTask.execute(map);	
	}
	
	public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
		PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

		for(int i = 0 ; i < directionPoints.size() ; i++) 
		{          
			rectLine.add(directionPoints.get(i));
		}
		if (newPolyline != null)
		{
			newPolyline.remove();
		}
		newPolyline = map.addPolyline(rectLine);
		
		latlngBounds = createLatLngBoundsObject(UserLocation, siteLocation);
		width = getWindowManager().getDefaultDisplay().getWidth()-100;
		height = getWindowManager().getDefaultDisplay().getHeight()-150;
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
		
	}
	
	private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation)
	{
		if (firstLocation != null && secondLocation != null)
		{
			LatLngBounds.Builder builder = new LatLngBounds.Builder();    
			builder.include(firstLocation).include(secondLocation);
			
			return builder.build();
		}
		return null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.description_site, menu);
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
	public void onConnected(Bundle arg0) {
		
		Toast.makeText(this,  "Connected",  Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this,  "Disconnected",  Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showDialog(connectionResult.getErrorCode());
        }
		
	}
}
