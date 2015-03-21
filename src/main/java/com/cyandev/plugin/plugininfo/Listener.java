package com.cyandev.plugin.plugininfo;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

/**
 * Created 2015-03-19 for PluginInfo
 *
 * @author Citymonstret
 */
public class Listener implements org.bukkit.event.Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandEvent(final PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("plinfo.use") && Arrays.asList("pl", "plugins").contains(event.getMessage().replace("/", "").split(" ")[0])) {
            event.setMessage("/plinfo list");
        }
    }

}
