package io.github.orbitemc.impl.util;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.function.Supplier;

public final class OrbitConstants {

    public static final Path ORBIT_CONFIG = Path.of("orbit.json");
    public static final Path PLUGINS_PATH = Path.of("plugins");

    private OrbitConstants() {
        throw new UnsupportedOperationException("Can not initialize utility class");
    }

    @NotNull
    public static <T> T getOrConstant(@NotNull Supplier<T> supplier, @NotNull T constant) {
        T temp = supplier.get();
        if (temp == null) {
            return constant;
        }
        return temp;
    }

}
