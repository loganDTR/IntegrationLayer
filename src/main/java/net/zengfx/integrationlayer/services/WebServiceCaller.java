package net.zengfx.integrationlayer.services;

import lombok.RequiredArgsConstructor;
import net.zengfx.integrationlayer.config.ServiceProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebServiceCaller {

    private final ServiceProperties serviceProperties;

    public Mono<?> callRegistryOffice(String office, int id) {
        ServiceProperties.OfficeProperties officeProperties = serviceProperties.getOffices().get(office);

        if (officeProperties == null) {
            return Mono.error(new IllegalArgumentException("Office not found"));
        }

        WebClient webClient = WebClient.builder().baseUrl(officeProperties.getBaseUrl()).build();

        return webClient.get().uri(officeProperties.getUri(), id).retrieve().bodyToMono(getClassForName(officeProperties.getResponseClass()));
    }

    private Class<?> getClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found", e);
        }
    }
}
