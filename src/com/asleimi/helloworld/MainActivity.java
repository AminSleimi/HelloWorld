package com.asleimi.helloworld;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

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
					startActivity(descriptionSiteIntent);
					
				}
			});

		}
		
	}
}