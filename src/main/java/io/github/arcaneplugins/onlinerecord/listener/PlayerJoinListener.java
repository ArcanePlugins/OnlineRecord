package io.github.arcaneplugins.onlinerecord.listener;

import io.github.arcaneplugins.onlinerecord.OnlineRecord;
import io.github.arcaneplugins.onlinerecord.configuration.impl.messages.Message;
import io.github.arcaneplugins.onlinerecord.misc.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.github.arcaneplugins.onlinerecord.misc.DebugCategory.PLAYER_JOIN_LISTENER;

public class PlayerJoinListener implements Listener {

    private final OnlineRecord plugin;

    public PlayerJoinListener(
            @NotNull final OnlineRecord plugin
    ) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @NotNull
    private OnlineRecord plugin() {
        return Objects.requireNonNull(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onJoin(
            @NotNull final PlayerJoinEvent event
    ) {
        plugin().debugLog(PLAYER_JOIN_LISTENER, () -> "PlayerJoinEvent fired for " + event.getPlayer().getName());

        final Collection<? extends Player> online = new LinkedList<>(Bukkit.getOnlinePlayers());

        plugin().debugLog(PLAYER_JOIN_LISTENER, () -> "Online player count (actual): " + online.size());

        if (plugin().config().isGenericVanishCompatibilityEnabled()) {
            online.removeIf(PlayerUtil::isVanished);
        }

        final int onlineCount = online.size();
        final int rec = plugin().data().getCurrentRecordCount();
        plugin().debugLog(PLAYER_JOIN_LISTENER, () -> "Online player count (excluding vanished): " + onlineCount);
        plugin().debugLog(PLAYER_JOIN_LISTENER, () -> "Online player record count: " + rec);

        if (onlineCount <= rec) {
            plugin().debugLog(PLAYER_JOIN_LISTENER, () -> "Record was not broken. Done");
            return;
        }

        plugin().debugLog(PLAYER_JOIN_LISTENER, () -> "Saving new record...");
        plugin().data().saveRecord(
                onlineCount,
                online.stream().collect(
                        Collectors.toMap(
                                p -> p.getUniqueId().toString(),
                                Player::getName
                        )
                )
        );

        // schedule 1 tick delay so it appears after the join message
        Bukkit.getScheduler().runTaskLater(
                plugin(),
                () -> {
                    plugin().debugLog(PLAYER_JOIN_LISTENER, () -> "Logging new record...");
                    if (plugin().config().isRecordAnnouncementLogMessageEnabled()) {
                        Message.NEW_RECORD_LOG_MESSAGE.list(plugin())
                                .stream()
                                .map(str -> str.replace("%player-count%", Integer.toString(onlineCount)))
                                .forEach(plugin().getLogger()::info);
                    }

                    plugin().debugLog(PLAYER_JOIN_LISTENER, () -> "Announcing new record to chat...");
                    if (plugin().config().isRecordAnnouncementChatMessageEnabled()) {
                        Bukkit.getOnlinePlayers().forEach(p -> Message.NEW_RECORD_CHAT_MESSAGE.sendTo(plugin(), p, msg -> msg
                                .replace("%player-count%", Integer.toString(onlineCount))
                                .replace("%players%", Integer.toString(onlineCount)) // legacy
                        ));
                    }
                },
                1L
        );
    }
}
