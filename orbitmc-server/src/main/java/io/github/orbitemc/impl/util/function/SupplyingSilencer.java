package io.github.orbitemc.impl.util.function;

import org.jetbrains.annotations.NotNull;

public interface SupplyingSilencer<R> {

    @NotNull
    R uncheck() throws Throwable;

    @NotNull
    static <R> R silence(SupplyingSilencer<R> silencer) {
        try {
            return silencer.uncheck();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
