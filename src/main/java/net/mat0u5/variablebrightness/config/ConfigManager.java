package net.mat0u5.variablebrightness.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigManager {

    private Properties properties = new Properties();
    private String filePath;
    private String mapFilePath;
    private Gson gson = new Gson();

    public ConfigManager(String filePath) {
        this.filePath = filePath;
        createFileIfNotExists(filePath);
        loadProperties();
    }
    public ConfigManager(String filePath, String mapFilePath) {
        this.filePath = filePath;
        this.mapFilePath = mapFilePath;
        createFileIfNotExists(filePath);
        createFileIfNotExists(mapFilePath);
        loadProperties();
    }

    private void createFileIfNotExists(String path) {
        File configFile = new File(path);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try  {
                    if (path.contains("brightnessMap")) {
                        String default_config = "\n" +
                                "##############################################################################\n" +
                                "## Please follow this syntax:\t\t\t\t\t\t\t\t\t\t\t\t##\n" +
                                "##\t\t\"[fromX fromY fromZ, toX, toY, toZ]\": brightnessValue\t\t\t\t##\n" +
                                "## Comments need to be surrounded with double # from *both sides*\t\t\t##\n" +
                                "##   \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t##\n" +
                                "## Entries higher up in the map have higher priority over the ones below\t##\n" +
                                "## That means that if the player is in two bounding boxes at once, \t\t\t##\n" +
                                "##  the one written higher up takes priority.\t\t\t\t\t\t\t\t##\n" +
                                "##############################################################################\n" +
                                "\n" +
                                "brightnessMap = {\n" +
                                "\t\"[-448 28 2000, -461 17 1945]\": 0,\t\t## Lvl2 Lava\t##\n" +
                                "\t\"[-587 46 1972, -579 11 1958]\": 100,\t## TNT Dive\t\t##\n" +
                                "\t\"[-639 -18 1906, -643 -30 1902]\": 50,\t## Drop to LVL4 ##\n" +
                                "\t\"[-563 -26 1914, -664 -64 1834]\": 50,\t## Level 4 \t\t##\n" +
                                "\t\"[-575 13 1878, -661 -25 1936]\": 45,\t## Level 3 \t\t##\n" +
                                "\t\"[-447 37 1930, -603 -8 2058]\": 60,\t\t## Level 2 \t\t##\n" +
                                "\t\"[-638 66 2040, -454 36 1941]\": 60,\t\t## Level 1 \t\t##\n" +
                                "\t\"[-560 120 1974, -620 48 1932]\": 55\t\t## Ice Tunnel \t##\n" +
                                "}";
                        setFileContent(path,default_config);
                    }
                    else {
                        try (OutputStream output = new FileOutputStream(configFile)) {
                            properties.setProperty("position_check_ticks","20");
                            properties.setProperty("transition_ticks","60");
                            properties.setProperty("default_brightness","100");
                            properties.setProperty("enabled","true");

                            properties.store(output, null);
                        }
                    }
                }catch (Exception e) {e.printStackTrace();}
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void loadProperties() {
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public String getFileContent() {
        try {
            String everything = "";
            BufferedReader br = new BufferedReader(new FileReader(mapFilePath));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                everything = sb.toString();
            } finally {
                br.close();
            }
            return everything;
        } catch (IOException e1) {}
        return null;
    }
    public void setFileContent(String path, String content) {
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(path, false);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try (OutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
