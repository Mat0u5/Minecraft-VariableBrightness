package net.mat0u5.variablebrightness.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mat0u5.variablebrightness.Main;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Optional;


public class Command {

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {

    }

    public static int execute(ServerCommandSource source, Collection<? extends PlayerEntity> targets) throws CommandSyntaxException {
        MinecraftServer server = source.getServer();
        final PlayerEntity self = source.getPlayer();
        self.sendMessage(Text.of("test"));
        return 1;
    }
}
