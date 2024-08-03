package net.zengfx.integrationlayer;

import net.zengfx.integrationlayer.models.People;
import net.zengfx.integrationlayer.models.Person;
import net.zengfx.integrationlayer.services.WebServiceRegistryOffice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
class IntegrationLayerApplicationTests {

    @Autowired
    private WebServiceRegistryOffice webServiceRegistryOffice;

    private static final Logger logger = LoggerFactory.getLogger(IntegrationLayerApplicationTests.class);


    @Test
    void contextLoads() {
        // Test di caricamento del contesto
    }

    @Test
    void getTest() {
        Mono<?> response = webServiceRegistryOffice.getUser("A", 1);
        Person person = response.cast(Person.class).block();
        System.out.println(person);
        assert person != null;

        response = webServiceRegistryOffice.getUser("B", 1);
        People people = response.cast(People.class).block();
        System.out.println(people);
        assert people != null;
    }

    @Test
    void postTest() {
        // Creazione di un nuovo oggetto Person
        Person person = new Person();
        person.setName("John Doe");
        person.setAddress("123 Main St");
        person.setEmail("j@doe.net");
        person.setPhone("555-1212");

        // Effettua la chiamata POST per creare un nuovo Person

        Mono<?> postResponse = webServiceRegistryOffice.postUser("A", person);

        // Utilizza block per garantire il completamento e stampare il risultato
        Person postedPerson = postResponse.cast(Person.class).block();
        logger.info("Posted Person: {}", postedPerson);
        assert postedPerson != null && postedPerson.getId() > 0;

        // Effettua la chiamata GET per recuperare il Person con l'ID ottenuto
        Mono<?> getResponse = webServiceRegistryOffice.getUser("A", postedPerson.getId());
        Person gottenPerson = getResponse.cast(Person.class).block();
        logger.info("Gotten Person: {}", gottenPerson);

        // Verifica che l'oggetto ottenuto sia uguale a quello creato
        assert gottenPerson != null;
        assert gottenPerson.getId() == postedPerson.getId();
        assert gottenPerson.getName().equals(postedPerson.getName());
        assert gottenPerson.getAddress().equals(postedPerson.getAddress());
        assert gottenPerson.getEmail().equals(postedPerson.getEmail());
        assert gottenPerson.getPhone().equals(postedPerson.getPhone());
    }
}
