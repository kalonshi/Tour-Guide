package tourGuide.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

public class User {
	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private List<VisitedLocation> visitedLocations = new ArrayList<>();
	
	private List<UserLocation> userLocations = new ArrayList<>();
		
	private List<UserReward> userRewards = new ArrayList<>();
	
	  private List<tourGuide.dto.UserReward> userRewardsfeign = new ArrayList<>();
	 
	private UserPreferences userPreferences = new UserPreferences();
	private List<Provider> tripDeals = new ArrayList<>();

	private Lock userLocationListLock = new ReentrantLock();

	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}

	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}

	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		userLocationListLock.lock();
		visitedLocations.add(visitedLocation);
		userLocationListLock.unlock();
	}
	
	
	public List<VisitedLocation> getVisitedLocations() {
		userLocationListLock.lock();
		try {
			return visitedLocations;
		} finally {
			userLocationListLock.unlock();
		}
	}
	
	public void clearVisitedLocations() {
		userLocationListLock.lock();
		visitedLocations.clear();
		userLocationListLock.unlock();
	}

	public VisitedLocation getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}
	
	//************* UserLocation test*********/
	//03062022
			public void addToUserLocations(UserLocation userLocation) {
				userLocationListLock.lock();
				userLocations.add(userLocation);
				userLocationListLock.unlock();
			}
		//03062022
		public List<UserLocation> getUserLocations() {
			userLocationListLock.lock();
			try {
				return userLocations;
			} finally {
				userLocationListLock.unlock();
			}
		}
//03062022
	
	public UserLocation getLastUserLocation() {
		return userLocations.get(userLocations.size() - 1);
	}
	public void clearUserLocations() {
		userLocationListLock.lock();
		userLocations.clear();
		userLocationListLock.unlock();
	}
	//03062022
	
	/*
	 * public void addToUserLocations(UserLocation userLocation) {
	 * userLocationListLock.lock(); userLocations.add(userLocation);
	 * userLocationListLock.unlock(); }
	 */
	 
//03062022
	/*
	 * public List<UserLocation2> getUserLocations2() { userLocationListLock.lock();
	 * try { return userLocations ; } finally { userLocationListLock.unlock(); } }
	 * //03062022
	 * 
	 * public UserLocation getLastUserLocation2() { return
	 * userLocations.get(userLocations.size() - 1); }
	 */


	/////********************************************////
	
	
	
	
	
	
	
	public synchronized void addUserReward(UserReward userReward) {
		userRewards.add(userReward);
	}
	// add method to userRewardFeign
	
	  public synchronized void addUserRewardFeign(tourGuide.dto.UserReward
	  userReward) { userRewardsfeign.add(userReward); }
	  
	  public List<tourGuide.dto.UserReward> getUserRewardsfeign() { return
	  userRewardsfeign; }
	 
	
	// End test 070622
	public List<UserReward> getUserRewards() {
		return userRewards;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public void setTripDeals(List<Provider> tripDeals) {
		this.tripDeals = tripDeals;
	}

	public List<Provider> getTripDeals() {
		return tripDeals;
	}
}
