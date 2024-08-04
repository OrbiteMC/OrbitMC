package io.github.orbitemc.impl.server.meta;

import com.google.gson.JsonObject;
import io.github.orbitemc.impl.util.OrbitConstants;
import io.github.orbitemc.impl.util.OrbitGson;
import io.github.orbitemc.server.meta.ServerConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class OrbitServerConfiguration implements ServerConfiguration {

    private final Path pluginsFolder;

    public OrbitServerConfiguration() {
        init();
        final JsonObject orbitSettings = OrbitGson.fromPath(OrbitConstants.ORBIT_CONFIG);
        pluginsFolder = OrbitConstants.getOrConstant(() -> {
            final var temp = orbitSettings.get("pluginsFolder");
            return temp == null ? null : Path.of(temp.getAsString());
        }, OrbitConstants.PLUGINS_PATH);
    }

    private void init() {
        try {
            if (Files.notExists(OrbitConstants.ORBIT_CONFIG)) {
                Files.createFile(OrbitConstants.ORBIT_CONFIG);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(OrbitConstants.ORBIT_CONFIG, StandardOpenOption.WRITE)) {
                OrbitGson.GSON.toJson(OrbitGson.GSON.fromJson(new InputStreamReader(getClass().getResourceAsStream("/orbit.json")), JsonObject.class), writer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Path pluginsFolder() {
        return this.pluginsFolder;
    }
}
