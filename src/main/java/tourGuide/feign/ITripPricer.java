package tourGuide.feign;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tripPricer.Provider;

@FeignClient(name = "tripPricer-api", url = "localhost:8083", path = "/")

public interface ITripPricer {
	@RequestMapping("/getPrice")
	public List<Provider> getPrice(@RequestParam("apiKey") String apiKey,
			@RequestParam("attractionId") UUID attractionId, @RequestParam("adults") int adults,
			@RequestParam("children") int children, @RequestParam("nightsStay") int nightsStay,
			@RequestParam("rewardsPoints") int rewardsPoints);
	
	
	@RequestMapping("/getProvider")
	public String getprovider(@RequestParam("apiKey") String apiKey, @RequestParam("adults") int adults);
}
