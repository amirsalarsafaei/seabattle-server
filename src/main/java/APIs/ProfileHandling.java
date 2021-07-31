package APIs;

import DataBase.GsonHandler;
import Models.Profile.Profile;
import Models.Request;
import Models.Response;
import Models.ResponseType;
import StreamHandler.StreamHandler;

public class ProfileHandling {
    public void getProfile(Request request) {
        StreamHandler.sendResponse(request.dataOut, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(new Profile(request.user))));
    }
}
