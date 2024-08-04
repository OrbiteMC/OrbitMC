package io.github.orbitemc.plugin.exception;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class InvalidPluginMetaException extends IllegalStateException {

    public InvalidPluginMetaException(@NotNull final Path file, @NotNull final String invalidOrMissingField) {
        super("Unable to load plugin from jar because of invalid or missing field %s within the meta file %s".formatted(invalidOrMissingField, file.getFileName()));
    }

    public InvalidPluginMetaException(@NotNull final String string) {
        super(string);
    }
}
