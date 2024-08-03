package net.zengfx.integrationlayer.services;

import lombok.RequiredArgsConstructor;
import net.zengfx.integrationlayer.config.ServiceProperties;
import net.zengfx.integrationlayer.models.User;
import net.zengfx.integrationlayer.models.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebServiceRegistryOffice {

    private final ServiceProperties serviceProperties;

    public Mono<UserResponse> postUser(String officeWebService, User body) {
        WebClient webClient = createWebClient(officeWebService);
        ServiceProperties.OfficeProperties officeProperties = serviceProperties.getOffices().get(officeWebService);
        Class<UserResponse> responseType = castToClass(getClassForName(officeProperties.getResponseClass()));

        return webClient.post().uri(officeProperties.getUriPost()).bodyValue(body).retrieve().bodyToMono(responseType);
    }

    public Mono<UserResponse> getUser(String officeWebService, int id) {
        WebClient webClient = createWebClient(officeWebService);
        ServiceProperties.OfficeProperties officeProperties = serviceProperties.getOffices().get(officeWebService);
        Class<UserResponse> responseType = castToClass(getClassForName(officeProperties.getResponseClass()));

        return webClient.get().uri(officeProperties.getUriGet(), id).retrieve().bodyToMono(responseType);
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
