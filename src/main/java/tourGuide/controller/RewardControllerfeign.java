package tourGuide.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import tourGuide.feign.IRewardCentral;
import tourGuide.model.User;
import tourGuide.service.RewardsService;
import tourGuide.service.RewardsServiceFeign;
import tourGuide.service.TourGuideService;

@RestController
public class RewardControllerfeign {
	@Autowired
	private TourGuideService tourGuideService;
	@Autowired
	private RewardsServiceFeign rewardsServiceFeign;

	private User getUser(String userName) {
		return tourGuideService.getUser(userName);
	}

	@RequestMapping("/getRewardsFeign")
	public String getRewards(@RequestParam String userName) {

		rewardsServiceFeign.calculateUserAttractionRewards(getUser(userName));
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
		}

		return JsonStream.serialize(tourGuideService.getUserRewardsFeign(getUser(userName)));
	}

}
