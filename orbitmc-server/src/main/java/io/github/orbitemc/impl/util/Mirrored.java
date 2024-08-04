package io.github.orbitemc.impl.util;

import org.jetbrains.annotations.NotNull;

public interface Mirrored<M> {
    @NotNull
    M getMirror();
}
