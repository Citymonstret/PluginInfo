package com.cyandev.plugin.plugininfo;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Created 2015-03-19 for PluginInfo
 *
 * @author Citymonstret
 */
public class PluginInfo extends JavaPlugin {

    public static final String SUPPORTED_VERSION = "v1_8_R2";

    @Override
    public void onEnable() {
        if (checkVersion()) {
            getLogger().log(Level.SEVERE, "Unsupported version - The plugin only supports: " + SUPPORTED_VERSION);
            getPluginLoader().disablePlugin(this);
        } else {
            setupConfig();
            getCommand("plinfo").setExecutor(new Command());
            if (getConfig().getBoolean("list.replace")) {
                Bukkit.getPluginManager().registerEvents(new Listener(), this);
            }
        }
    }

    private void setupConfig() {
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        config.options().copyHeader(true);
        config.options().header("PluginInfo> Configuration");
        config.addDefault("list.replace", true);
        saveConfig();
        reloadConfig();
    }

    protected boolean checkVersion() {
        try {
            Class.forName("net.minecraft.server.%s.IChatBaseComponent".replaceFirst("%s", SUPPORTED_VERSION), false, null);
        } catch(final Exception e) {
            return false;
        }
        return true;
    }
}
