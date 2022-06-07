package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

public class TestRewardsService {

	@Test
	public void usersGetRewards() {
		GpsUtilService gpsUtilService = new GpsUtilService();
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		assertTrue(user.getUserRewards().size() == 0);

		Attraction attraction = gpsUtilService.getAttractions().get(4);
		
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		rewardsService.calculateRewards(user);
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
		}

		tourGuideService.tracker.stopTracker();
		List<UserReward> userRewards = user.getUserRewards();
		System.out.println("userRewards.size():  " + userRewards.size());
		
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void nearAllAttractions() {
		GpsUtilService gpsUtilService = new GpsUtilService();
		InternalTestHelper.setInternalUserNumber(0);
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		assertTrue(user.getUserRewards().size() == 0);

		gpsUtilService.getAttractions().forEach(attract -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attract, new Date()));
			rewardsService.calculateDistanceReward(user, user.getLastVisitedLocation(), attract);
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
			}
		});

		List<UserReward> userRewards = tourGuideService.getUserRewards(user);
		tourGuideService.tracker.stopTracker();

		assertEquals(gpsUtilService.getAttractions().size(), userRewards.size());
	}

}
