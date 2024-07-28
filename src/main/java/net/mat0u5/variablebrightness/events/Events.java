package net.mat0u5.variablebrightness.events;



import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.mat0u5.variablebrightness.Main;
import net.mat0u5.variablebrightness.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class Events {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(Events::onClientTickEnd);
    }
    public static void onClientTickEnd(MinecraftClient client) {
        if (client.player != null) {
            System.out.println("test_tick");
            double y = client.player.getY();
            if (y < 0) {
                MinecraftClient.getInstance().options.getGamma().setValue(100.0);
                System.out.println("Gamma set to 100");
            } else {
                MinecraftClient.getInstance().options.getGamma().setValue(1.0);
                System.out.println("Gamma set to 1.0");
            }
        }
    }

}
