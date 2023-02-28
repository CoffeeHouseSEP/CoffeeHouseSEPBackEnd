package com.sep.coffeemanagement.jwt;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.log.AppLogger;
import com.sep.coffeemanagement.log.LoggerFactory;
import com.sep.coffeemanagement.log.LoggerType;
import java.util.Collections;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleValidation {
  @Value("${gg.client.id}")
  protected String CLIENT_ID;

  protected AppLogger APP_LOGGER = LoggerFactory.getLogger(LoggerType.APPLICATION);

  public Payload validateTokenId(String idTokenString) {
    JsonFactory jsonFactory = new GsonFactory();
    NetHttpTransport transport = new NetHttpTransport();

    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
      transport,
      jsonFactory
    )
      // Specify the CLIENT_ID of the app that accesses the backend:
      .setAudience(Collections.singletonList(CLIENT_ID))
      // Or, if multiple clients access the backend:
      // .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
      .build();

    // (Receive idTokenString by HTTPS POST)

    try {
      GoogleIdToken idToken = verifier.verify(idTokenString);
      if (idToken != null) {
        Payload payload = idToken.getPayload();

        // Print user identifier
        // String userId = payload.getSubject();

        // Get profile information from payload
        // String email = payload.getEmail();
        // boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
        // String name = (String) payload.get("name");
        // String pictureUrl = (String) payload.get("picture");
        // String locale = (String) payload.get("locale");
        // String familyName = (String) payload.get("family_name");
        // String givenName = (String) payload.get("given_name");

        return payload;
        // Use or store profile information
        // ...

      } else {
        throw new InvalidRequestException(new HashMap<>(), "invalid token gg");
      }
    } catch (Exception e) {
      APP_LOGGER.error(e.getMessage());
      throw new InvalidRequestException(new HashMap<>(), "invalid token gg");
    }
  }
}
