package tourGuide.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.Attraction;
import tourGuide.dto.RecommandedAttraction;
import tourGuide.dto.UserAttraction;
import tourGuide.feign.IGpsUtils;
import tourGuide.model.User;
import tourGuide.service.GpsUtilService;
import tourGuide.service.TourGuideService;

@RestController
public class AttractionControllerFeign {

	@Autowired
	private IGpsUtils iGpsUtils;
	@Autowired
	private TourGuideService tourGuideService;

	private User getUser(String userName) {
		return tourGuideService.getUser(userName);
	}

	@GetMapping("/getRecommandedAttractionsFeign")
	public List<RecommandedAttraction> getRecommandedAttractions(@RequestParam String userName) {
		User user = getUser(userName);
		return tourGuideService.getAllAttractionFeign(user);
	}

	@GetMapping("/getNearbyAttractionsFeign")
	public String getNearbyAttractions(String userName) {

		User user = getUser(userName);
		return JsonStream.serialize(tourGuideService.getFiveClosestAttractionFeign(user));
	}

	@GetMapping("/getAttractionsFeign")
	public List<UserAttraction> getAttractions() {
		return iGpsUtils.getAttractions();
	}
}
