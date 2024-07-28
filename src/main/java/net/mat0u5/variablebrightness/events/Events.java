package net.mat0u5.variablebrightness.events;



import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class Events {
    public static int TRANSITION_TIME = 0;
    public static int CHECK_DELAY = 0;
    public static boolean enabled = false;
    private static int current_delay = 0;
    public static int default_brightness = 0;

    private static double targetGamma = 0.01;
    private static double currentGamma = 0.01;
    private static int transitionTicksRemaining = 0;
    public static LinkedHashMap<List<String>, Integer> brightnessMap = new LinkedHashMap<>();

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(Events::onClientTickEnd);
    }

    public static void onClientTickEnd(MinecraftClient client) {
        try {
            checkPosition(client);
            handleTransitions();
        }catch (Exception e) {e.printStackTrace();}
    }
    public static void checkPosition(MinecraftClient client) {
        if (!enabled || CHECK_DELAY == 0) return;
        current_delay++;
        if (current_delay < CHECK_DELAY) return;
        current_delay = 0;
        if (client.player == null) return;
        BlockPos playerPos = client.player.getBlockPos();
        Integer brightness = getBrightnessForPosition(playerPos);
        if (brightness != null) {
            startGammaTransition(brightness / 100.0);
        }
    }
    public static Integer getBrightnessForPosition(BlockPos pos) {
        if (brightnessMap == null) return null;
        if (brightnessMap.isEmpty()) return null;
        for (Object key : brightnessMap.keySet()) {
            List<String> bounds = new ArrayList<>();
            if (key instanceof List) {
                bounds = (List<String>) key;
            }
            if (key instanceof String) {
                String[] split = ((String)key).replaceAll("\\[","").replaceAll("]","").replaceAll(", ",",").split(",");
                bounds = List.of(split);
            }
            if (isWithinBounds(pos, bounds)) {
                Object value = brightnessMap.get(key);
                if (value instanceof Integer) {
                    return (Integer) value;
                }
                if (value instanceof Double) {
                    return (int) Math.floor((Double) value);
                }
            }
        }
        return default_brightness;
    }
    public static boolean isWithinBounds(BlockPos pos, List<String> bounds) {
        String[] coords1 = bounds.get(0).split(" ");
        String[] coords2 = bounds.get(1).split(" ");

        int x1 = Integer.parseInt(coords1[0]);
        int y1 = Integer.parseInt(coords1[1]);
        int z1 = Integer.parseInt(coords1[2]);
        int x2 = Integer.parseInt(coords2[0]);
        int y2 = Integer.parseInt(coords2[1]);
        int z2 = Integer.parseInt(coords2[2]);

        // Determine the minimum and maximum values for each coordinate
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        return pos.getX() >= minX && pos.getX() <= maxX &&
                pos.getY() >= minY && pos.getY() <= maxY &&
                pos.getZ() >= minZ && pos.getZ() <= maxZ;
    }
    public static void handleTransitions() {
        if (transitionTicksRemaining <= 0) return;
        double step = (targetGamma - currentGamma) / transitionTicksRemaining;
        currentGamma += step;
        MinecraftClient.getInstance().options.getGamma().setValue(currentGamma);
        transitionTicksRemaining--;
        if (transitionTicksRemaining == 0) {
            MinecraftClient.getInstance().options.getGamma().setValue(targetGamma);
        }
    }
    public static void startGammaTransition(double newGamma) {
        double currentValue = MinecraftClient.getInstance().options.getGamma().getValue();
        if (currentValue == newGamma || transitionTicksRemaining > 0) return;
        targetGamma = newGamma;
        currentGamma = currentValue;
        transitionTicksRemaining = TRANSITION_TIME;
    }

}
