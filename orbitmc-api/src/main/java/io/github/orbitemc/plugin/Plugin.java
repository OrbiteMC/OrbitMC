package io.github.orbitemc.plugin;

import org.jetbrains.annotations.ApiStatus;

/**
 * This is the main entrypoint and access point for any plugin. It provides a variety of startup methods with varying
 * degrees of access.
 *
 * @since 1.21
 */
@ApiStatus.AvailableSince("1.21")
public interface Plugin {

    /**
     * Called internally when this plugin is being enabled.
     * <p>
     * This method is called after the bootstrap, world loading phases. That means accessing APIs related to those
     * operations will throw exceptions.
     *
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    default void onEnable() {
    }

    /**
     * Called internally when this plugin is being disabled.
     * <p>
     * This method is called to finalize closing of all plugin operations. Any asynchronous code run within this method
     * is not guaranteed to finish before the server shuts down
     *
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    default void onDisable() {
    }
}
