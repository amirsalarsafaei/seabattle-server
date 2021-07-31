package DataBase;

import Models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Load {
    private static final Logger logger = LogManager.getLogger(Load.class);

    public static ArrayList<User> openUsers = new ArrayList<>();
    public static User LoadUser(String username) {
        //if user is open it will just return it
        for (User user: openUsers)
            if (user.getUsername().equals(username))
                return user;
        File dir = FileHandler.loadFile("user.dir");

        if (dir != null) {
            try {
                File userFile = new File(dir.getPath() + "/" + username);
                if (!userFile.exists())
                    return null;
                Reader reader = new FileReader(userFile.getPath());
                User user = GsonHandler.getGson().fromJson(reader, User.class);
                reader.close();
                openUsers.add(user);
                return user;
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
                logger.error("couldn't load User" + username + " File!");
            }
        }
        else {
            logger.fatal("users Dir doesnt exist!");
        }
        return null;
    }

    public static ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        File dir = FileHandler.loadFile("user.dir");
        for (String username:dir.list())
            users.add(LoadUser(username));
        return users;
    }
}
