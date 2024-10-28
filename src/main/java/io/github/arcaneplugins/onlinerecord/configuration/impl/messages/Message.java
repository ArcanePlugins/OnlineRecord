package io.github.arcaneplugins.onlinerecord.configuration.impl.messages;

import io.github.arcaneplugins.onlinerecord.OnlineRecord;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.arcaneplugins.onlinerecord.misc.DebugCategory.MESSAGE;

public enum Message {

    PREFIX("prefix", false),
    NEW_RECORD_CHAT_MESSAGE("messages.new-record.chat-message", true),
    NEW_RECORD_LOG_MESSAGE("messages.new-record.log-message", true),
    COMMAND_ONLINERECORD_BASE("messages.command.onlinerecord.base", true),
    COMMAND_ONLINERECORD_RELOAD_STARTED("messages.command.onlinerecord.reload.started", true),
    COMMAND_ONLINERECORD_RELOAD_FINISHED("messages.command.onlinerecord.reload.finished", true),
    COMMAND_ONLINERECORD_RELOAD_FAILED("messages.command.onlinerecord.reload.failed", true),
    COMMAND_ONLINERECORD_USAGE("messages.command.onlinerecord.usage", true),
    NO_PERMISSION("messages.no-permission", true)

    ;

    private final String yamlPath;
    private final boolean isList;

    Message(
            final String yamlPath,
            final boolean isList
    ) {
        this.yamlPath = Objects.requireNonNull(yamlPath);
        this.isList = isList;
    }

    private String yamlPath() {
        return Objects.requireNonNull(yamlPath);
    }

    private boolean isList() {
        return isList;
    }

    public void sendTo(
            @NotNull final OnlineRecord plugin,
            @NotNull final CommandSender recipient
    ) {
        sendTo(plugin, recipient, Function.identity());
    }

    public void sendTo(
            @NotNull final OnlineRecord plugin,
            @NotNull final CommandSender recipient,
            @NotNull final Function<String, String> placeholderer
    ) {
        Objects.requireNonNull(plugin);
        Objects.requireNonNull(recipient);
        Objects.requireNonNull(placeholderer);

        plugin.debugLog(
                MESSAGE,
                () -> "Sending message " + name() + " to " + recipient.getName() + ". Is list: " + isList() +
                        ". Contents: " + (isList() ? String.join(", ", list(plugin)) : string(plugin))
        );

        if(isList()) {
            list(plugin)
                    .stream()
                    .map(placeholderer)
                    .map(msg -> ChatColor.translateAlternateColorCodes('&', msg))
                    .forEach(recipient::sendMessage);
        } else {
            recipient.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            placeholderer.apply(
                                    string(plugin)
                            )
                    )
            );
        }
    }

    @NotNull
    public String string(
            @NotNull final OnlineRecord plugin
    ) {
        if(isList()) {
            throw new IllegalStateException("Message '" + name() + "' is list type, but requested string value");
        }

        String str = plugin.messages().string(yamlPath());

        if (this != PREFIX) {
            str = str.replace("%prefix%", PREFIX.string(plugin));
        }

        return str;
    }

    @NotNull
    public List<String> list(
            @NotNull final OnlineRecord plugin
    ) {
        if(!isList()) {
            throw new IllegalStateException("Message '" + name() + "' is string type, but requested list value");
        }

        return plugin.messages().list(yamlPath()).stream()
                .map(str -> this == PREFIX ? str : str.replace("%prefix%", PREFIX.string(plugin)))
                .collect(Collectors.toList());
    }

}
