package net.zengfx.integrationlayer.services;

import lombok.RequiredArgsConstructor;
import net.zengfx.integrationlayer.config.ServiceProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebServiceRegistryOffice {

    private final ServiceProperties serviceProperties;

    public <T> Mono<T> getUser(String officeWebService, int id) {
        WebClient webClient = createWebClient(officeWebService);
        ServiceProperties.OfficeProperties officeProperties = serviceProperties.getOffices().get(officeWebService);
        Class<T> responseType = castToClass(getClassForName(officeProperties.getResponseClass()));

        return webClient.get().uri(officeProperties.getUriGet(), id).retrieve().bodyToMono(responseType);
    }

    public <T> Mono<T> postUser(String officeWebService, T body) {
        WebClient webClient = createWebClient(officeWebService);
        ServiceProperties.OfficeProperties officeProperties = serviceProperties.getOffices().get(officeWebService);
        Class<T> responseType = castToClass(getClassForName(officeProperties.getResponseClass()));

        return webClient.post().uri(officeProperties.getUriPost()).bodyValue(body).retrieve().bodyToMono(responseType);
    }

    private WebClient createWebClient(String officeWebService) {
        ServiceProperties.OfficeProperties officeProperties = serviceProperties.getOffices().get(officeWebService);

        if (officeProperties == null) {
            throw new IllegalArgumentException("Office not found");
        }

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
