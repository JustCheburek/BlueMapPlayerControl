package com.technicjelle.bluemapplayercontrol.commands;

import com.technicjelle.bluemapplayercontrol.BlueMapPlayerControl;
import com.technicjelle.bluemapplayercontrol.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BMPCReload implements CommandExecutor {
    private final BlueMapPlayerControl plugin;
    private final Config config;

    public BMPCReload(BlueMapPlayerControl plugin) {
        this.plugin = plugin;
        this.config = plugin.getPluginConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("bmpc.reload")) {
            plugin.getPluginConfig().reloadConfig();
            sender.sendMessage(config.getReloadSuccessMessage());
            return true;
        } else {
            sender.sendMessage(config.getReloadNoPermissionMessage());
            return true;
        }
    }
} 