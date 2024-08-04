package io.github.orbitemc;

import io.github.orbitemc.server.meta.ServerConfiguration;
import org.jetbrains.annotations.ApiStatus;

/**
 * This class is the main entrypoint for OrbitMC. It provides access to methods that can be accessed from anywhere
 * safely without exception.
 *
 * @since 1.21
 */
@ApiStatus.AvailableSince("1.21")
public final class Orbit {
    private static ServerConfiguration serverConfiguration;

    /**
     * Gets the server configuration.
     *
     * @return the {@link ServerConfiguration}
     * @see ServerConfiguration
     */
    public static ServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }

}
