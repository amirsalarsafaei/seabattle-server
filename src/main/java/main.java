import DataBase.properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

public class main {
    private static final Logger logger = LogManager.getLogger(main.class);
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(properties.loadServerConfig("port")));
            while(true) {
                new ClientAgent(serverSocket.accept());
            }
        } catch (IOException ioException) {
            logger.fatal("Server Couldn't Open Socket");
        }

    }
}
