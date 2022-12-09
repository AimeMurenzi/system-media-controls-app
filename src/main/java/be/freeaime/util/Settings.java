/**
 * @Author: Aimé
 * @Date:   2022-12-08 00:24:11
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-08 01:05:45
 */
 
package be.freeaime.util;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//@SuppressWarnings("unchecked")
public class Settings implements Serializable { 
    private final String settingFileName = "settings.ser";
    private final String appName = "smc";
    private final String settingsFolderName = "settings"; 
    private final String settingsFileLocation; 

    private String serverPort="8080";
    
    private Settings() {
        final Path settingsFolderLocationPath = generateSettingsFolderLocation();
        createFolder(settingsFolderLocationPath); 
        settingsFileLocation = generateSettingFileLocation(settingsFolderLocationPath);
        final Settings settingsMapSaved = openSetting();
        if (settingsMapSaved != null) {
            serverPort=settingsMapSaved.serverPort;
        }
    }

    private static class Holder {
        private static final Settings INSTANCE = new Settings();
    }

    public static Settings getInstance() {
        return Holder.INSTANCE;
    } 
    private String generateSettingFileLocation(Path settingsFolderLocation) {
        return Paths.get(settingsFolderLocation.toString(), settingFileName).toString();
    }
 

    private Path generateSettingsFolderLocation() {
        return Paths.get(//
                System.getProperty("user.home"), //
                settingsFolderName, //
                appName);
    }

    private Settings openSetting() {
        try {
            if (Files.isRegularFile(Paths.get(settingsFileLocation))) {
                return ((Settings) BasicTool.openObjectFile(settingsFileLocation));
            }
        } catch (IOException e) {
            System.out.println("IOException While Opening UserManager");
        }
        return null;
    }

    private void createFolder(Path settingsFolderLocation) {
        if (!Files.isDirectory(settingsFolderLocation)) {
            try {
                Files.createDirectories(settingsFolderLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveSettings() {
        try {
            BasicTool.saveObjectToFile(this, settingsFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getServerPort() {
        return getInstance().serverPort;
    }
    public static void saveServerPort(String port) {
        getInstance().serverPort=port;
        getInstance().saveSettings();
    }
    
}
