package io.github.orbitemc.impl.server;

import io.github.orbitemc.impl.plugin.OrbitPluginLoader;
import io.github.orbitemc.impl.plugin.PluginLoadGraph;
import io.github.orbitemc.impl.plugin.exception.PluginLoadException;
import io.github.orbitemc.impl.plugin.meta.OrbitPluginMeta;
import io.github.orbitemc.impl.util.Mirrored;
import io.github.orbitemc.plugin.meta.PluginMeta;
import io.github.orbitemc.server.Server;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class OrbitServer implements Server, Mirrored<MinecraftServer> {

    private final MinecraftServer mirror;

    public OrbitServer(@NotNull final MinecraftServer mirror) {
        this.mirror = mirror;
        load();
    }

    private void load() {
        loadPlugins();
    }

    private void loadPlugins() {
        final OrbitPluginLoader loader = OrbitPluginLoader.INSTANCE;
        final List<PluginMeta> loadOrder;
        final PluginLoadGraph graph = new PluginLoadGraph(OrbitPluginLoader.INSTANCE.getMetas().size());
        try {
            loadOrder = graph.generate(loader.getMetas());
        } catch (PluginLoadException e) {
            throw new IllegalStateException(e);
        }

        for (final PluginMeta meta : loadOrder) {
            loader.loadClassLoader((OrbitPluginMeta) meta);
        }
    }

    @Override
    public @NotNull MinecraftServer getMirror() {
        return this.mirror;
    }
}
