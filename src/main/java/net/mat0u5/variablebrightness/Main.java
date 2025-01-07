package net.mat0u5.variablebrightness;

import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.ModInitializer;

import net.mat0u5.variablebrightness.config.ConfigManager;
import net.mat0u5.variablebrightness.events.Events;
import net.mat0u5.variablebrightness.utils.ModRegistries;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NbtIo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class Main implements ModInitializer {
	public static final String MOD_ID = "variablebrightness";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigManager config;
	@Override
	public void onInitialize() {
		reloadConfig();
		ModRegistries.registerModStuff();
		LOGGER.info("Initializing Variable Brightness...");
	}
	public static void reloadConfig() {
		config = new ConfigManager("./config/"+MOD_ID+".properties","./config/"+MOD_ID+"-brightnessMap.properties");
		if (config.getProperty("enabled") != null) {
			Events.enabled = config.getProperty("enabled").equalsIgnoreCase("true");
		}
		if (config.getProperty("transition_ticks") != null) {
			try {
				Events.TRANSITION_TIME = Integer.parseInt(config.getProperty("transition_ticks"));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (config.getProperty("position_check_ticks") != null) {
			try {
				Events.CHECK_DELAY = Integer.parseInt(config.getProperty("position_check_ticks"));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (config.getProperty("default_brightness") != null) {
			try {
				Events.default_brightness = Integer.parseInt(config.getProperty("default_brightness"));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			Gson gson = new Gson();
			String mapStr = "{"+config.getFileContent().split("brightnessMap")[1].split("\\{")[1].split("}")[0]+"}";
			String mapStrFormatted = mapStr.replaceAll("\\t","").replaceAll(": ",":").replaceAll("##.+##","");
			List<String> lines = new ArrayList<>();
			for (String line : mapStrFormatted.split("\\r\\n")) {
				lines.add(line.trim());
			}
			Type type = new TypeToken<LinkedHashMap<List<String>, Integer>>() {}.getType();
			LinkedHashMap<List<String>, Integer> brightnessMap = new LinkedHashMap<>();
			Events.brightnessMap = gson.fromJson(String.join("",lines),brightnessMap.getClass());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}