package no.ntnu.team5.minvakt.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Created by Harald Floor Wilhelmsen on 17.01.2017.
 */
@Service
public class ContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public ContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Takes the given template and injects the given variables
     *
     * @param templateName Name (path) of template
     * @param variables    A map containing the variables to be used in the template
     * @return The completed content
     */
    public String build(String templateName, Map<String, String> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);
        return templateEngine.process(templateName, context);
    }

}
