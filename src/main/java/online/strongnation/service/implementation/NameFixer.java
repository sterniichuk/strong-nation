package online.strongnation.service.implementation;

import online.strongnation.config.NameProperties;
import online.strongnation.exception.IllegalCountryException;
import online.strongnation.exception.IllegalRegionException;
import org.apache.commons.lang3.StringUtils;

public interface NameFixer {
    static String checkAndNormalizeRegion(final String name) {
        if (name == null) {
            throw new IllegalRegionException("Name of region is null");
        }
        if (name.length() > NameProperties.REGION_NAME_LENGTH) {
            throw new IllegalRegionException("Too long name of region");
        }
        String clearName = StringUtils.normalizeSpace(name);
        if (clearName.length() == 0) {
            throw new IllegalRegionException("Empty name");
        }
        return clearName;
    }

    static String checkAndNormalizeCountry(final String name) {
        if (name == null) {
            throw new IllegalCountryException("Name of country is null");
        }
        if (name.length() > NameProperties.COUNTRY_NAME_LENGTH) {
            throw new IllegalCountryException("Too long name of country");
        }
        String clearName = StringUtils.normalizeSpace(name);
        if (clearName.length() == 0) {
            throw new IllegalCountryException("Empty name");
        }
        return clearName;
    }
}
