package com.asleimi.helloworld;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SiteJSONParser {

public static List<Site> parseFeed(String content) {
		
		try {
			JSONArray ar = new JSONArray(content);
			List<Site> siteList = new ArrayList<>();
			
			for (int i = 0; i < ar.length(); i++) {
				
				JSONObject obj = ar.getJSONObject(i);
				Site site = new Site(i, null, null, null, 0, 0);
				
				site.setIdSite(obj.getInt("idSite"));
				site.setNomSite(obj.getString("nomSite"));
				//site.setPhotoSite(obj.getString("photoSite"));
				site.setDescriptionSite(obj.getString("descriptionSite"));
				site.setLongitudeSite(obj.getDouble("longitudeSite"));
				site.setLatitudeSite(obj.getDouble("latitudeSite"));
				
				
				siteList.add(site);
			}
			
			return siteList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
