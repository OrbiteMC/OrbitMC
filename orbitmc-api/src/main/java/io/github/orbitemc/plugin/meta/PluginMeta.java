package io.github.orbitemc.plugin.meta;

import io.github.orbitemc.plugin.type.StandardPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Defines the meta of a plugin.
 *
 * @since 1.21
 */
@ApiStatus.AvailableSince("1.21")
public interface PluginMeta {

    /**
     * The name of the plugin.
     *
     * @return the plugin name
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    @NotNull
    String name();

    /**
     * The plugins main class in string form
     *
     * @return the main class
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    @NotNull
    String main();

    /**
     * Gets the main class of this plugin. This method can return null if the plugin's main class has not yet been
     * loaded
     *
     * @return the plugin main class
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    @Nullable
    Class<? extends StandardPlugin> mainClass();

    /**
     * The version of the plugin.
     *
     * @return the plugin version
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    @NotNull
    String version();

    /**
     * The name of the author of this plugin.
     *
     * @return the plugin author's name
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    @NotNull
    String author();

    /**
     * Gets a list of contributors of this plugin.
     *
     * @return the list of contributors
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    @NotNull
    List<String> contributors();

    /**
     * Gets the api version of the plugin.
     *
     * @return the plugin api version
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    @NotNull
    PluginApiVersion apiVersion();

    /**
     * Gets the set of dependencies of the plugin.
     *
     * @return a set of dependencies
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    @NotNull
    Set<PluginDependency> dependencies();

    /**
     * Attempts to get a dependency of this plugin from the given name.
     *
     * @param name the name of the plugin.
     * @return the the dependency or null
     * @since 1.21
     */
    @ApiStatus.AvailableSince("1.21")
    @Nullable
    PluginDependency getDependency(@NotNull final String name);
}
