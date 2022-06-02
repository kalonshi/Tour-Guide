package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.dto.RecommandedAttraction;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

public class TestTourGuideService {


	@Test
	public void getUserLocationOriginalTestFixedTour2() {
		GpsUtilService gpsUtilService = new GpsUtilService();
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		tourGuideService.trackUserLocation(user);
		 while(user.getVisitedLocations().isEmpty()) { try {
			  TimeUnit.MILLISECONDS.sleep(1000); } catch (InterruptedException e) { } }
			 
		tourGuideService.tracker.stopTracker();

		assertTrue(user.getVisitedLocations().get(0).userId.equals(user.getUserId()));
	}


@Test
public void addUser() {
	GpsUtilService gpsUtil = new GpsUtilService();
	RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
	InternalTestHelper.setInternalUserNumber(0);
	TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

	User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
	User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
	tourGuideService.addUser(user);
	tourGuideService.addUser(user2);

	User retrivedUser = tourGuideService.getUser(user.getUserName());
	User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

	tourGuideService.tracker.stopTracker();

	assertEquals(user, retrivedUser);
	assertEquals(user2, retrivedUser2);
}

@Test
public void getAllUsers() {
	GpsUtilService gpsUtil = new GpsUtilService();
	RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
	 InternalTestHelper.setInternalUserNumber(0); 
	TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

	User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
	User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

	tourGuideService.addUser(user);
	tourGuideService.addUser(user2);

	List<User> allUsers = tourGuideService.getAllUsers();

	tourGuideService.tracker.stopTracker();

	assertTrue(allUsers.contains(user));
	assertTrue(allUsers.contains(user2));
}



@Test
public void trackUser()  {
	GpsUtilService gpsUtil = new GpsUtilService();
	RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
	TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
	InternalTestHelper.setInternalUserNumber(0);
	User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

	tourGuideService.trackUserLocation(user);
	while(user.getVisitedLocations().isEmpty()) { try {
		  TimeUnit.MILLISECONDS.sleep(1000); } catch (InterruptedException e) { } }
		 
	VisitedLocation visitedLocation = user.getLastVisitedLocation();

	tourGuideService.tracker.stopTracker();

	assertEquals(user.getUserId(), visitedLocation.userId);
}

// Test  avec VisitedLocation OK 080522 11:26
	@Test
	public void trackUserVisitedLocation()  {
		GpsUtilService gpsUtil = new GpsUtilService();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		InternalTestHelper.setInternalUserNumber(0);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		tourGuideService.trackUserLocation(user);
		 while(user.getVisitedLocations().isEmpty()) { try {
			  TimeUnit.MILLISECONDS.sleep(1000); } catch (InterruptedException e) { } }
			 
		VisitedLocation visitedLocation = user.getVisitedLocations().get(0);
	/*
	 * VisitedLocation visitedLocation = user.getLastVisitedLocation();
	 */tourGuideService.tracker.stopTracker();

		assertEquals(user.getUserId(), visitedLocation.userId);
	}

//Test avec la methode getFiveclosest Attractions
@Test
public void getNearbyAttractionsUserAttration2() {
	GpsUtilService gpsUtil = new GpsUtilService();
	RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
	InternalTestHelper.setInternalUserNumber(0);
	TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

	User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
	/* tourGuideService.trackUserLocations2(user); */

	
	// Test VisitedLocation 080522
	tourGuideService.trackUserLocation(user);
	 while(user.getVisitedLocations().isEmpty()) { try {
		  TimeUnit.MILLISECONDS.sleep(1000); } catch (InterruptedException e) { } }
	// Fin de Test VisitedLocation 080522	
	
	 
	 List<RecommandedAttraction> attractions = tourGuideService.getFiveClosestAttraction(user);
	
	tourGuideService.tracker.stopTracker();

	assertEquals(5, attractions.size());
}
// ok 240322 avec 10 not OK 120422

@Test
public void getTripDeals() {
	GpsUtilService gpsUtil = new GpsUtilService();
	RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
	InternalTestHelper.setInternalUserNumber(0);
	TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
	User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
	List<Provider> providers = tourGuideService.getTripDeals(user);
	tourGuideService.tracker.stopTracker();

	assertEquals(5, providers.size());
}




// ok 040422*****Attention internalTestUser use for
	// test*************************
@Ignore	
@Test
	public void getUserLocation() {
		GpsUtilService gpsUtilService = new GpsUtilService();
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);
		User user = tourGuideService.getAllUsers().get(0);

		tourGuideService.tracker.stopTracker();

		assertTrue(user.getVisitedLocations().get(0).userId.equals(user.getUserId()));

	}
	
}
