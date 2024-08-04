package io.github.orbitemc.impl.plugin.meta;

import io.github.orbitemc.plugin.meta.PluginApiVersion;
import org.jetbrains.annotations.NotNull;

public record OrbitPluginApiVersion(@NotNull String version) implements PluginApiVersion {
}
