package com.awesome.service.utility.email;

import lombok.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;

@Value
public class EmailAttachment {
     InputStreamSource data;
     String contentType;
     String filename;

    public static EmailAttachment csv(final byte[] content, final String fileName) {
        return create("text/csv", content, fileName);
    }

    private static EmailAttachment create(final String contentType, final byte[] content, final String filename) {
        return new EmailAttachment(new ByteArrayResource(content), contentType, filename);
    }
}
