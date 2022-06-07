package tourGuide.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.Attraction;
import tourGuide.dto.RecommandedAttraction;
import tourGuide.model.User;
import tourGuide.service.GpsUtilService;
import tourGuide.service.TourGuideService;

@RestController
public class AttractionController {

	@Autowired
	private GpsUtilService gpsUtilService;
	@Autowired
	private TourGuideService tourGuideService;

	private User getUser(String userName) {
		return tourGuideService.getUser(userName);
	}

	@GetMapping("/getRecommandedAttractions")
	public List<RecommandedAttraction> getAttractions(@RequestParam String userName) {
		User user = getUser(userName);
		return tourGuideService.getAllAttraction(user);
	}

	@GetMapping("/getNearbyAttractions")
	public String getNearbyAttractions(String userName) {

		User user = getUser(userName);
		return JsonStream.serialize(tourGuideService.getFiveClosestAttraction(user));
	}

	@GetMapping("/getAttractions")
	public List<Attraction> getAttractions() {
		return gpsUtilService.getAttractions();
	}
}
