package io.github.orbitemc.impl.plugin.meta;

import io.github.orbitemc.plugin.meta.PluginDependency;
import org.jetbrains.annotations.NotNull;

public record OrbitPluginDependency(@NotNull String name, boolean required, boolean loadBefore, boolean mergeClasspath) implements PluginDependency {
}
