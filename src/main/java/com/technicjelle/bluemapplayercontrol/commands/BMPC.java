package com.technicjelle.bluemapplayercontrol.commands;

import com.technicjelle.bluemapplayercontrol.BlueMapPlayerControl;
import com.technicjelle.bluemapplayercontrol.Config;
import de.bluecolored.bluemap.api.BlueMapAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class BMPC implements CommandExecutor, TabCompleter {

	private final BlueMapPlayerControl plugin;
	private final Config config;

	public BMPC(BlueMapPlayerControl plugin) {
		this.plugin = plugin;
		this.config = plugin.getPluginConfig();
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (BlueMapAPI.getInstance().isPresent()) {
			BlueMapAPI api = BlueMapAPI.getInstance().get();

			// === SELF ===
			if (sender instanceof Player player) { // only players can self
				UUID senderUUID = player.getUniqueId();
				if (args.length == 0) {
					if (!selfAllowed(sender)) {
						sender.sendMessage(config.getSelfNoPermissionMessage());
						return true;
					}
					//toggle
					if (api.getWebApp().getPlayerVisibility(senderUUID)) {
						hideSelf(api, sender, senderUUID);
					} else {
						showSelf(api, sender, senderUUID);
					}
					return true;
				}
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("show")) {
						if (!selfAllowed(sender)) {
							sender.sendMessage(config.getSelfNoPermissionMessage());
							return true;
						}
						showSelf(api, sender, senderUUID);
						return true;
					} else if (args[0].equalsIgnoreCase("hide")) {
						if (!selfAllowed(sender)) {
							sender.sendMessage(config.getSelfNoPermissionMessage());
							return true;
						}
						hideSelf(api, sender, senderUUID);
						return true;
					}
				}
			} else {
				if (args.length == 0) {
					sender.sendMessage(config.getMustBePlayerMessage());
					return true;
				}
			}

			// === OTHER ===
			if (!othersAllowed(sender)) {
				sender.sendMessage(config.getOthersNoPermissionMessage());
			} else {
				String targetName = args[args.length - 1];
				List<Entity> targets = Bukkit.selectEntities(sender, targetName);
				if (targets.isEmpty()) {
					sender.sendMessage(config.getPlayerNotFoundMessage(targetName));
					return true;
				}
				for (Entity target : targets) {
					if (!(target instanceof Player targetPlayer)) continue;
					if (args.length == 1) {
						//toggle
						if (api.getWebApp().getPlayerVisibility(targetPlayer.getUniqueId())) {
							hideOther(api, sender, targetPlayer);
						} else {
							showOther(api, sender, targetPlayer);
						}
					} else if (args[0].equalsIgnoreCase("show")) {
						showOther(api, sender, targetPlayer);
					} else if (args[0].equalsIgnoreCase("hide")) {
						hideOther(api, sender, targetPlayer);
					}
				}
			}
			return true;
		}

		return false;
	}

	private void showSelf(BlueMapAPI blueMapAPI, CommandSender sender, UUID senderUUID) {
		blueMapAPI.getWebApp().setPlayerVisibility(senderUUID, true);
		sender.sendMessage(config.getSelfVisibleMessage());
	}

	private void hideSelf(BlueMapAPI blueMapAPI, CommandSender sender, UUID senderUUID) {
		blueMapAPI.getWebApp().setPlayerVisibility(senderUUID, false);
		sender.sendMessage(config.getSelfInvisibleMessage());
	}

	private void showOther(BlueMapAPI api, @NotNull CommandSender sender, Player targetPlayer) {
		api.getWebApp().setPlayerVisibility(targetPlayer.getUniqueId(), true);
		sender.sendMessage(config.getOthersVisibleMessage(targetPlayer.getDisplayName()));
	}

	private void hideOther(BlueMapAPI api, @NotNull CommandSender sender, Player targetPlayer) {
		api.getWebApp().setPlayerVisibility(targetPlayer.getUniqueId(), false);
		sender.sendMessage(config.getOthersInvisibleMessage(targetPlayer.getDisplayName()));
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			if (selfAllowed(sender)) {
				completions.add("show");
				completions.add("hide");
			}
			if (othersAllowed(sender)) {
				for (Player player : sender.getServer().getOnlinePlayers()) {
					completions.add(player.getName());
				}
			}
		}
		if (othersAllowed(sender)) {
			if (sender.getServer().getPlayer(args[0]) == null
					|| args[0].equalsIgnoreCase("show")
					|| args[0].equalsIgnoreCase("hide")
					|| args[0].isBlank()) {
				if (args.length <= 2) {
					for (Player player : sender.getServer().getOnlinePlayers()) {
						completions.add(player.getName());
					}
					completions.add("@a");
					completions.add("@p");
					completions.add("@r");
					completions.add("@s");
				}
			}
		}
		return completions;
	}

	private boolean selfAllowed(CommandSender sender) {
		return sender.isOp() || sender.hasPermission("bmpc.self");
	}

	private boolean othersAllowed(CommandSender sender) {
		return sender.isOp() || sender.hasPermission("bmpc.others");
	}
}
