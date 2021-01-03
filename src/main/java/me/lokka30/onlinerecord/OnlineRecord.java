package me.lokka30.onlinerecord;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class OnlineRecord extends JavaPlugin {

    /**
     * This is the number of players of the highest peak, it is stored in memory to minimise disk usage.
     */
    public int currentRecord;

    /**
     * This is ran when the plugin starts up.
     */
    @Override
    public void onEnable() {
        // Load config + set appropriate options
        getLogger().info("Loading config...");
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().header("OnlineRecord by lokka30");

        // Save the peak in memory to minimise disk usage
        getLogger().info("Storing current record to memory...");
        currentRecord = getConfig().getInt("data.currentRecord");

        // Register the listener to put the plugin into effect
        getLogger().info("Registering listeners...");
        getServer().getPluginManager().registerEvents(new Listeners(this), this);

        // Register the command
        getLogger().info("Registering commands...");
        Objects.requireNonNull(getCommand("onlinerecord")).setExecutor(new Commands(this));

        getLogger().info("Start-up complete.");
    }

    /**
     * Translates all standard Minecraft colour codes (e.g. &a = lime green) in the specified message.
     *
     * @param msg the message to translate
     * @return the translated message
     */
    public String colorize(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * Sends a debug message to console if the 'debug' option is enabled in the configuration.
     *
     * @param msg the message to send if debugging is enabled.
     */
    public void debugMessage(final String msg) {
        if (getConfig().getBoolean("debug")) {
            getLogger().info(colorize("&9[DEBUG] &7" + msg));
        }
    }
}
