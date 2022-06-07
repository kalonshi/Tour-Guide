package tourGuide.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.comparatorTools.AttractionNameComparator;
import tourGuide.dto.UserAttraction;
import tourGuide.model.User;
import tourGuide.model.UserLocation;

@Service
public class GpsUtilService {
	private GpsUtil gpsUtil;

	public GpsUtilService() {
		gpsUtil = new GpsUtil();
	}

	private ExecutorService executor = Executors.newFixedThreadPool(1000);

	public List<Attraction> getAttractions() {
		List<Attraction> allAttractions=gpsUtil.getAttractions();
		 Collections.sort(allAttractions, new AttractionNameComparator());

		 return allAttractions;
	}

	public List<UserAttraction> getUserAttractions() {

		List<UserAttraction> getUserAttractions = new ArrayList<UserAttraction>();
		List<Attraction> list = gpsUtil.getAttractions();
		list.forEach(att -> {
			UserAttraction userAtt = new UserAttraction(att.attractionId, att.attractionName, att.city, att.state,
					att.longitude, att.latitude);
			getUserAttractions.add(userAtt);

		});
		return getUserAttractions;
	}

	public VisitedLocation getUserLocationApi(UUID userId) {
		Locale.setDefault(Locale.ENGLISH);
		return gpsUtil.getUserLocation(userId);
	}

	public void submitLocation(User user, TourGuideService tourGuideService) {
		CompletableFuture.supplyAsync(() -> {
			return getUserLocationApi(user.getUserId());
		}, executor).thenAccept(visitedLocation -> {

			tourGuideService.finalizeLocation(user, visitedLocation);
		});
	}
	public void submitLocationApi(User user, TourGuideService tourGuideService) {
		CompletableFuture.supplyAsync(() -> {
			return getUserLocationApi(user.getUserId());
		}, executor).thenAccept(userLocation -> {
// TODO Corriger le problème de finalization de l'userlocation ( actuelement add visitedLocation
			
			tourGuideService.finalizeLocation(user, userLocation);
			});
	}
}
