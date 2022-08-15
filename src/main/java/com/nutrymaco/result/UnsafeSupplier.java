package com.nutrymaco.result;

@FunctionalInterface
public interface UnsafeSupplier<T> {
    T get() throws Exception;
}