package no.ntnu.team5.minvakt;

import no.ntnu.team5.minvakt.controllers.HelloController;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = HelloController.class)
@EnableAutoConfiguration
public class HelloControllerTest {

}
