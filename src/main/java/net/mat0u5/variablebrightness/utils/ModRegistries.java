package net.mat0u5.variablebrightness.utils;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.mat0u5.variablebrightness.command.VariableBrightness;
import net.mat0u5.variablebrightness.events.Events;

public class ModRegistries {
    public static void registerModStuff() {
        registerCommands();
        registerEvents();
    }
    private static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            VariableBrightness.register(dispatcher);
        });
    }
    private static void registerEvents() {
        Events.register();
    }
}
