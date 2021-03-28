package org.hoohacks;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CredentialFactory {
    private static final String CREDENTIAL_NAME = "speakeasy-gcp-tts.key.json";

    private static InputStream getCredentialsInputStream() {
        var in = CredentialFactory.class.getClassLoader()
                .getResourceAsStream(CREDENTIAL_NAME);
        if (in != null) {
            System.out.println("found in classpath!");
            return in;
        } else {
            try {
                System.out.println("found in file!");
                return new FileInputStream(CREDENTIAL_NAME);
            } catch (FileNotFoundException e) {
                return null;
            }
        }
    }

    public static GoogleCredentials getCredentials() {
        try(var in = getCredentialsInputStream()) {
            if (in == null) {
                System.err.println("No credentials found! Exiting...");
                System.exit(1);
            }
            return ServiceAccountCredentials.fromStream(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
