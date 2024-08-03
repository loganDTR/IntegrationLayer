package net.zengfx.integrationlayer.services;

import lombok.RequiredArgsConstructor;
import net.zengfx.integrationlayer.config.ServiceProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebServiceRegistryOffice {

    private final ServiceProperties serviceProperties;

    public Mono<?> getUser(String office, int id) {
        ServiceProperties.OfficeProperties officeProperties = serviceProperties.getOffices().get(office);

        if (officeProperties == null) {
            return Mono.error(new IllegalArgumentException("Office not found"));
        }

        WebClient webClient = getWebClient(officeProperties);

        return webClient.get().uri(officeProperties.getUriGet(), id).retrieve().bodyToMono(getClassForName(officeProperties.getResponseClass()));
    }

    public Mono<?> postUser(String office, Object body) {
        ServiceProperties.OfficeProperties officeProperties = serviceProperties.getOffices().get(office);

        if (officeProperties == null) {
            return Mono.error(new IllegalArgumentException("Office not found"));
        }

        WebClient webClient = getWebClient(officeProperties);

        return webClient.post().uri(officeProperties.getUriPost()).bodyValue(body).retrieve().bodyToMono(getClassForName(officeProperties.getResponseClass()));
    }

    private WebClient getWebClient(ServiceProperties.OfficeProperties officeProperties) {
        return WebClient.builder().baseUrl(officeProperties.getBaseUrl()).build();
    }

    private Class<?> getClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found", e);
        }
    }
}
