package io.github.orbitemc.plugin.meta;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Defines Plugin Api Versions that plugins use.
 *
 * @since 1.21
 */
@ApiStatus.AvailableSince("1.21")
public interface PluginApiVersion {

    /**
     * Gets the plugin api version as a string.
     *
     * @return the plugin api version as a string
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    @NotNull
    String version();
}
