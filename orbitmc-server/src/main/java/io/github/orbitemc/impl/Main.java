package io.github.orbitemc.impl;

import io.github.orbitemc.Orbit;
import io.github.orbitemc.impl.plugin.OrbitPluginLoader;
import io.github.orbitemc.impl.server.meta.OrbitServerConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        setupOrbit();
        loadPlugins(Orbit.getServerConfiguration().pluginsFolder());
        net.minecraft.server.Main.main(args);
    }

    private static void setupOrbit() {
        MetaOrbit.setServerConfiguration(new OrbitServerConfiguration());
    }

    private static void loadPlugins(@NotNull final Path folder) {
        try {
            if (Files.notExists(folder)) {
                Files.createDirectories(folder);
            }
            try (Stream<Path> files = Files.list(folder)) {
                files.filter((path) -> path.getFileName().toString().endsWith(".jar")).forEach(OrbitPluginLoader.INSTANCE::loadMeta);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
