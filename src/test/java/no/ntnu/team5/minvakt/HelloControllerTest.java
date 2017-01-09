package no.ntnu.team5.minvakt;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = HelloController.class)
@EnableAutoConfiguration
public class HelloControllerTest {

    @Value("${main.question}")
    String question;

    @Value("${main.answer}")
    String answer;

    @Test
    public void testHello() throws Exception {
        System.out.println(question);
        System.out.println(answer);
        String response = new HelloController().getAnswer(question);
        Assert.assertEquals(answer, response);
    }
}
