package com.technicjelle.bluemapplayercontrol;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Config {
    private final BlueMapPlayerControl plugin;
    private FileConfiguration config;

    public Config(BlueMapPlayerControl plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    // Методы для получения сообщений
    public String getSelfVisibleMessage() {
        return ChatColor.translateAlternateColorCodes('§', config.getString("messages.self.now-visible", "Теперь вы §b§lвидимы§r на карте"));
    }

    public String getSelfInvisibleMessage() {
        return ChatColor.translateAlternateColorCodes('§', config.getString("messages.self.now-invisible", "Теперь вы §6§lневидимы§r на карте"));
    }

    public String getSelfNoPermissionMessage() {
        return ChatColor.translateAlternateColorCodes('§', config.getString("messages.self.no-permission", "§cУ вас нет прав для изменения своей видимости на карте"));
    }

    public String getOthersVisibleMessage(String playerName) {
        return ChatColor.translateAlternateColorCodes('§', 
            config.getString("messages.others.now-visible", "%player% теперь §b§lвидим§r на карте")
                .replace("%player%", playerName));
    }

    public String getOthersInvisibleMessage(String playerName) {
        return ChatColor.translateAlternateColorCodes('§', 
            config.getString("messages.others.now-invisible", "%player% теперь §6§lневидим§r на карте")
                .replace("%player%", playerName));
    }

    public String getOthersNoPermissionMessage() {
        return ChatColor.translateAlternateColorCodes('§', config.getString("messages.others.no-permission", "§cУ вас нет прав для изменения видимости других игроков"));
    }

    public String getPlayerNotFoundMessage(String playerName) {
        return ChatColor.translateAlternateColorCodes('§', 
            config.getString("messages.others.player-not-found", "§eИгрок \"%player%\" не найден")
                .replace("%player%", playerName));
    }

    public String getMustBePlayerMessage() {
        return ChatColor.translateAlternateColorCodes('§', config.getString("messages.general.must-be-player", "§cТолько игроки могут скрывать себя на карте"));
    }
    
    public String getReloadSuccessMessage() {
        return ChatColor.translateAlternateColorCodes('§', config.getString("messages.reload.success", "§aКонфигурация BlueMapPlayerControl перезагружена!"));
    }
    
    public String getReloadNoPermissionMessage() {
        return ChatColor.translateAlternateColorCodes('§', config.getString("messages.reload.no-permission", "§cУ вас нет прав для перезагрузки конфигурации!"));
    }
} 