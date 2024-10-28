package io.github.arcaneplugins.onlinerecord.configuration.impl.config;

import io.github.arcaneplugins.onlinerecord.OnlineRecord;
import io.github.arcaneplugins.onlinerecord.configuration.YamlWrapper;
import io.github.arcaneplugins.onlinerecord.misc.DebugCategory;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

public final class Config extends YamlWrapper {

    private static final int LATEST_VERSION = 2;

    // only populated if debugging is enabled.
    private final EnumSet<DebugCategory> debugCategories = EnumSet.noneOf(DebugCategory.class);

    public Config(
            @NotNull final OnlineRecord plugin
    ) {
        super(plugin, "Config", "config.yml", LATEST_VERSION);
    }

    @Override
    protected void postLoad() {
        debugCategories().clear();
        final List<String> catsStr = debuggingCategoriesAsStr();
        if (catsStr.contains("ALL") || catsStr.contains("*")) {
            this.debugCategories.addAll(EnumSet.allOf(DebugCategory.class));
        } else if (isDebuggingEnabled()) {
            for (final String catStr : catsStr) {
                try {
                    this.debugCategories.add(DebugCategory.valueOf(catStr.toUpperCase(Locale.ROOT)));
                } catch (IllegalArgumentException ignored) {
                    plugin().getLogger().warning("Unrecognised debug category '" + catStr + "'.");
                }
            }
        }
    }

    @Override
    protected void migrate() {
        fileVersionStupidityCheck();
        // intentionally do nothing until future file versions arrive
    }

    public boolean isRecordAnnouncementChatMessageEnabled() {
        return cfg().getBoolean("record-announcement.enable-chat-message", true);
    }

    public boolean isRecordAnnouncementLogMessageEnabled() {
        return cfg().getBoolean("record-announcement.enable-log-message", true);
    }

    public boolean isDebuggingEnabled() {
        return cfg().getBoolean("advanced.debugging.enabled");
    }

    private List<String> debuggingCategoriesAsStr() {
        return cfg().getStringList("advanced.debugging.categories");
    }

    @NotNull
    private EnumSet<DebugCategory> debugCategories() {
        return debugCategories;
    }

    public boolean isDebugCategoryEnabled(final @NotNull DebugCategory cat) {
        return debugCategories().contains(cat);
    }

    public boolean isGenericVanishCompatibilityEnabled() {
        return cfg().getBoolean("compatibility.generic-vanish");
    }

    // Determine if `config.yml` from OnlineRecord v1.x is being used.
    public boolean isLegacyConfigFile() {
        // We can determine if a legacy config is being used by checking the existence of these paths
        return cfg().contains("debug", true) &&
                cfg().contains("data", true) &&
                cfg().contains("messages", true) &&
                !cfg().contains("metadata", true);
    }

}
