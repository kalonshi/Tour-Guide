package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.comparatorTools.RecommandedAttractionDistanceComparator;
import tourGuide.dto.RecommandedAttraction;
import tourGuide.dto.UserAttraction;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	private final GpsUtilService gpsUtilService;
	private final RewardsService rewardsService;

	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	ExecutorService executor = Executors.newFixedThreadPool(1000);
	/*
	 * Fixed Thread Pool : un pool qui contient un nombre fixe de threads. Ceux-ci
	 * sont utilises pour executer en parallele les differentes taches. Si tous les
	 * threads sont occupes alors la tache est empilee jusqu'a ce qu'elle puisse
	 * etre executee par un thread
	 */
	boolean testMode = true;

	/* boolean testMode =false; */
	public TourGuideService(GpsUtilService gpsUtilService, RewardsService rewardsService) {
		this.gpsUtilService = gpsUtilService;
		this.rewardsService = rewardsService;

		if (testMode) {
			logger.info("TestMode enabled");

			logger.debug("Initializing users");

			initializeInternalUsers();

			logger.debug("Finished initializing users");
			initializeTripPricer();
		}
		tracker = new Tracker(this);

		addShutDownHook();
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Shutdown TourGuideService");
				tracker.stopTracker();
			}
		});
	}

	private void initializeTripPricer() {
		logger.debug("Initialize tripPricer");
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	/*********************************************************************/

	/*
	 * methode de recuperation de la localisation actuelle de l'user: Recuperation
	 * de l'object VisitedLocation
	 */

	public void trackUserLocation(User user) {
		gpsUtilService.submitLocation(user, this);
	}

	/*
	 * methode de recuperation de la dernier visited Location si elle existe sinon
	 * ajouter la localisation actuelle de l'user avec trackUserLocation:
	 * Recuperation de l'object VisitedLocation
	 */
	public VisitedLocation getUserLocation(User user) {

		if (user.getVisitedLocations().size() > 0) {
			VisitedLocation visitedLocation = user.getLastVisitedLocation();
			return visitedLocation;
		} else {
			trackUserLocation(user);
			VisitedLocation visitedLocation = user.getLastVisitedLocation();

			return visitedLocation;
		}
	}

	/*
	 * Ajout de la localisation dans l'historique de localisation
	 */
	public void finalizeLocation(User user, VisitedLocation visitedLocation) {
		user.addToVisitedLocations(visitedLocation);
		System.out.println("liste des localisation=" + user.getVisitedLocations().size());
		rewardsService.calculateRewards(user);
		tracker.finalizeTrack(user);

	}

	/* Liste des recentes localisations des users HashMap  */
	  
	  public HashMap<String, Location> getAllUsersCurrentLocations() {
			HashMap<String, Location> allUsersCurrentLocations = new HashMap<>();
			getAllUsers()
					.forEach(u -> allUsersCurrentLocations.put(u.getUserId().toString(), u.getLastVisitedLocation().location));
			return allUsersCurrentLocations;
		}	 

	/** getAllAttractions **/
	public List<RecommandedAttraction> getAllAttraction(User user) {
		List<RecommandedAttraction> recommandedAttractions = new ArrayList<>();
		// Test VisitedLocation 080522
		gpsUtilService.getUserLocationApi(user.getUserId());
		while (user.getVisitedLocations().isEmpty()) {
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		// Fin de Test VisitedLocation 080522

		Location lastVistedLocation = user.getVisitedLocations().get(user.getVisitedLocations().size() - 1).location;
		/*
		 * Location
		 * lastVistedLocationNull=gpsUtilService.getVisitedLocationFromUserLocation2(
		 * user.getUserId()).location;
		 */
		for (UserAttraction userAttraction : gpsUtilService.getUserAttractions()) {
			Location attractionLocation = new Location(userAttraction.getAttractionLattitude(),
					userAttraction.getAttractionLongitude());

			recommandedAttractions.add(new RecommandedAttraction(userAttraction.getAttractionName(),
					lastVistedLocation.latitude, lastVistedLocation.longitude, userAttraction.getAttractionLattitude(),
					userAttraction.getAttractionLongitude(),
					rewardsService.getDistance(lastVistedLocation, attractionLocation),
					rewardsService.getRewardPoints(userAttraction, user)));
		}

		Collections.sort(recommandedAttractions, new RecommandedAttractionDistanceComparator());
		return recommandedAttractions;
	}

	/** Closest five attractions **/
	public List<RecommandedAttraction> getFiveClosestAttraction(User user) {
		List<RecommandedAttraction> recommandedAttractions = getAllAttraction(user);
		List<RecommandedAttraction> fiveClosestAttraction = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			fiveClosestAttraction.add(new RecommandedAttraction(recommandedAttractions.get(i).getAttractionName(),
					recommandedAttractions.get(i).getUserLat(), recommandedAttractions.get(i).getUserLong(),
					recommandedAttractions.get(i).getAttractionLat(), recommandedAttractions.get(i).getAttractionLong(),
					recommandedAttractions.get(i).getDistance(), recommandedAttractions.get(i).getRewardPoint()));
		}

		return fiveClosestAttraction;
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

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();

	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			 generateUserPreferences( user);
			internalUserMap.put(userName, user);
		});
		((org.slf4j.Logger) logger)
				.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}
	private void generateUserPreferences(User user) {
		UserPreferences userPreferences = new UserPreferences();
		userPreferences.setNumberOfAdults(new Random().nextInt(3) + 1);
		userPreferences.setNumberOfChildren(new Random().nextInt(5));
		userPreferences.setLowerPricePoint(Money.of(new Random().nextInt(1000), userPreferences.getCurrency()));
		userPreferences.setHighPricePoint(Money.of(new Random().nextInt(5000), userPreferences.getCurrency()).add(userPreferences.getLowerPricePoint()));
		userPreferences.setTripDuration(new Random().nextInt(14) + 1);
		userPreferences.setTicketQuantity(userPreferences.getNumberOfAdults() + userPreferences.getNumberOfChildren() / 2);
		user.setUserPreferences(userPreferences);
	}
	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
	/**Save userPreferences for a  User**/
	public UserPreferences saveUserPreference(String userName,UserPreferences userPreferences) {
		
		getUser(userName).setUserPreferences(userPreferences);
		return userPreferences ;
	}

}
