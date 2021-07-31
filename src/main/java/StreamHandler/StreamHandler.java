package StreamHandler;

import DataBase.GsonHandler;
import MiddleWare.Router;
import Models.Request;
import Models.Response;
import Models.ResponseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StreamHandler {
    private static final Logger logger = LogManager.getLogger(StreamHandler.class);
    public static void sendResponse(DataOutputStream dataOut, Response response) {
        try {
            dataOut.writeUTF(GsonHandler.getGson().toJson(response));
            dataOut.flush();
        } catch (IOException ioException) {
            logger.error("Couldn't send a response");
        }
    }

    public static Request getRequest(DataInputStream dataIn) {
        String requestJson = null;
        try {
            requestJson = dataIn.readUTF();
        } catch (IOException ioException) {
            logger.error("Couldn't read a request");
        }
        Request request = GsonHandler.getGson().fromJson(requestJson, Request.class);
        return request;
    }
}
