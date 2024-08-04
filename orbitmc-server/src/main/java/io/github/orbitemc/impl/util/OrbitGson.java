package io.github.orbitemc.impl.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class OrbitGson {
    public static final Gson GSON = new GsonBuilder().create();

    private OrbitGson() {
        throw new UnsupportedOperationException("Can not initialize utility class");
    }

    public static JsonObject fromPath(@NotNull final Path path) {
        try {
            return GSON.fromJson(Files.newBufferedReader(path), JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
