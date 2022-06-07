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
import tourGuide.feign.IGpsUtils;
import tourGuide.model.User;
import tourGuide.model.UserLocation;


@Service
public class GpsUtilServiceFeign {
	private IGpsUtils iGpsUtils;
private ExecutorService executor = Executors.newFixedThreadPool(1000);

	public List<UserAttraction> getAttractions() {
		List<UserAttraction> allAttractions=iGpsUtils.getAttractions();
		/*
		 * Collections.sort(allAttractions, new AttractionNameComparator());
		 */
		 return allAttractions;
	}

	public List<UserAttraction> getUserAttractions() {

		List<UserAttraction> getUserAttractions =iGpsUtils.getAttractions();
		
		return getUserAttractions;
	}

	public UserLocation getUserLocationApi(UUID userId) {
		
		return iGpsUtils.getLocation(userId);
	}

	public void submitUserLocation(User user, TourGuideService tourGuideService) {
		CompletableFuture.supplyAsync(() -> {
			return getUserLocationApi(user.getUserId());
		}, executor).thenAccept(userLocation -> {

			tourGuideService.finalizeUserLocation(user, userLocation);
		});
	}
}
