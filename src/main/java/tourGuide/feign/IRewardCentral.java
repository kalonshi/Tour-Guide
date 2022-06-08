package tourGuide.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "rewardCentral-api", url = "localhost:8082", path = "/")

public interface IRewardCentral {
	 @GetMapping("/getAttractionRewardPoints")
	    public int getAttractionRewardPoints(@RequestParam("userId") UUID userId,@RequestParam("attractionId") UUID attractionId);
	        
	    
}
