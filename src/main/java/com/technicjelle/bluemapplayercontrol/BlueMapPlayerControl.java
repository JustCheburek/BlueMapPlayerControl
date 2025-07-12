package com.technicjelle.bluemapplayercontrol;

import com.technicjelle.UpdateChecker;
import com.technicjelle.bluemapplayercontrol.commands.BMPC;
import com.technicjelle.bluemapplayercontrol.commands.BMPCReload;
import de.bluecolored.bluemap.api.BlueMapAPI;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlueMapPlayerControl extends JavaPlugin {
	UpdateChecker updateChecker;
	BMPC executor;
	private Config config;

	@Override
	public void onEnable() {
		getLogger().info("BlueMapPlayerControl enabled");

		// Загрузка конфигурации
		this.config = new Config(this);

		new Metrics(this, 18378);

		updateChecker = new UpdateChecker("TechnicJelle", "BlueMapPlayerControl", getDescription().getVersion());
		updateChecker.checkAsync();

		BlueMapAPI.onEnable(api -> updateChecker.logUpdateMessage(getLogger()));

		// Регистрация команды bmpc
		PluginCommand bmpc = Bukkit.getPluginCommand("bmpc");
		executor = new BMPC(this);
		if(bmpc != null) {
			bmpc.setExecutor(executor);
			bmpc.setTabCompleter(executor);
		} else {
			getLogger().warning("bmpc is null. This is not good");
		}
		
		// Регистрация команды bmpcreload
		PluginCommand bmpcReload = Bukkit.getPluginCommand("bmpcreload");
		if(bmpcReload != null) {
			bmpcReload.setExecutor(new BMPCReload(this));
		} else {
			getLogger().warning("bmpcreload is null. This is not good");
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("BlueMapPlayerControl disabled");
	}
	
	public Config getPluginConfig() {
		return this.config;
	}
}
