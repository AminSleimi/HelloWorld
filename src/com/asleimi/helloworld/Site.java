package com.asleimi.helloworld;


public class Site {
	
	private int idSite;
	private String nomSite;
	private String descriptionSite;
	private String photoSite;
	private double latitudeSite;
	private double longitudeSite;
	public int getIdSite() {
		return idSite;
	}
	public void setIdSite(int idSite) {
		this.idSite = idSite;
	}
	public String getNomSite() {
		return nomSite;
	}
	public void setNomSite(String nomSite) {
		this.nomSite = nomSite;
	}
	public String getDescriptionSite() {
		return descriptionSite;
	}
	public void setDescriptionSite(String descriptionSite) {
		this.descriptionSite = descriptionSite;
	}
	public String getPhotoSite() {
		return photoSite;
	}
	public void setPhotoSite(String photoSite) {
		this.photoSite = photoSite;
	}
	public double getLatitudeSite() {
		return latitudeSite;
	}
	public void setLatitudeSite(double latitudeSite) {
		this.latitudeSite = latitudeSite;
	}
	public double getLongitudeSite() {
		return longitudeSite;
	}
	public void setLongitudeSite(double longitudeSite) {
		this.longitudeSite = longitudeSite;
	}
	public Site(int idSite, String nomSite, String descriptionSite,
			String photoSite, double latitudeSite, double longitudeSite) {
		super();
		this.idSite = idSite;
		this.nomSite = nomSite;
		this.descriptionSite = descriptionSite;
		this.photoSite = photoSite;
		this.latitudeSite = latitudeSite;
		this.longitudeSite = longitudeSite;
	}
	
	
	

}
