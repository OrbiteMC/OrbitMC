package io.github.orbitemc.plugin.type;

import io.github.orbitemc.server.Server;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A standard plugin loads after the world has fully loaded and all features are accessible.
 *
 * @since 1.21
 */
@ApiStatus.OverrideOnly
@ApiStatus.AvailableSince("1.21")
public interface StandardPlugin {

    /**
     * Initializes the standard plugin.
     *
     * @param server the server instance at time of initialization
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    void initialize(@NotNull Server server);

    /**
     * Disables the standard plugin.
     *
     * @param server the server instance at time of disabling
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    void disable(@NotNull Server server);
}
