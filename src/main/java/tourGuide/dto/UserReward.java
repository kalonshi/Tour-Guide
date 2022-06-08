package tourGuide.dto;


import tourGuide.model.UserLocation;

public class UserReward {

	public final UserLocation userLocation;
	public final UserAttraction userAttraction;
	private int rewardPoints;
	
	
	

	public UserReward(UserLocation userLocation, UserAttraction userAttraction) {
		super();
		this.userLocation = userLocation;
		this.userAttraction = userAttraction;
	}

	public UserReward(UserLocation userLocation, UserAttraction userAttraction, int rewardPoints) {
		super();
		this.userLocation = userLocation;
		this.userAttraction = userAttraction;
		this.rewardPoints = rewardPoints;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	
	public int getRewardPoints() {
		return rewardPoints;
	}
	
}
