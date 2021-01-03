package me.lokka30.peakplayersrecord;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class Listeners implements Listener {

    /**
     * Access to the main class.
     */
    private final PeakPlayersRecord instance;

    public Listeners(final PeakPlayersRecord instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        instance.debugMessage("A player has joined.");

        final int currentlyOnline = Bukkit.getOnlinePlayers().size();

        if (currentlyOnline > instance.currentPeak) {
            // Change peak count.
            instance.currentPeak = currentlyOnline;
            instance.getConfig().set("data.currentPeak", instance.currentPeak);

            // Set player list.
            final ArrayList<String> usernamesOnline = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> usernamesOnline.add(onlinePlayer.getName()));
            instance.getConfig().set("data.playerListAtPeak", String.join(", ", usernamesOnline));

            // Log the achievement.
            instance.getLogger().info("New peak player count reached: " + instance.currentPeak + " players are online.");

            // Announce the achievement.
            if (instance.getConfig().getBoolean("messages.announcement.enabled")) {
                List<String> messages = instance.getConfig().getStringList("messages.announcement.text");
                messages.forEach(message -> Bukkit.broadcastMessage(instance.colorize(message).replace("%players%", instance.currentPeak + "")));
            }

            // Write changes to the disk
            instance.saveConfig();

            instance.debugMessage("Finished peak code.");
        }
    }
}
