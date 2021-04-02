package com.awesome.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class EmailTemplate {
    private String template;

    public EmailTemplate(String templateName) {
        try {
            this.template = loadTemplate(templateName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String loadTemplate(String templateName) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(templateName).getFile());
        String content;

        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new FileNotFoundException("Could not read template with name = " + templateName);
        }

        return content;
    }

    public String getTemplate(Map<String, String> replacements) {
        String emailTemplate = this.template;
        //Replace the String
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            emailTemplate = emailTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return emailTemplate;
    }
}
