package no.ntnu.team5.minvakt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Harald Floor Wilhelmsen on 09.01.2017.
 */
@RestController
public class HelloController {

    @Value("${main.question}")
    String question;

    @Value("${main.answer}")
    String answer;

    @RequestMapping("/hello")
    public String getAnswer(@RequestParam(value = "question", defaultValue = "World") String input) {
        System.out.println("input: '" + input + "'");
        System.out.println("question: '" + question + "'");
        if (input.equals(question)) {
            return answer;
        } else {
            return "Wrong question";
        }
    }
}
