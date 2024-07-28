package net.mat0u5.variablebrightness.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.mat0u5.variablebrightness.Main;
import net.mat0u5.variablebrightness.events.Events;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;


public class VariableBrightness {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("variable-brightness")
                .then(ClientCommandManager.literal("reload")
                        .executes(VariableBrightness::executeReload))
                .then(ClientCommandManager.literal("enable")
                        .executes(context -> VariableBrightness.executeEnableOrDisable("true")))
                .then(ClientCommandManager.literal("disable")
                        .executes(context -> VariableBrightness.executeEnableOrDisable("false")))
                .then(ClientCommandManager.literal("setDefaultBrightness")
                    .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0,100))
                        .executes(context -> VariableBrightness.executeSetConfig(
                                "default_brightness",
                                IntegerArgumentType.getInteger(context, "value"))
                        )
                    )
                )
                .then(ClientCommandManager.literal("setTransitionTicks")
                        .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(1))
                                .executes(context -> VariableBrightness.executeSetConfig(
                                        "transition_ticks",
                                        IntegerArgumentType.getInteger(context, "value"))
                                )
                        )
                )
                .then(ClientCommandManager.literal("setPositionCheckTicks")
                        .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(1))
                                .executes(context -> VariableBrightness.executeSetConfig(
                                        "position_check_ticks",
                                        IntegerArgumentType.getInteger(context, "value"))
                                )
                        )
                )

        );

    }

    private static int executeReload(CommandContext<FabricClientCommandSource> fabricClientCommandSourceCommandContext) {
        Main.reloadConfig();
        MinecraftClient.getInstance().player.sendMessage(Text.literal("Brightness configuration reloaded."), false);
        return 1;
    }
    private static int executeEnableOrDisable(String enableOrDisable) {
        Main.config.setProperty("enabled",enableOrDisable);
        Main.reloadConfig();
        return 1;
    }
    private static int executeSetConfig(String type, int value) {
        Main.config.setProperty(type,String.valueOf(value));
        Main.reloadConfig();
        return 1;
    }
}
