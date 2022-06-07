package tourGuide.feign;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




import tourGuide.dto.UserAttraction;
import tourGuide.model.UserLocation;


@FeignClient(name = "gpsUtil-api", url = "localhost:8081", path = "/")
public interface IGpsUtils {
	@GetMapping("/getAttractions")
	public List<UserAttraction> getAttractions() ;
	
	
	 @GetMapping("/getLocation")
	    public UserLocation getLocation(@RequestParam("userId") UUID userId);

	 }

