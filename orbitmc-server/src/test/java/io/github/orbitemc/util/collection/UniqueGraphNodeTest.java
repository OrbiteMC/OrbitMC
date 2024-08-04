package io.github.orbitemc.util.collection;

import io.github.orbitemc.impl.util.collection.UniqueGraphNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UniqueGraphNodeTest {

    private static int index = 0;

    @AfterEach
    void teardown() {
        index = 0;
    }

    @Test
    public void test_Should_Know_Children() {
        final var zero = next();
        final var one = next();
        final var two = next();

        zero.child(one);
        one.child(two);

        assertTrue(zero.hasChild(one));
        assertTrue(zero.hasChild(two));
        assertFalse(zero.hasDirectChild(two));
    }

    @Test
    public void test_Should_Know_Circular_Graph() {
        final var zero = next();
        final var one = next();
        final var two = next();

        zero.child(one);
        one.child(two);
        two.child(zero);

        assertTrue(zero.hasChild(two));
        assertTrue(two.hasChild(zero));
    }

    private static UniqueGraphNode<Integer> next() {
        return new UniqueGraphNode<>(index++);
    }

}
