package com.nutrymaco.result;

public record ExceptionWithSource<T>(Exception e, T source) {
}