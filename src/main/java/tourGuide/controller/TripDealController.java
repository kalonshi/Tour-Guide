package tourGuide.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import tourGuide.model.User;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

@RestController
	public class TripDealController {
		@Autowired
		private TourGuideService tourGuideService;

		private User getUser(String userName) {
			return tourGuideService.getUser(userName);
		}

		@RequestMapping("/getTripDeals")
		public String getTripDeals(@RequestParam String userName) {
			List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
			return JsonStream.serialize(providers);
		}

	}


