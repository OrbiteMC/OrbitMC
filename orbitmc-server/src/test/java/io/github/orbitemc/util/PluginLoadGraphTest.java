package io.github.orbitemc.util;

import io.github.orbitemc.impl.plugin.meta.OrbitPluginDependency;
import io.github.orbitemc.impl.plugin.PluginLoadGraph;
import io.github.orbitemc.impl.plugin.exception.PluginLoadException;
import io.github.orbitemc.plugin.meta.PluginMeta;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.orbitemc.testutil.PluginMetaHelper.dependency;
import static io.github.orbitemc.testutil.PluginMetaHelper.from;
import static io.github.orbitemc.testutil.PluginMetaHelper.named;
import static io.github.orbitemc.testutil.PluginMetaHelper.random;
import static org.junit.jupiter.api.Assertions.*;

public class PluginLoadGraphTest {

    private static SecureRandom RANDOM = new SecureRandom();

    @Test
    void test_should_Load_No_Depends() {
        final Map<String, PluginMeta> plugins = new HashMap<>();
        for (int i = 0; i < RANDOM.nextInt(25, 50); i++) {
            final var meta = random();
            plugins.put(meta.name(), meta);
        }

        final PluginLoadGraph graph = new PluginLoadGraph(plugins.size());
        assertDoesNotThrow(() -> graph.generate(plugins));
    }

    @Test
    void test_Should_Load_Valid_Depends() {
        final List<PluginMeta> metas = new ArrayList<>();
        for (int i = 0; i < RANDOM.nextInt(25, 100); i++) {
            metas.add(random());
        }
        metas.add(named("one", dependency(metas.get(0), true)));
        metas.add(named("two", dependency("one", true)));
        metas.add(named("three", dependency("one", true, true)));

        final Map<String, PluginMeta> plugins = from(metas);
        final PluginLoadGraph graph = new PluginLoadGraph(plugins.size());
        assertDoesNotThrow(() -> graph.generate(plugins));
    }

    @Test
    void test_should_Fail_RequiredDepGone() {
        final Map<String, PluginMeta> plugins = new HashMap<>();
        final var meta = random(dependency(random(), true));
        plugins.put(meta.name(), meta);

        final PluginLoadGraph graph = new PluginLoadGraph(plugins.size());
        assertThrows(PluginLoadException.class, () -> graph.generate(plugins));
    }

    @Test
    void test_should_Fail_SimpleCircleDepend() {
        final Map<String, PluginMeta> plugins = from(
                List.of(
                        named("one", new OrbitPluginDependency("two", true, false, false)),
                        named("two", new OrbitPluginDependency("one", true, false, false))
                )
        );

        final PluginLoadGraph graph = new PluginLoadGraph(plugins.size());
        assertThrows(PluginLoadException.class, () -> graph.generate(plugins));
    }

    @Test
    void test_should_Fail_SimpleCircleLoadBefore() {
        final Map<String, PluginMeta> plugins = from(
                List.of(
                        named("one", new OrbitPluginDependency("two", true, true, false)),
                        named("two", new OrbitPluginDependency("one", true, true, false))
                )
        );
        final PluginLoadGraph graph = new PluginLoadGraph(plugins.size());
        assertThrows(PluginLoadException.class, () -> graph.generate(plugins));
    }

    @Test
    void test_should_Fail_Complex_Circle() {
        final Map<String, PluginMeta> plugins = from(
                List.of(
                        named("one", dependency("three", true)),
                        named("two", dependency("four", true)),
                        named("three", dependency("two", true)),
                        named("four", dependency("one", true))
                )
        );
        final PluginLoadGraph graph = new PluginLoadGraph(plugins.size());
        assertThrows(PluginLoadException.class, () -> graph.generate(plugins));
    }

    @Test
    void test_Should_Fail_Complex_BackCircle() {
        final Map<String, PluginMeta> plugins = from(
                List.of(
                        named("one", dependency("three", true, true)),
                        named("two", dependency("four", true, true)),
                        named("three", dependency("two", true, true)),
                        named("four", dependency("one", true, true))
                )
        );

        final PluginLoadGraph graph = new PluginLoadGraph(plugins.size());
        assertThrows(PluginLoadException.class, () -> graph.generate(plugins));
    }
}
