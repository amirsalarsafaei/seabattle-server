package APIs;

import DataBase.FileHandler;
import DataBase.GsonHandler;
import DataBase.Load;
import DataBase.Save;
import MiddleWare.TokenHandling;
import Models.*;
import StreamHandler.StreamHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class Authenticate {
    DataOutputStream dataOut;
    public Authenticate(DataOutputStream dataOutputStream) {
        dataOut = dataOutputStream;
    }
    public void SignUp(Request request) {
        BaseUser baseUser = GsonHandler.getGson().fromJson(request.data, BaseUser.class);
        String username = baseUser.username, password = baseUser.password;
        if (FileHandler.Exist("user.usernames", username)) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.InvalidData, "Username exists"));
            return;
        }
        User user = new User(username, password);
        Save.save(user);
        Save.saveUsername(username);
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Created, "Successful"));
    }

    public void SignIn(Request request) {
        BaseUser baseUser = GsonHandler.getGson().fromJson(request.data, BaseUser.class);
        String username = baseUser.username, password = baseUser.password;
        if (!FileHandler.Exist("user.usernames", username)) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.InvalidData, "Username doesn't exist"));
            return;
        }
        User user = Load.LoadUser(username);
        if (user != null && user.password.equals(password)) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, TokenHandling.generateSafeToken(user)));
            return;
        }
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.InvalidData, "Incorrect password"));
    }

    public void lastOnline(Request request) {
        request.user.lastOnline = LocalDateTime.now();
    }
}
