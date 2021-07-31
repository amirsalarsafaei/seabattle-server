package DataBase;

import Models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Save {
    private static final Logger logger = LogManager.getLogger(Save.class);

    public static void save(User user) {
        File dir = FileHandler.loadFile("user.dir");
        if (dir != null) {
            try {
                File userFile = new File(dir.getPath() + "/" + user.getUsername());
                if (userFile.exists())
                    FileHandler.ClearFile(userFile);
                else {
                    if (!userFile.getParentFile().exists()) {
                        userFile.getParentFile().mkdir();
                        logger.info("user " + user.getUsername() + " folder Created");
                    }
                    userFile.createNewFile();
                    logger.info("User data file for user " + user.getUsername() + " created.");
                }
                save(user, new PrintStream(new FileOutputStream(userFile)));
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
                logger.error("couldn't save User " + user.getUsername() + " File!");
            }
        }
        else {
            logger.fatal("users Dir doesnt exist!");
        }
    }

    static void save(Object obj, PrintStream print) {
        GsonHandler.getGson().toJson(obj, print);
        print.close();
    }

    public static void saveUsername(String username) {
        File usernameFile = FileHandler.loadFile("user.usernames");
        try {
            PrintStream print = new PrintStream(new FileOutputStream(usernameFile, true));
            print.println(username);
            print.close();
        } catch (IOException e) {
            logger.fatal("Usernames file not Found!");
            e.printStackTrace();
        }
    }
}
