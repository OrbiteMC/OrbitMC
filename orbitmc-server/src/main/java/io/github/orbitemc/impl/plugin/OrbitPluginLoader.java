package io.github.orbitemc.impl.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.orbitemc.impl.plugin.meta.OrbitPluginApiVersion;
import io.github.orbitemc.impl.plugin.meta.OrbitPluginDependency;
import io.github.orbitemc.impl.plugin.meta.OrbitPluginMeta;
import io.github.orbitemc.impl.util.OrbitGson;
import io.github.orbitemc.impl.util.function.SupplyingSilencer;
import io.github.orbitemc.plugin.exception.InvalidPluginMetaException;
import io.github.orbitemc.plugin.meta.PluginApiVersion;
import io.github.orbitemc.plugin.meta.PluginDependency;
import io.github.orbitemc.plugin.meta.PluginMeta;
import io.github.orbitemc.plugin.type.StandardPlugin;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;
import java.util.zip.ZipFile;

public class OrbitPluginLoader {

    public static final OrbitPluginLoader INSTANCE = new OrbitPluginLoader();

    private final Map<String, PluginMeta> metas = new HashMap<>();
    private final List<OrbitPluginClassLoader> loaders = new ArrayList<>();
    private final Queue<PluginMeta> loadOrder = new ArrayDeque<>();

    private OrbitPluginLoader() {
    }

    public void loadMeta(@NotNull final Path jarFile) throws InvalidPluginMetaException {
        try (final ZipFile zip = new ZipFile(jarFile.toFile())) {
            final JsonObject object = OrbitGson.GSON.fromJson(new InputStreamReader(zip.getInputStream(zip.getEntry("plugin.json"))), JsonObject.class);

            final String name = getStringField(object, "name", jarFile);
            final String main = getStringField(object, "main", jarFile);
            final String version = getStringField(object, "version", jarFile);
            final String author = getStringField(object, "author", jarFile);
            final List<String> contributors = getListField(object, "contributors", JsonElement::getAsString);
            final PluginApiVersion apiVersion = new OrbitPluginApiVersion(getStringField(object, "apiVersion", jarFile));
            final ImmutableSet<PluginDependency> dependencies = ImmutableSet.copyOf(getListField(object, "dependencies", (element) -> {
                final JsonObject dependencyObject = element.getAsJsonObject();
                if (!dependencyObject.has("name")) {
                    throw new InvalidPluginMetaException("Declared dependency within jarFile %s has no field name".formatted(jarFile));
                }
                final boolean required;
                if (dependencyObject.has("required")) {
                    required = dependencyObject.get("required").getAsBoolean();
                } else required = true;
                final boolean loadBefore;
                if (dependencyObject.has("loadBefore")) {
                    loadBefore = dependencyObject.get("loadBefore").getAsBoolean();
                } else loadBefore = false;
                final boolean mergeClasspath;
                if (dependencyObject.has("mergeClasspath")) {
                    mergeClasspath = dependencyObject.get("mergeClasspath").getAsBoolean();
                } else mergeClasspath = true;
                return new OrbitPluginDependency(dependencyObject.get("name").getAsString(), required, loadBefore, mergeClasspath);
            }));
            LogManager.getLogger().info("Loaded %s from %s".formatted(name, jarFile));
            metas.put(name, new OrbitPluginMeta(jarFile, name, main, version, author, contributors, apiVersion, dependencies));
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public void loadClassLoader(@NotNull final OrbitPluginMeta meta) {
        final OrbitPluginClassLoader loader = OrbitPluginClassLoader.create(meta.pluginFile(), getClass().getClassLoader(), this, meta);
        meta.mainClass((Class<? extends StandardPlugin>) SupplyingSilencer.silence(() -> Class.forName(meta.main(), true, loader)));
        try {
            Constructor<? extends StandardPlugin> constructor = meta.mainClass().getDeclaredConstructor();
            loader.setStandardPlugin(constructor.newInstance());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    Class<?> getClassByName(@NotNull final String name, final boolean resolve, @NotNull final PluginMeta meta) {
        for (final OrbitPluginClassLoader loader : loaders) {
            try {
                return loader.loadClass(name, resolve, loader.getPluginMeta().getDependency(meta.name()).mergeClasspath());
            } catch (ClassNotFoundException ignored) {
            }
        }
        return null;
    }

    public Map<String, PluginMeta> getMetas() {
        return ImmutableMap.copyOf(this.metas);
    }

    public List<OrbitPluginClassLoader> getLoaders() {
        return ImmutableList.copyOf(this.loaders);
    }

    private String getStringField(JsonObject object, String field, Path jarFile) throws InvalidPluginMetaException {
        if (!object.has(field)) {
            throw new InvalidPluginMetaException(jarFile, field);
        }

        return object.get(field).getAsString();
    }

    private <T> List<T> getListField(JsonObject object, String field, Function<JsonElement, T> mapper) {
        if (!object.has(field)) {
            return new ArrayList<>();
        }

        return object.getAsJsonArray(field).asList().stream().map(mapper).toList();
    }
}
