package io.github.orbitemc.impl;

import io.github.orbitemc.Orbit;
import io.github.orbitemc.server.meta.ServerConfiguration;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public final class MetaOrbit {

    private static final MethodHandles.Lookup lookup;
    private static final MethodHandle OrbitFieldServerConfiguration;

    static {
        try {
            lookup = MethodHandles.privateLookupIn(Orbit.class, MethodHandles.lookup());
            MethodHandles.privateLookupIn(Orbit.class, lookup);
            OrbitFieldServerConfiguration = lookup.findStaticSetter(Orbit.class, "serverConfiguration", ServerConfiguration.class);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private MetaOrbit() {
        throw new UnsupportedOperationException("Can not initialize utility class");
    }

    public static void setServerConfiguration(ServerConfiguration configuration) {
        try {
            OrbitFieldServerConfiguration.invoke(configuration);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
