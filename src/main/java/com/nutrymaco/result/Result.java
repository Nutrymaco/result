package com.nutrymaco.result;

class Result<T, S> {
    private final boolean isValue;
    private final T value;
    private final ExceptionWithSource<S> exception;

    private Result(boolean isValue, T value, ExceptionWithSource<S> exception) {
        this.isValue = isValue;
        this.value = value;
        this.exception = exception;
    }

    public static <T, S> Result<T, S> value(T value) {
        return new Result<>(true, value, null);
    }

    public static <T, S> Result<T, S> exception(Exception e, S source) {
        return new Result<>(false, null, new ExceptionWithSource<>(e, source));
    }

    public static <T, S> Result<T, S> create(UnsafeSupplier<T> supplier, S source) {
        try {
            var value = supplier.get();
            return value(value);
        } catch (Exception e) {
            return exception(e, source);
        }
    }

    public static <S> Result<Void, S> create(UnsafeRunnable runnable, S source) {
        try {
            runnable.run();
            return value(null);
        } catch (Exception e) {
            return exception(e, source);
        }
    }

    public T value() {
        return value;
    }

    public ExceptionWithSource<S> exception() {
        return exception;
    }

    public boolean isValue() {
        return isValue;
    }

    public boolean isException() {
        return !isValue;
    }

    public Result<Void, S> toVoid() {
        return new Result<>(isValue, null, exception);
    }

}