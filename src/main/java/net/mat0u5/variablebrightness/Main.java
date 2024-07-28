package net.mat0u5.variablebrightness;

import net.fabricmc.api.ModInitializer;

import net.mat0u5.variablebrightness.config.ConfigManager;
import net.mat0u5.variablebrightness.utils.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main implements ModInitializer {
	public static final String MOD_ID = "variablebrightness";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigManager config;

	@Override
	public void onInitialize() {
		config = new ConfigManager("./config/"+MOD_ID+".properties");
		System.out.println("transition_seconds: " + Main.config.getProperty("transition_seconds"));

		ModRegistries.registerModStuff();
		LOGGER.info("Initializing Variable Brightness...");
	}
}