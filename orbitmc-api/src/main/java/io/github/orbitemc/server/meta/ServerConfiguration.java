package io.github.orbitemc.server.meta;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Represents read only settings defined in the orbit.json file.
 *
 * @since 1.21
 */
@ApiStatus.AvailableSince("1.21")
public interface ServerConfiguration {
    /**
     * Gets the folder plugins are stored within
     *
     * @return the path for pluginsFolder
     */
    @ApiStatus.AvailableSince("1.21")
    @NotNull
    Path pluginsFolder();
}
