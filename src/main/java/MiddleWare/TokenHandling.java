package MiddleWare;

import Models.Request;
import Models.RequestType;
import Models.User;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class TokenHandling {
    public static HashMap<String, User> tokens = new HashMap<>();
    public static String generateSafeToken(User user) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        tokens.put(token, user);
        return token;
    }
    public static void HandleToken(Request request) {
        if (request.requestType == RequestType.SignIn ||
                request.requestType == RequestType.SignUp)
            return;
        request.user = tokens.get(request.token);
    }

}
