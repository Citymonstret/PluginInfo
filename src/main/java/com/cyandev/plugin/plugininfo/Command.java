package com.cyandev.plugin.plugininfo;

import com.cyandev.plugin.plugininfo.builder.ChatMessage;
import com.cyandev.plugin.plugininfo.builder.ClickEvent;
import com.cyandev.plugin.plugininfo.builder.HoverEvent;
import com.cyandev.plugin.plugininfo.builder.Part;
import com.cyandev.plugin.plugininfo.util.StringComparison;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.*;

/**
 * Created 2015-03-19 for PluginInfo
 *
 * @author Citymonstret
 */
public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("The plugin is currently not usable by the console, pardon me!");
            return true;
        }
        final boolean argsEmpty = args == null || args.length == 0;
        final Player player = (Player) commandSender;
        if (argsEmpty) {
            new ChatMessage("PluginInfo> ")
                    .color(ChatColor.DARK_RED)
                    .format(ChatColor.BOLD)
                    .with(
                            new Part("&lHelp Menu", ChatColor.GOLD)
                    )
                    .with(
                        new Part("\n /plinfo list - List all plugins", ChatColor.GOLD)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to execute: " + ChatColor.GOLD + "/plinfo list"))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/plinfo list"))
                    )
                    .with(
                        new Part("\n /plinfo info <plugin> - Display plugin info", ChatColor.GOLD)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to execute: " + ChatColor.GOLD + "/plinfo info <plugin>"))
                            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/plinfo info "))
                    )
                    .with(
                            new Part("\n /plinfo disable <plugin> - Disable a plugin", ChatColor.GOLD)
                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to execute: " + ChatColor.GOLD + "/plinfo disable <plugin>"))
                                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/plinfo disable "))
                    )
                    .with(
                            new Part("\n /plinfo enable <plugin> - Enable a plugin", ChatColor.GOLD)
                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to execute: " + ChatColor.GOLD + "/plinfo enable <plugin>"))
                                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/plinfo enable "))
                    )
                    .send(player);
        } else {
            if (!(player.hasPermission("plinfo." + args[0].toLowerCase()))) {
                new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED)
                        .format(ChatColor.BOLD)
                        .with(
                                new Part("You're not permitted to use that command", ChatColor.RED)
                        ).send(player);
                    return true;
            }
            switch(args[0].toLowerCase()) {
                case "list": {
                    List<Plugin> plugins = Arrays.asList(Bukkit.getPluginManager().getPlugins());
                    final ChatMessage message = new ChatMessage("Plugins (" + plugins.size() +"): ");
                    if (plugins.isEmpty()) {
                        message.with(
                                new Part("There are no loaded plugins!", ChatColor.RED)
                        );
                    } else {
                        PluginDescriptionFile d;
                        Plugin p;
                        final Iterator<Plugin> i = plugins.iterator();
                        while (i.hasNext()) {
                            p = i.next();
                            if (i.hasNext()) {
                                d = p.getDescription();
                                message.with(
                                        new Part((p.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + d.getFullName() + ChatColor.WHITE + ", ", ChatColor.WHITE)
                                                .event(
                                                        new HoverEvent(
                                                                HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to show info about the plugin"
                                                        )
                                                )
                                                .event(
                                                        new ClickEvent(
                                                                ClickEvent.Action.RUN_COMMAND, "/plinfo info " + p.getName().toLowerCase()
                                                        )
                                                )
                                );
                            } else {
                                d = p.getDescription();
                                message.with(
                                        new Part((p.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + d.getFullName(), ChatColor.WHITE)
                                                .event(
                                                        new HoverEvent(
                                                                HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to show info about the plugin"
                                                        )
                                                )
                                                .event(
                                                        new ClickEvent(
                                                                ClickEvent.Action.RUN_COMMAND, "/plinfo info " + p.getName().toLowerCase()
                                                        )
                                                )
                                );
                            }
                        }
                    }
                    message.send(player);
                }
                    break;
                case "info": {
                    if (args.length > 1) {
                        Plugin p = null;
                        try {
                            p = (Plugin) new StringComparison(args[1], Bukkit.getPluginManager().getPlugins()).getMatchObject();
                        } catch(final Exception e) {
                            new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                                    .with(
                                            new Part("Found no plugin even remotely close to '&6" + args[1] + "&c'", ChatColor.RED)
                                    ).send(player);
                            break;
                        }
                        final PluginDescriptionFile d = p.getDescription();
                        new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                        .with(
                                new Part((p.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + p.getName())
                        ).with(
                                new Part("\n &cFull Name: &6" + d.getFullName())
                        ).with(
                                new Part("\n &cAuthor: &6" + join(d.getAuthors(), ", ").replace("[", "").replace("]", ""))
                        ).with(
                                new Part("\n &cMain File: &6" + d.getMain())
                        ).with(
                                new Part("\n &cWebsite: &6" + d.getWebsite())
                        ).with(
                                new Part("\n &cCommands: &6" + getCommandInfo(d))
                        ).with(
                                new Part("\n &cEnabled: " + (p.isEnabled() ? ChatColor.GREEN + "YES" : ChatColor.RED + "NO"))
                        ).with(
                                p.isEnabled() ?
                                        new Part(" &lDisable", ChatColor.RED)
                                        .event(
                                                new HoverEvent(
                                                        HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to disable the plugin"
                                                )
                                        )
                                        .event(
                                                new ClickEvent(
                                                        ClickEvent.Action.RUN_COMMAND, "/plinfo disable " + p.getName()
                                                )
                                        )
                                :
                                        new Part(" &lEnable", ChatColor.GREEN)
                                        .event(
                                                new HoverEvent(
                                                        HoverEvent.Action.SHOW_TEXT, ChatColor.GREEN + "Click to enable the plugin"
                                                )
                                        )
                                        .event(
                                                new ClickEvent(
                                                        ClickEvent.Action.RUN_COMMAND, "/plinfo enable " + p.getName()
                                                )
                                        )
                        ).send(player);
                    }
                }
                    break;
                case "enable": {
                    Plugin p = null;
                    if (args.length < 2) {
                        new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                                .with(
                                        new Part("You have to provide a plugin name!", ChatColor.RED)
                                ).send(player);
                        break;
                    }
                    try {
                        p = (Plugin) new StringComparison(args[1], Bukkit.getPluginManager().getPlugins()).getMatchObject();
                    } catch(final Exception e) {
                        new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                                .with(
                                        new Part("Found no plugin even remotely close to '&6" + args[1] + "&c'", ChatColor.RED)
                                ).send(player);
                        break;
                    }
                    if (p.isEnabled()) {
                        new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                                .with(
                                        new Part("The plugin is already enabled", ChatColor.RED)
                                ).send(player);
                    } else {
                        String error = "";
                        try {
                            Bukkit.getPluginManager().enablePlugin(p);
                        } catch(final Exception e) {
                            error = e.getMessage();
                            e.printStackTrace();
                        }
                        new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                                .with(
                                        new Part("Enabled " + p.getName() + (error.length() == 0 ? ", no problems occurred!" : ", an error occurred: &c" + error), ChatColor.GREEN)
                                ).send(player);
                    }
                }
                    break;
                case "disable": {
                    if (args.length < 2) {
                        new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                                .with(
                                        new Part("You have to provide a plugin name!", ChatColor.RED)
                                ).send(player);
                        break;
                    }
                    Plugin p = null;
                    try {
                        p = (Plugin) new StringComparison(args[1], Bukkit.getPluginManager().getPlugins()).getMatchObject();
                    } catch(final Exception e) {
                        new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                                .with(
                                        new Part("Found no plugin even remotely close to '&6" + args[1] + "&c'", ChatColor.RED)
                                ).send(player);
                        break;
                    }
                    if (!p.isEnabled()) {
                        new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                                .with(
                                        new Part("The plugin is already disabled", ChatColor.RED)
                                ).send(player);
                    } else {
                        if (p.getName().equalsIgnoreCase("plugininfo")) {
                            new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                                    .with(new Part("Well, good luck enabling me again...", ChatColor.RED))
                                    .send(player);
                        }
                        String error = "";
                        try {
                            Bukkit.getPluginManager().disablePlugin(p);
                        } catch(final Exception e) {
                            error = e.getMessage();
                            e.printStackTrace();
                        }
                        new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD)
                                .with(
                                        new Part("Disabled " + p.getName() + (error.length() == 0 ? ", no problems occurred!" : ", an error occurred: &c" + error), ChatColor.GREEN)
                                ).send(player);
                    }
                }
                    break;
                default:
                    new ChatMessage("PluginInfo> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD).with(new Part("Unknown subcommand", ChatColor.RED)).send(player);
                    break;
            }
        }
        return true;
    }

    protected String join(final Collection e, String s) {
        final Iterator i = e.iterator();
        final StringBuilder b = new StringBuilder();
        while (i.hasNext()) {
            String n = i.next().toString();
            if (i.hasNext()) {
                b.append(n).append(s);
            } else {
                b.append(n);
            }
        }
        return b.toString();
    }

    protected String getCommandInfo(final PluginDescriptionFile d) {
        StringBuilder b = new StringBuilder();

        if (d.getCommands() != null) {
            for (final Map.Entry<String, Map<String, Object>> m : d.getCommands().entrySet()) {
                b.append("\n").append("   &cCommand: &6/").append(m.getKey());
                for (String s : m.getValue().keySet()) {
                    b.append("\n     &c").append(s).append("&6: ").append(m.getValue().get(s));
                }
            }
        }
        return b.toString();
    }
}
