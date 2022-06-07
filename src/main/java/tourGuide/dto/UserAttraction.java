package tourGuide.dto;

import java.util.UUID;

public class UserAttraction {
	private UUID attractionId ;
	private String attractionName; 
	private String city ;
    private String state ;
	 private double  longitude;
	 private double  latitude;
	 
	public UserAttraction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserAttraction(String attractionName, String city, String state, double attractionLongitude,
			double attractionLattitude) {
		super();
		this.attractionName = attractionName;
		this.city = city;
		this.state = state;
		this.longitude = attractionLongitude;
		this.latitude = attractionLattitude;
	}
	
	public UserAttraction(UUID attractionId, String attractionName, String city, String state,
			double attractionLongitude, double attractionLattitude) {
		super();
		this.attractionId = attractionId;
		this.attractionName = attractionName;
		this.city = city;
		this.state = state;
		this.longitude = attractionLongitude;
		this.latitude = attractionLattitude;
	}

	public String getAttractionName() {
		return attractionName;
	}
	public void setAttractionName(String attractionName) {
		this.attractionName = attractionName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double attractionLongitude) {
		this.longitude = attractionLongitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double attractionLattitude) {
		this.latitude = attractionLattitude;
	}
	public UUID getAttractionId() {
		return attractionId;
	}
	public void setAttractionId(UUID attractionId) {
		this.attractionId = attractionId;
	}
	
	
}
