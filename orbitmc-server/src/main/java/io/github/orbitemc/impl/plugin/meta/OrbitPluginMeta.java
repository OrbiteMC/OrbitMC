package io.github.orbitemc.impl.plugin.meta;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import io.github.orbitemc.plugin.meta.PluginApiVersion;
import io.github.orbitemc.plugin.meta.PluginDependency;
import io.github.orbitemc.plugin.meta.PluginMeta;
import io.github.orbitemc.plugin.type.StandardPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class OrbitPluginMeta implements PluginMeta {
    private final @NotNull Path pluginFile;
    private final @NotNull String name;
    private final @NotNull String main;
    private final @NotNull String version;
    private final @NotNull String author;
    private final @NotNull List<String> contributors;
    private final @NotNull PluginApiVersion apiVersion;
    private final @NotNull ImmutableSet<PluginDependency> dependencies;

    public OrbitPluginMeta(@NotNull Path pluginFile, @NotNull String name, @NotNull String main, @NotNull String version,
                           @NotNull String author, @NotNull List<String> contributors,
                           @NotNull PluginApiVersion apiVersion,
                           @NotNull ImmutableSet<PluginDependency> dependencies) {
        this.pluginFile = pluginFile;
        this.name = name;
        this.main = main;
        this.version = version;
        this.author = author;
        this.contributors = contributors;
        this.apiVersion = apiVersion;
        this.dependencies = dependencies;
    }

    private Class<? extends StandardPlugin> mainClass;

    public void mainClass(@NotNull final Class<? extends StandardPlugin> mainClass) {
        Preconditions.checkState(this.mainClass == null, "The given plugin mainClass is already set");
        this.mainClass = mainClass;
    }

    @Override
    public @Nullable Class<? extends StandardPlugin> mainClass() {
        return this.mainClass;
    }

    public @NotNull Path pluginFile() {
        return this.pluginFile;
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String main() {
        return main;
    }

    @Override
    public @NotNull String version() {
        return version;
    }

    @Override
    public @NotNull String author() {
        return author;
    }

    @Override
    public @NotNull List<String> contributors() {
        return contributors;
    }

    @Override
    public @NotNull PluginApiVersion apiVersion() {
        return apiVersion;
    }

    @Override
    public @NotNull ImmutableSet<PluginDependency> dependencies() {
        return dependencies;
    }

    @Override
    public @Nullable PluginDependency getDependency(final @NotNull String name) {
        for (final PluginDependency dependency : this.dependencies) {
            if (dependency.name().equals(name)) {
                return dependency;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OrbitPluginMeta) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.main, that.main) &&
                Objects.equals(this.version, that.version) &&
                Objects.equals(this.author, that.author) &&
                Objects.equals(this.contributors, that.contributors) &&
                Objects.equals(this.apiVersion, that.apiVersion) &&
                Objects.equals(this.dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, main, version, author, contributors, apiVersion, dependencies);
    }

    @Override
    public String toString() {
        return "OrbitPluginMeta[" +
                "name=" + name + ", " +
                "main=" + main + ", " +
                "version=" + version + ", " +
                "author=" + author + ", " +
                "contributors=" + contributors + ", " +
                "apiVersion=" + apiVersion + ", " +
                "dependencies=" + dependencies + ']';
    }

}
