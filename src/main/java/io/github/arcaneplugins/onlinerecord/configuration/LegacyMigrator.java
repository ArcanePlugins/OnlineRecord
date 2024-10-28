package io.github.arcaneplugins.onlinerecord.configuration;

import io.github.arcaneplugins.onlinerecord.OnlineRecord;
import io.github.arcaneplugins.onlinerecord.configuration.impl.config.Config;
import io.github.arcaneplugins.onlinerecord.configuration.impl.data.Data;
import io.github.arcaneplugins.onlinerecord.configuration.impl.messages.Messages;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Objects;

public class LegacyMigrator {

    private final OnlineRecord plugin;

    public LegacyMigrator(
            @NotNull final OnlineRecord plugin
    ) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @NotNull
    private OnlineRecord plugin() {
        return plugin;
    }

    public void migrate() {
        final Config config = plugin().config();
        final Messages messages = plugin().messages();
        final Data data = plugin().data();

        // don't migrate if legacy config is not being used
        if (!config.isLegacyConfigFile()) {
            return;
        }

        plugin().getLogger().warning("Detected legacy config file. OnlineRecord will migrate this for you.");

        // make backups
        plugin().getLogger().info("Making backups of configuration files to be modified...");
        config.makeBackup();
        messages.makeBackup();
        data.makeBackup();

        // set metadata
        plugin().getLogger().info("Updating metadata...");
        config.cfg().set("metadata.installed-version", 2);
        config.cfg().set("metadata.original-version", 1);
        config.cfg().set("metadata.generated-with", "1.0.0");

        // migrate data
        plugin().getLogger().info("Migrating data...");
        plugin().data().saveLegacyRecord(
                config.cfg().getInt("data.currentRecord", 0),
                config.cfg().getStringList("data.playersOnlineAtRecord")
        );
        plugin().config().cfg().set("data", null);

        // migrate messages
        plugin().getLogger().info("Migrating messages...");
        config.cfg().set(
                "record-announcement.enable-chat-message",
                config.cfg().getBoolean("messages.announcement.enabled", true)
        );
        messages.cfg().set(
                "messages.new-record.chat-message",
                config.cfg().getStringList("messages.announcement.text")
        );
        messages.cfg().set(
                "messages.no-permission",
                config.cfg().getStringList("messages.no-permission")
        );
        messages.cfg().set(
                "messages.command.onlinerecord.base",
                config.cfg().getStringList("messages.help")
        );
        messages.cfg().set(
                "messages.command.onlinerecord.reload.started",
                config.cfg().getStringList("messages.reload.start")
        );
        messages.cfg().set(
                "messages.command.onlinerecord.reload.finished",
                config.cfg().getStringList("messages.reload.finish")
        );
        messages.cfg().set(
                "messages.command.onlinerecord.usage",
                config.cfg().getStringList("messages.usage")
        );
        config.cfg().set("messages", null);

        // apply defaults, remove older keys
        plugin().getLogger().info("Migrating other miscellaneous features...");
        config.cfg().set("record-announcement.enable-log-message", true);
        config.cfg().set("compatibility.generic-vanish", true);
        config.cfg().set(
                "advanced.debugging.enabled",
                config.cfg().getBoolean("debug", false)
        );
        config.cfg().set(
                "advanced.debugging.categories",
                Collections.singletonList("*")
        );
        config.cfg().set("debug", null);
        messages.cfg().set("prefix", "&b&lOnlineRecord:&7");
        messages.cfg().set(
                "messages.new-record.log-message",
                Collections.singletonList("New online record achieved (%player-count% players online)!")
        );
        messages.cfg().set(
                "messages.command.onlinerecord.reload.finish",
                Collections.singletonList("%prefix% &cReload failed!&7 Check cosole for further details.")
        );

        final String dateStr = Instant.now()
                .atZone(ZoneId.systemDefault())
                .toString();
        config.cfg().set("metadata.migration-on", dateStr);
        messages.cfg().set("metadata.migration-on", dateStr);
        data.cfg().set("metadata.migration-on", dateStr);

        plugin().getLogger().info("Saving affected configuration files...");
        config.save();
        messages.save();
        data.save();

        // make backups again
        plugin().getLogger().info("Making additional backups of modified configuration files...");
        config.makeBackup();
        messages.makeBackup();
        data.makeBackup();

        plugin().getLogger().warning("Migration complete!");
    }

}
