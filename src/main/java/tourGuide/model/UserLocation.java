package tourGuide.model;

import java.util.Date;
import java.util.UUID;





public class UserLocation {
	 private UUID userId ;
	    private Location location;
	   private  Date timeLocation;
	public UserLocation(UUID userId, Location location, Date timeLocation) {
		super();
		this.userId = userId;
		this.location = location;
		this.timeLocation = timeLocation;
	}
	
	public UserLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UUID getUserId() {
		return userId;
	}
	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Date getTimeLocation() {
		return timeLocation;
	}
	public void setTimeLocation(Date timeLocation) {
		this.timeLocation = timeLocation;
	}
	
	    
	  
}
