package io.github.orbitemc.impl.util.collection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UniqueGraphNode<T> {

    private final T value;
    private final @NotNull List<UniqueGraphNode<T>> children = new ArrayList<>();

    public UniqueGraphNode(@NotNull final T value) {
        this.value = value;
    }

    @NotNull
    public UniqueGraphNode<T> child(int index) {
        return children.get(index);
    }

    public void child(UniqueGraphNode<T> node) {
        if (node == this) throw new IllegalStateException("Can not have node be child of itself");
        children.add(node);
    }

    public int children() {
        return children.size();
    }

    @NotNull
    public Set<UniqueGraphNode<T>> collectRelatives() {
        final Set<UniqueGraphNode<T>> relatives = new LinkedHashSet<>();

        for (final UniqueGraphNode<T> child : children) {
            relatives.addAll(child.collectRelatives());
        }

        return relatives;
    }

    public boolean hasDirectChild(UniqueGraphNode<T> node) {
        return children.contains(node);
    }

    public boolean hasChild(UniqueGraphNode<T> node) {
        if (node == this) throw new IllegalStateException("Can not have node be child of itself");
        if (children.contains(node)) return true;

        for (final UniqueGraphNode<T> child : children) {
            if (child.hasChild(node)) return true;
        }

        return false;
    }

    public boolean isChildless() {
        return this.children.isEmpty();
    }

    @NotNull
    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final UniqueGraphNode<?> that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[").append(value).append("]").append("->");
        for (final UniqueGraphNode<T> child : children) {
            builder.append("[").append(child.getValue()).append("]");
        }
        return builder.toString();
    }
}

