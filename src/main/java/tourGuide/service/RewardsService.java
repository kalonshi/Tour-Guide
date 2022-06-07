package tourGuide.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.dto.UserAttraction;
import tourGuide.model.User;
import tourGuide.model.UserReward;

@Service
public class RewardsService {
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	private int proximityBufferMiles = 10;

	private final RewardCentral rewardsCentral;

	private final GpsUtilService gpsUtilService;

	private ExecutorService executor = Executors.newFixedThreadPool(1000);

	public RewardsService(GpsUtilService gpsUtilService, RewardCentral rewardCentral) {
		this.rewardsCentral = rewardCentral;
		this.gpsUtilService = gpsUtilService;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBufferMiles = proximityBuffer;
	}

//old code

	public void calculateRewards(User user) {
		List<Attraction> attractions = gpsUtilService.getAttractions();
		List<VisitedLocation> visitedLocationList = user.getVisitedLocations().stream().collect(Collectors.toList());
		/* int i = 1; */
		/* int j = 0; */
		for (VisitedLocation visitedLocation : visitedLocationList) {

			/*
			 * System.out.println("VisitedLocation visitedLocation latitude=" +
			 * visitedLocation.location.latitude);
			 * System.out.println("nb de visited location : " + i); i++;
			 * 
			 * System.out.println("nb de visited location : " + i); i++;
			 */
			for (Attraction attraction : attractions) {

				/*
				 * System.out.println("attraction.attractionName=" + attraction.attractionName);
				 * 
				 * System.out.println("attractionId=" + attraction.attractionId);
				 * 
				 * System.out.println("user.getUserRewards() size =" +
				 * user.getUserRewards().size());
				 * 
				 * System.out.println("userName :" + user.getUserName());
				 * System.out.println("userName visited location size :" +
				 * user.getVisitedLocations().size());
				 */
				if (user.getUserRewards().stream()
						.filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					calculateDistanceReward(user, visitedLocation, attraction);

					/*
					 * System.out.println("nb reward : " + j); j++;
					 */

				}
			}
		}
	}

		public void calculateDistanceReward(User user, VisitedLocation visitedLocation, Attraction attraction) {
		Double distance = getDistance(attraction, visitedLocation.location);
		/*
		 * System.out.println("distance :"+distance+"<= proximityBufferMiles"+
		 * proximityBufferMiles);
		 */if (distance <= proximityBufferMiles) {
			UserReward userReward = new UserReward(visitedLocation, attraction, distance.intValue());
			submitRewardPoints(userReward, attraction, user);

			System.out.println("attraction reward: " + attraction.attractionName);

		}
	}

	private void submitRewardPoints(UserReward userReward, Attraction attraction, User user) {
		// userReward.setRewardPoints(10);
		// user.addUserReward(userReward);
		CompletableFuture.supplyAsync(() -> {
			return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
		}, executor).thenAccept(points -> {
			userReward.setRewardPoints(points);
			user.addUserReward(userReward);
		});
	}

	public int getRewardPoints(UserAttraction attraction, User user) {

		return rewardsCentral.getAttractionRewardPoints(attraction.getAttractionId(), user.getUserId());
	}

	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math
				.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}
	public double getDistanceFeign(tourGuide.model.Location loc1, tourGuide.model.Location loc2) {
		double lat1 = Math.toRadians(loc1.getLatitude());
		double lon1 = Math.toRadians(loc1.getLongitude());
		double lat2 = Math.toRadians(loc2.getLatitude());
		double lon2 = Math.toRadians(loc2.getLongitude());

		double angle = Math
				.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}
}
