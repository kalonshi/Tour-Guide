package tourGuide.comparatorTools;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import tourGuide.dto.UserAttraction;



public class UserAttractionNameComparator  implements Comparator<UserAttraction>{

	@Override
	public int compare(UserAttraction arg0, UserAttraction arg1) {
		 return new CompareToBuilder()
	                .append(arg0.getAttractionName(),arg1.getAttractionName()).toComparison();
	 
	}

}