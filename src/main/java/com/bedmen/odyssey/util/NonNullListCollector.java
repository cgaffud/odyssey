package com.bedmen.odyssey.util;

import net.minecraft.core.NonNullList;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class NonNullListCollector<T> implements Collector<T, NonNullList<T>, NonNullList<T>> {
    public BiConsumer<NonNullList<T>, T> accumulator() {
        return NonNullList::add;
    }

    public Supplier<NonNullList<T>> supplier() {
        return NonNullList::create;
    }

    public BinaryOperator<NonNullList<T>> combiner() {
        return (nonNullList1, nonNullList2) -> {
            nonNullList1.addAll(nonNullList2);
            return nonNullList1;
        };
    }

    public Function<NonNullList<T>, NonNullList<T>> finisher() {
        return nonNullList -> nonNullList;
    }

    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
    }
}
