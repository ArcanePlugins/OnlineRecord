package me.lokka30.onlinerecord;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commands implements TabExecutor {

    /**
     * Access to the main class.
     */
    private final OnlineRecord instance;

    public Commands(final OnlineRecord instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        instance.debugMessage("Command ran.");

        if (sender.hasPermission("onlinerecord.command")) {
            if (args.length == 0) {
                instance.getConfig().getStringList("messages.help").forEach(message -> sender.sendMessage(instance.colorize(message)));
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("onlinerecord.command.reload")) {
                        instance.getConfig().getStringList("messages.reload.start").forEach(message -> sender.sendMessage(instance.colorize(message)));

                        instance.saveDefaultConfig(); // In case they've decided to delete it whilst the plugin is running.
                        instance.reloadConfig(); // Reload the config now

                        instance.getConfig().getStringList("messages.reload.finish").forEach(message -> sender.sendMessage(instance.colorize(message)));

                        instance.debugMessage("Reloaded config.");
                    } else {
                        instance.getConfig().getStringList("messages.no-permission").forEach(message -> sender.sendMessage(instance.colorize(message)));
                    }
                } else {
                    instance.getConfig().getStringList("messages.usage").forEach(message -> sender.sendMessage(instance.colorize(message)));
                }
            } else {
                instance.getConfig().getStringList("messages.usage").forEach(message -> sender.sendMessage(instance.colorize(message)));
            }
        } else {
            instance.getConfig().getStringList("messages.no-permission").forEach(message -> sender.sendMessage(instance.colorize(message)));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        instance.debugMessage("Tab completions ~ fetching");

        if (args.length == 0) {
            instance.debugMessage("Tab completions ~ args.loength == 0");
            return Collections.singletonList("reload");
        }

        instance.debugMessage("Tab completions ~ none");
        return new ArrayList<>();
    }
}