package me.lokka30.peakplayersrecord;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PeakPlayersRecord extends JavaPlugin {

    int currentPeak;

    @Override
    public void onEnable() {
        // Load config + set appropriate options
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().header("PeakPlayersRecord by lokka30");

        // Save the peak in memory to minimise disk usage
        currentPeak = getConfig().getInt("data.currentPeak");

        // Register the listener to put the plugin into effect
        getServer().getPluginManager().registerEvents(new Listeners(), this);

        // Register the command
        Objects.requireNonNull(getCommand("peakplayersrecord")).setExecutor(new Commands());
    }

    private String colorize(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    class Listeners implements Listener {
        @EventHandler
        public void onJoin(final PlayerJoinEvent event) {
            final int currentlyOnline = Bukkit.getOnlinePlayers().size();

            if (currentlyOnline > currentPeak) {
                // Change peak count.
                currentPeak = currentlyOnline;
                getConfig().set("data.currentPeak", currentPeak);

                // Set player list.
                final ArrayList<String> usernamesOnline = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> usernamesOnline.add(onlinePlayer.getName()));
                getConfig().set("data.playerListAtPeak", String.join(",", usernamesOnline));

                // Log the achievement.
                getLogger().info("New peak player count reached: " + currentPeak + " players are online.");

                // Announce the achievement.
                if (getConfig().getBoolean("messages.announcement.enabled")) {
                    List<String> messages = getConfig().getStringList("messages.announcement.text");
                    messages.forEach(message -> {
                        message = colorize(message);
                        message = message.replace("%players%", currentPeak + "");
                        Bukkit.broadcastMessage(message);
                    });
                }

                // Write changes to the disk
                saveConfig();
            }
        }
    }

    class Commands implements TabExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (sender.hasPermission("peakplayersrecord.command")) {
                if (args.length == 0) {
                    getConfig().getStringList("messages.help").forEach(sender::sendMessage);
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (sender.hasPermission("peakplayersrecord.command.reload")) {
                            getConfig().getStringList("messages.reload.start").forEach(sender::sendMessage);

                            saveDefaultConfig(); // In case they've decided to delete it whilst the plugin is running.
                            reloadConfig(); // Reload the config now

                            getConfig().getStringList("messages.reload.finish").forEach(sender::sendMessage);
                        } else {
                            getConfig().getStringList("messages.no-permission").forEach(sender::sendMessage);
                        }
                    } else {
                        getConfig().getStringList("messages.usage").forEach(sender::sendMessage);
                    }
                } else {
                    getConfig().getStringList("messages.usage").forEach(sender::sendMessage);
                }
            } else {
                getConfig().getStringList("messages.no-permission").forEach(sender::sendMessage);
            }
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length == 0) {
                return Collections.singletonList("reload");
            }
            return null;
        }
    }
}
