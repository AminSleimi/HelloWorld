package com.asleimi.helloworld;

import java.io.Serializable;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DescriptionSite extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_description_site);
		
		final TextView nomSiteTextView = (TextView) findViewById(R.id.nomSitetextView);
		final TextView descriptionSiteTextView = (TextView) findViewById(R.id.descriptionSiteTextView);
		//final ImageView siteImageView = (ImageView) findViewById(R.id.siteImageView);
		
		Serializable extra = getIntent().getSerializableExtra("nomSite");
		Serializable extraDesc = getIntent().getSerializableExtra("descSite");
		
		if(extra != null && extraDesc != null)
		{
			nomSiteTextView.setText(extra.toString());
			descriptionSiteTextView.setText(extraDesc.toString());
		}
		
		
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
}
