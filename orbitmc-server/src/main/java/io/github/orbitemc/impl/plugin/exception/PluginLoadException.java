package io.github.orbitemc.impl.plugin.exception;

public class PluginLoadException extends Exception {

    public PluginLoadException(String message) {
        super(message);
    }

    public PluginLoadException(String message, Exception exception) {
        super(message, exception);
    }

}
