package io.github.arcaneplugins.onlinerecord.configuration.impl.data;

import io.github.arcaneplugins.onlinerecord.OnlineRecord;
import io.github.arcaneplugins.onlinerecord.configuration.YamlWrapper;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

public final class Data extends YamlWrapper {

    private static final int LATEST_VERSION = 1;

    public Data(final OnlineRecord plugin) {
        super(plugin, "Data", "data.yml", LATEST_VERSION);
    }

    @Override
    protected void migrate() {
        fileVersionStupidityCheck();
        // intentionally do nothing until future file versions arrive
    }

    public int getCurrentRecordCount() {
        final ConfigurationSection section = cfg().getConfigurationSection("records-by-count");

        if (section == null) {
            return 0;
        }

        return section
                .getKeys(false)
                .stream()
                .map(Integer::parseInt)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }

    public void saveRecord(final int playerCount, final Map<String, String> uuidNameMap) {
        cfg().set("records-by-count." + playerCount + ".online-players", uuidNameMap);
        save();
    }

    public void saveLegacyRecord(final int playerCount, final Collection<String> usernames) {
        cfg().set("records-by-count." + playerCount + ".online-players.unknown-uuid", usernames);
        save();
    }

}
