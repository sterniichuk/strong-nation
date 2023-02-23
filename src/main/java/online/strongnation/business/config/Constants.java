package online.strongnation.business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources({
        @PropertySource("classpath:custom.properties")
})
public class Constants {

    public static int MAX_NUMBER_OF_CATEGORIES_OF_POST = 100;
    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Value("${directory.photo}")
    public String PATH_TO_PHOTO_DIRECTORY;
    @Value("${directory.post.photo.name}")
    public String POST_PHOTO_DIRECTORY_NAME;

    @Value("${directory.slider.photo.name}")
    public String SLIDER_PHOTO_DIRECTORY_NAME;
    @Value("${post.photo.default.path}")
    public String PATH_TO_DEFAULT_POST_PHOTO;
}
