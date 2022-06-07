package tourGuide.service;

import java.util.List;
import java.util.UUID;

import tripPricer.Provider;
import tripPricer.TripPricer;

public class TripPriceService {
	private TripPricer tripPricer;
	
	 public List<Provider> getPriceTest(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
	        List<Provider> providers = tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
			return providers; 
	        
	        
	 }
	    public String getProviderNameTest(String apiKey, int adults) {
	    String	providerName=tripPricer.getProviderName(apiKey, adults);
	    	
	    	return providerName;
	    	
	    }
}
