package tourGuide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.model.User;
import tourGuide.service.TourGuideService;

@RestController
public class GpsUtilController {

	@Autowired
	TourGuideService tourGuideService;

	@RequestMapping("/getLocation")
	public String getLocation(@RequestParam String userName) {
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
	}

	@RequestMapping("/getAllUsersCurrentLocations")
	public String getAllUsersCurrentLocations() throws Exception {
		/*
		 * TODO: Get a list of every user's most recent location as JSON //- Note: does
		 * not use gpsUtil to query for their current location, but rather gathers the
		 * user's current location from their stored location history. // Return object
		 * should be the just a JSON mapping of userId to Locations similar to: {
		 * "019b04a9-067a-4c76-8817-ee75088c3822":
		 * {"longitude":-48.188821,"latitude":74.84371} }
		 */
		return JsonStream.serialize(tourGuideService.getAllUsersCurrentLocations());
	}

	private User getUser(String userName) {
		return tourGuideService.getUser(userName);
	}
}
