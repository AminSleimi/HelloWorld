package com.asleimi.helloworld;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{
	
	private LocationClient mLocationClient;
	Location mCurrentLocation;
	private static LatLng testUserLocation;

	private List<Site> siteList = new ArrayList<Site>();
	public static final String PHOTOS_BASE_URL = 
			"http://whc.unesco.org/uploads/sites/";
	//ProgressBar pb;
	List<MyTask> tasks;
	GoogleMap googleMap;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationClient = new LocationClient(this, this, this);
        
        
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
       /*
        
        pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
		*/
		tasks = new ArrayList<>();
		
		if (isOnline()) {
			requestData("http://10.0.2.2:8080/TestWS/webresources/entities.site");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
		}
        
        //show Tunisia full map
        LatLng loc1 = new LatLng(33.7931605, 9.5607654);
        //zoom the location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc1, 6));
        
        
        
        
        /*
        siteList.add(new Site(1, "Coliseum ElJem", null, "site_38.jpg", 35.296457, 10.706908));
        siteList.add(new Site(1, "Parc national de l'Ichkeul", null, "site_11.jpg", 37.1636100000, 9.6747200000));
        siteList.add(new Site(1, "Site archéologique de Carthage", null, "site_37.jpg", 36.8527800000, 10.3233300000));
       
        for (Site site : siteList) {
			
        	
        	googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(site.getLatitudeSite(),site.getLongitudeSite()))
            .title(site.getNomSite()));
			
			
		}
        
        
        /*
        googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(35.296457,10.706908))
        .title("Coliseum ElJem"));
        
        googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(37.1636100000,9.6747200000))
        .title("Parc national de l'Ichkeul"));
        
        googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(37.1636100000,9.6747200000))
        .title("Parc national de l'Ichkeul"));
        
        googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(36.8527800000,10.3233300000))
        .title("Site archéologique de Carthage"));
        
        googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(36.9463900000,11.0991700000))
        .title("Cité punique de Kerkouane et sa nécropole"));
        
        googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(36.4236100000,9.2202800000))
        .title("Dougga / Thugga"));
        
        */
        
        
        /*
        
        GeoPoint tlGpt; // Top left (NW) Geopoint
        GeoPoint brGpt; // Bottom right (SE) GeoPoint

        GeoPoint trGpt; // Top right (NE) Geopoint
        GeoPoint blGpt; // Bottom left (SW) GeoPoint

        tlGpt = googleMap.getProjection().fromPixels(0, 0);
        brGpt = googleMap.getProjection().fromPixels(googleMap.getWidth(),
        		googleMap.getHeight());

        trGpt = googleMap.getProjection().fromPixels(googleMap.getWidth(), 0);
        blGpt = googleMap.getProjection().fromPixels(0, googleMap.getHeight());
        */
    }
    
    

	private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}
    
    protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}
    
    private class MyTask extends AsyncTask<String, String, List<Site>> {

		@Override
		protected void onPreExecute() {
			/*if (tasks.size() == 0) {
				pb.setVisibility(View.VISIBLE);
			}*/
			tasks.add(this);
		}
		
		
		
		@Override
		protected List<Site> doInBackground(String... params) {
			
			String content = HttpManager.getData(params[0]);//, "feeduser", "feedpassword"
			siteList= SiteJSONParser.parseFeed(content);
			
			
		
			return siteList;
		}
		
		protected void onPostExecute(List<Site> result) {
			
			tasks.remove(this);
			/*if (tasks.size() == 0) {
				pb.setVisibility(View.INVISIBLE);
			}*/
			
			if (result == null) {
				Toast.makeText(MainActivity.this, "Web service not available", Toast.LENGTH_LONG).show();
				return;
			}
			
			siteList = result;
			for (Site site : siteList) {
				
	        	
	        	googleMap.addMarker(new MarkerOptions()
	            .position(new LatLng(site.getLatitudeSite(),site.getLongitudeSite()))
	            .title(site.getNomSite()));
	        	
	        	
				
				
			}
			
			//intent
			
			googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker arg0) {
					Intent descriptionSiteIntent = new Intent(MainActivity.this, DescriptionSite.class);
					//String reference = mMarkerPlaceLink.get(arg0.getId());
					String str = arg0.getId();
					str = str.replaceAll("[^\\d.]", "");
					int location = 0;
					try {
						location = ((Number)NumberFormat.getInstance().parse(str)).intValue();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Site site = (Site) siteList.get(location);
					
					
					
					descriptionSiteIntent.putExtra("nomSite", site.getNomSite());
					descriptionSiteIntent.putExtra("descSite", site.getDescriptionSite());
					descriptionSiteIntent.putExtra("latSite", site.getLatitudeSite());
					descriptionSiteIntent.putExtra("lonSite", site.getLongitudeSite());
					
					descriptionSiteIntent.putExtra("latUser", mCurrentLocation.getLatitude());
					descriptionSiteIntent.putExtra("lonUser",mCurrentLocation.getLongitude());
					startActivity(descriptionSiteIntent);
					
				}
			});

		}
		
	}
    
    
 // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    
    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    
    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                    
                    break;
                }
            
        }
     }
    
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        }
		return false; 
        
    }
    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        
        mCurrentLocation = mLocationClient.getLastLocation();
        if (mLocationClient.isConnected())
        	testUserLocation = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		

    }
    
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    
  
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
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
    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
     
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
    
}