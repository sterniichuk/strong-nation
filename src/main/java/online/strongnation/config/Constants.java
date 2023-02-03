package online.strongnation.config;

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

    @Value("${directory.post.photo}")
    public String PATH_TO_POST_PHOTO_DIRECTORY;
    @Value("${directory.post.photo.name}")
    public String POST_PHOTO_DIRECTORY_NAME;
}
