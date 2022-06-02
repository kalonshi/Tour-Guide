package tourGuide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tourGuide.service.TourGuideService;
import tourGuide.user.UserPreferences;

@RestController
public class UserController {
	@Autowired
	TourGuideService tourGuideService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}
	
	 @PostMapping("/saveUserPreference/{userName}")
	    public UserPreferences setUserPreference(@PathVariable String userName, @RequestBody UserPreferences userPreferences){
		
		 return tourGuideService.saveUserPreference(userName,userPreferences) ;
	    }
}

