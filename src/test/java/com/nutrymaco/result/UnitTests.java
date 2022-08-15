package com.nutrymaco.result;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static com.nutrymaco.result.Result.*;

public class UnitTests {

    @Test
    public void testValueCreation() {
        var result = value("value");
        assert result.isValue();
        assert !result.isException();
    }

    @Test
    public void testExceptionCreation() {
        var exception = new Exception();
        var result = exception(exception, "test-method");
        
        assert result.isException();
        assert !result.isValue();
        assert result.exception().source().equals("test-method");
        assert result.exception().e().equals(exception);
    }
    
    @Test
    public void testToVoidMapping() {
        var result = value("value");
        var voidValueOrException = result.toVoid();

        assert !Objects.equals(voidValueOrException.value(), "value");
    }
    
    @Test
    public void testValueCreationFromSupplier() {
        var result = create(() -> "value", "test-method");
        
        assert result.isValue();
        assert !result.isException();
        assert result.value().equals("value");
    }
    
    @Test
    public void testVoidValueCreationFromRunnable() {
        var voidValueOrException = create(() -> {
            System.out.println("doing some task");
        }, "test-method");
        
        assert voidValueOrException.isValue();
        assert !voidValueOrException.isException();
    }
    
    @Test
    public void testExceptionCreationFromSupplier() {
        var exception = new Exception();
        var result = create(() -> {
            throw exception;
        }, "test-method");
        
        assert result.isException();
        assert !result.isValue();
        assert result.exception().source().equals("test-method");
        assert result.exception().e().equals(exception);
    }

    @Test
    public void testExceptionCreationFromRunnable() {
        var exception = new Exception();
        var result = create((UnsafeRunnable) () -> {
            throw exception;
        }, "test-method");

        assert result.isException();
        assert !result.isValue();
        assert result.exception().source().equals("test-method");
        assert result.exception().e().equals(exception);
    }
    
}
