package net.zengfx.integrationlayer;

import net.zengfx.integrationlayer.services.WebServiceCaller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

@SpringBootTest
class IntegrationLayerApplicationTests {

    @Autowired
    private WebServiceCaller webServiceCaller;

    @Test
    void contextLoads() {
        Mono<?> response =  webServiceCaller.callRegistryOffice("A", 1);
        System.out.println(response.block());
        response =  webServiceCaller.callRegistryOffice("B", 1);
        System.out.println(response.block());
    }

}
