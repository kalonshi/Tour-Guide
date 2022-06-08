package tourGuide.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.dto.UserAttraction;
import tourGuide.feign.IGpsUtils;
import tourGuide.feign.IRewardCentral;
import tourGuide.model.User;
import tourGuide.model.UserLocation;
import tourGuide.model.UserReward;

@Service
public class RewardsServiceFeign {
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	private int proximityBufferMiles = 10;

	@Autowired
	private IRewardCentral iRewardCentral;
	
	@Autowired
	private GpsUtilServiceFeign gpsUtilServiceFeign;

	private ExecutorService executor = Executors.newFixedThreadPool(1000);

	public RewardsServiceFeign(IRewardCentral iRewardCentral, GpsUtilServiceFeign gpsUtilServiceFeign) {
		super();
		this.iRewardCentral = iRewardCentral;
		this.gpsUtilServiceFeign = gpsUtilServiceFeign;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBufferMiles = proximityBuffer;
	}

//old code

	public void calculateUserAttractionRewards(User user) {
		List<UserAttraction> userAttractions = gpsUtilServiceFeign.getAttractions();
		List<UserLocation> userLocations = user.getUserLocations().stream().collect(Collectors.toList());
		/* int i = 1; */
		/* int j = 0; */
		for (UserLocation userLocation : userLocations) {

			for (UserAttraction userAttraction : userAttractions) {
				if (user.getUserRewardsfeign().stream().filter(rewardFeign -> rewardFeign.userAttraction
						.getAttractionName().equals(userAttraction.getAttractionName())).count() == 0) {
					calculateDistanceRewardFeign(user, userLocation, userAttraction);
				}

			}

		}
	}

	public void calculateDistanceRewardFeign(User user, UserLocation userLocation, UserAttraction userAttraction) {
		tourGuide.model.Location loc = new tourGuide.model.Location(userAttraction.getLatitude(),
				userAttraction.getLongitude());
		Double distance = getDistanceFeign(loc, userLocation.getLocation());
		/*
		 * System.out.println("distance :"+distance+"<= proximityBufferMiles"+
		 * proximityBufferMiles);
		 */if (distance <= proximityBufferMiles) {
			tourGuide.dto.UserReward userReward = new tourGuide.dto.UserReward(userLocation, userAttraction,
					distance.intValue());
			submitRewardPointsFeign(userReward, userAttraction, user);

			System.out.println("attractionFeign reward: " + userAttraction.getAttractionName());

		}
	}

	private void submitRewardPointsFeign(tourGuide.dto.UserReward userReward, UserAttraction userAttraction,
			User user) {

		CompletableFuture.supplyAsync(() -> {
			return iRewardCentral.getAttractionRewardPoints(userAttraction.getAttractionId(), user.getUserId());
		}, executor).thenAccept(points -> {
			userReward.setRewardPoints(points);
			/* user.addUserRewardFeign(userReward); */
		});
	}
	public int getRewardPointsFeign(UserAttraction attraction, User user) {

		return iRewardCentral.getAttractionRewardPoints(attraction.getAttractionId(), user.getUserId());
	}
	public double getDistanceFeign(tourGuide.model.Location loc, tourGuide.model.Location location) {
		double lat1 = Math.toRadians(loc.getLatitude());
		double lon1 = Math.toRadians(loc.getLongitude());
		double lat2 = Math.toRadians(location.getLatitude());
		double lon2 = Math.toRadians(location.getLongitude());

		double angle = Math
				.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}

}
