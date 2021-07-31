import APIs.Authenticate;
import APIs.MatchMaking;
import DataBase.GsonHandler;
import MiddleWare.MiddleWareHandler;
import MiddleWare.Router;
import MiddleWare.TokenHandling;
import Models.Request;
import StreamHandler.StreamHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ClientAgent extends Thread{
    private static final Logger logger = LogManager.getLogger(ClientAgent.class);
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    Router router;
    Authenticate authenticate;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    MiddleWareHandler middleWareHandler;
    public ClientAgent(Socket socket) {
        this.socket = socket;

        start();
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
        } catch (IOException ioException) {
            logger.error("Couldn't get Socket " + socket + " inputStream");
        }
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException ioException) {
            logger.error("Couldn't get Socket " + socket + " outputStream");
        }
        dataInputStream = new DataInputStream(inputStream);
        dataOutputStream = new DataOutputStream(outputStream);
        authenticate = new Authenticate(dataOutputStream);

        router = new Router(authenticate, dataOutputStream);
        middleWareHandler = new MiddleWareHandler(router, new TokenHandling());
        while (true) {
            Request request = StreamHandler.getRequest(dataInputStream);
            if (request == null)
                return;
            request.dataOut = dataOutputStream;
            request.dataIn = dataInputStream;
            middleWareHandler.run(request);
        }
    }
}
