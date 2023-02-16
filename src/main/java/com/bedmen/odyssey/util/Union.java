package com.bedmen.odyssey.util;

import net.minecraft.network.FriendlyByteBuf;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Union<A,B> {

    private final A firstValue;
    private final B secondValue;

    public Union(Class<A> classA, Class<B> classB, @NonNull Object object){
        if(!classA.isInstance(object) && !classB.isInstance(object)){
            throw new IllegalArgumentException("Object "+ object +" was not an instance of either class "+ classA.getName() +" or class "+ classB.getName());
        }
        if(classA.isInstance(object)){
            this.firstValue = classA.cast(object);
            this.secondValue = null;
        } else {
            this.firstValue = null;
            this.secondValue = classB.cast(object);
        }
    }

    public boolean valueIsFirstType(){
        return this.firstValue != null;
    }

    public boolean valueIsSecondType(){
        return this.secondValue != null;
    }

    public Optional<A> getFirstTypeValueAsOptional(){
        return this.firstValue == null ? Optional.empty() : Optional.of(this.firstValue);
    }

    public Optional<B> getSecondTypeValueAsOptional(){
        return this.secondValue == null ? Optional.empty() : Optional.of(this.secondValue);
    }

    public A getFirstTypeValue(){
        return this.firstValue;
    }

    public B getSecondTypeValue(){
        return this.secondValue;
    }

    public void caseOnType(Consumer<A> firstTypeConsumer, Consumer<B> secondTypeConsumer){
        if(this.valueIsFirstType()){
            firstTypeConsumer.accept(this.firstValue);
        } else {
            secondTypeConsumer.accept(this.secondValue);
        }
    }

    public void toNetwork(FriendlyByteBuf friendlyByteBuf, BiConsumer<FriendlyByteBuf, A> firstTypeToNetwork, BiConsumer<FriendlyByteBuf, B> secondTypeToNetwork){
        friendlyByteBuf.writeBoolean(this.valueIsFirstType());
        this.caseOnType(
                firstValue -> firstTypeToNetwork.accept(friendlyByteBuf, this.firstValue),
                secondValue -> secondTypeToNetwork.accept(friendlyByteBuf, this.secondValue)
        );
    }

    public static <A,B> Union<A, B> fromNetwork(Class<A> classA, Class<B> classB, FriendlyByteBuf friendlyByteBuf, Function<FriendlyByteBuf, A> firstTypeFromNetwork, Function<FriendlyByteBuf, B> secondTypeFromNetwork){
        boolean firstType = friendlyByteBuf.readBoolean();
        if(firstType){
            return new Union<>(classA, classB, firstTypeFromNetwork.apply(friendlyByteBuf));
        } else {
            return new Union<>(classA, classB, secondTypeFromNetwork.apply(friendlyByteBuf));
        }
    }
}
