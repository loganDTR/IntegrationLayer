package net.zengfx.integrationlayer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "services")
public class ServiceProperties {

    private Map<String, OfficeProperties> offices;

    @Setter
    @Getter
    public static class OfficeProperties {
        private String baseUrl;
        private String uriGet;
        private String uriPost;
        private String responseClass;

    }
}