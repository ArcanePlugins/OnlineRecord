package io.github.arcaneplugins.onlinerecord.misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerUtil {

    private PlayerUtil() {
        throw new IllegalStateException("Utility class");
    }

    // If any online players can't see (Player#canSee) the target player, then we consider them vanished.
    public static boolean isVanished(
            @NotNull final Player target
    ) {
        for(final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(target != onlinePlayer && !onlinePlayer.canSee(target)) {
                return true;
            }
        }

        return false;
    }

}
