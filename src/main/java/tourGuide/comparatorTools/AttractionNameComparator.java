package tourGuide.comparatorTools;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import gpsUtil.location.Attraction;

public class AttractionNameComparator  implements Comparator<Attraction>{

	@Override
	public int compare(Attraction arg0, Attraction arg1) {
		 return new CompareToBuilder()
	                .append(arg0.attractionName,arg1.attractionName).toComparison();
	 
	}

}