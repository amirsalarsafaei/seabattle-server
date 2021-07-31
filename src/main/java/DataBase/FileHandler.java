package DataBase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class FileHandler {
    private static final Logger logger = LogManager.getLogger(FileHandler.class);

    public static boolean Exist(String propertyKey, String key) {
        File file = loadFile(propertyKey);
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                if (scan.nextLine().equals(key)) {
                    scan.close();
                    return true;
                }
            }
            scan.close();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            logger.fatal("Config address " + propertyKey + " file doesn't exist!");
        }
        return false;
    }

    //loading one of config files location files that we are sure it exists
    public static File loadFile(String propertyKey) {
        try {
            InputStream input = new FileInputStream("./src/main/resources/config/Addresses.properties");
            Properties properties = new Properties();
            properties.load(input);
            input.close();
            return new File(properties.getProperty(propertyKey));
        } catch (IOException e) {
            e.printStackTrace();
            logger.fatal("Couldn't Load File\n"
                    + "{'Config' : 'Addresses', 'key' : " + propertyKey + "}");
        }
        return null;
    }

    public static String loadLocation(String propertyKey) {
        try {
            InputStream input = new FileInputStream("./src/main/resources/config/Addresses.properties");
            Properties properties = new Properties();
            properties.load(input);
            input.close();
            return properties.getProperty(propertyKey);
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return null;
    }
    //to clear a txt file first we delete it then we will remake it
    public static void ClearFile(File file) {
        if (file.delete()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Couldn't Make File after delete (clearing) : " + file.getAbsolutePath());
            }
        }
        else {
            logger.error("Couldn't delete File for creating (clearing) : " + file.getAbsolutePath());
        }
    }
    //deleting the whole folder
    public static void delete(File file) {
        if (!file.exists())
            return;
        for (File f: file.listFiles())
            delete(f);
        file.delete();
    }
}
