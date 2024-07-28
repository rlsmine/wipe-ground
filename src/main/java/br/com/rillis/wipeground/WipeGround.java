package br.com.rillis.wipeground;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class WipeGround extends JavaPlugin {

    public static JavaPlugin plugin;
    public static Configuration config;

    @Override
    public void onEnable() {
        System.out.println("[WipeGround] Starting");
        plugin = this;

        config = new Configuration("config.yml", this);

        FloorLoop.init(config.getString("message", false));
        FloorLoop.startLoop(config.getInt("time-warn", false), config.getInt("time-between-clears", false));
    }

    @Override
    public void onDisable() {
        System.out.println("[WipeGround] Shutdown");
    }
}
