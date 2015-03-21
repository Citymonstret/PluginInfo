package com.cyandev.plugin.plugininfo;

import com.cyandev.plugin.plugininfo.builder.ReflectionSender;
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
    public static boolean NMS_CONTROL = false;

    @Override
    public void onEnable() {
        if (checkVersion()) {
            getLogger().log(Level.WARNING, "Unsupported version - The plugin is built for: " + SUPPORTED_VERSION);
            getLogger().log(Level.WARNING, "Will attempt to use the NMS Control System");
            NMS_CONTROL = true;
        } else {
            setupConfig();
            getCommand("plinfo").setExecutor(new Command());
            if (getConfig().getBoolean("list.replace")) {
                Bukkit.getPluginManager().registerEvents(new Listener(), this);
            }

            if (NMS_CONTROL) {
                ReflectionSender.setup();
                getLogger().log(Level.WARNING, "Using ReflectionSender -> Might cause issues!");
            }
        }
    }

    private void setupConfig() {
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        config.options().copyHeader(true);
        config.options().header("PluginInfo> Configuration");
        config.addDefault("list.replace", true);
        config.addDefault("nmsControl", false);
        saveConfig();
        reloadConfig();
        NMS_CONTROL = config.getBoolean("nmsControl");
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
