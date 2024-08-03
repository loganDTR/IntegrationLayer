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

    public <T>Mono<T> getUser(String office, int id) {
        ServiceProperties.OfficeProperties officeProperties = serviceProperties.getOffices().get(office);

        if (officeProperties == null) {
            return Mono.error(new IllegalArgumentException("Office not found"));
        }

        WebClient webClient = getWebClient(officeProperties);

        Class<T> responseType = castToClass(getClassForName(officeProperties.getResponseClass()));

        return webClient.get().uri(officeProperties.getUriGet(), id).retrieve().bodyToMono(responseType);
    }

    public <T>Mono<T> postUser(String office, T body) {
        ServiceProperties.OfficeProperties officeProperties = serviceProperties.getOffices().get(office);

        if (officeProperties == null) {
            return Mono.error(new IllegalArgumentException("Office not found"));
        }

        WebClient webClient = getWebClient(officeProperties);

        Class<T> responseType = castToClass(getClassForName(officeProperties.getResponseClass()));

        return webClient.post().uri(officeProperties.getUriPost()).bodyValue(body).retrieve().bodyToMono(responseType);
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

    @SuppressWarnings("unchecked")
    private <T> Class<T> castToClass(Class<?> clazz) {
        return (Class<T>) clazz;
    }
}
