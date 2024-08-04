package io.github.orbitemc.testutil;

import io.github.orbitemc.impl.plugin.meta.OrbitPluginDependency;
import io.github.orbitemc.plugin.meta.PluginApiVersion;
import io.github.orbitemc.plugin.meta.PluginDependency;
import io.github.orbitemc.plugin.meta.PluginMeta;
import io.github.orbitemc.plugin.type.StandardPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class PluginMetaHelper {

    private static int creationId = 0;

    public static Map<String, PluginMeta> from(Collection<PluginMeta> metas) {
        return metas.stream().collect(Collectors.toMap(PluginMeta::name, (a) -> a));
    }

    public static PluginDependency dependency(PluginMeta meta, boolean required) {
        return new OrbitPluginDependency(meta.name(), required, false, true);
    }

    public static PluginDependency dependency(String name, boolean required) {
        return new OrbitPluginDependency(name, required, false, true);
    }

    public static PluginDependency dependency(String name, boolean required, boolean loadBefore) {
        return new OrbitPluginDependency(name, required, loadBefore, true);
    }

    public static PluginMeta random(PluginDependency... dependencies) {
        return new PluginMetaMock(creationId++ + "", Arrays.stream(dependencies).collect(Collectors.toSet()));
    }

    public static PluginMeta random() {
        return random(new PluginDependency[0]);
    }

    public static PluginMeta named(String name, PluginDependency... dependencies) {
        return new PluginMetaMock(name, Arrays.stream(dependencies).collect(Collectors.toSet()));
    }

    public static PluginMeta named(String name) {
        return named(name, new PluginDependency[0]);
    }

    private static class PluginMetaMock implements PluginMeta {

        private final String name;
        private final Set<PluginDependency> dependencies;

        public PluginMetaMock(String name, Set<PluginDependency> dependencies) {
            this.name = name;
            this.dependencies = dependencies;
        }

        @Override
        public @NotNull String name() {
            return this.name;
        }

        @Override
        public @NotNull String main() {
            throw new UnsupportedOperationException("Can not do this with mock");
        }

        @Override
        public @Nullable Class<? extends StandardPlugin> mainClass() {
            throw new UnsupportedOperationException("Can not do this with mock");
        }

        @Override
        public @NotNull String version() {
            throw new UnsupportedOperationException("Can not do this with mock");
        }

        @Override
        public @NotNull String author() {
            throw new UnsupportedOperationException("Can not do this with mock");
        }

        @Override
        public @NotNull List<String> contributors() {
            throw new UnsupportedOperationException("Can not do this with mock");
        }

        @Override
        public @NotNull PluginApiVersion apiVersion() {
            throw new UnsupportedOperationException("Can not do this with mock");
        }

        @Override
        public @NotNull Set<PluginDependency> dependencies() {
            return this.dependencies;
        }

        @Override
        public @Nullable PluginDependency getDependency(final @NotNull String name) {
            throw new UnsupportedOperationException("Can not do this with mock");
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof final PluginMetaMock that)) return false;
            return Objects.equals(name, that.name) && Objects.equals(dependencies, that.dependencies);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, dependencies);
        }

        @Override
        public String toString() {
            return "PluginMetaMock[name=%s]".formatted(name);
        }
    }

}
