package tourGuide.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import tourGuide.model.User;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

@RestController
public class RewardController {
	@Autowired
	private TourGuideService tourGuideService;
	@Autowired
	private RewardsService rewardService;

	private User getUser(String userName) {
		return tourGuideService.getUser(userName);
	}

	@RequestMapping("/getRewards")
	public String getRewards(@RequestParam String userName) {

		rewardService.calculateRewards(getUser(userName));
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
		}

		return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
	}

}
