package com.adelean.inject.resources.spring.beans;

import com.adelean.inject.resources.spring.BinaryResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanWithBinaryResource {

    @BinaryResource("/com/adelean/junit/jupiter/fibonacci.bin")
    private static byte[] fibonacciClassField;

    @BinaryResource("/com/adelean/junit/jupiter/fibonacci.bin")
    private byte[] fibonacciInstanceField;

    private byte[] fibonacciAutowiredArgument;

    private byte[] fibonacciAutowiredInConstructor;

    @Autowired
    public BeanWithBinaryResource(
            @BinaryResource("/com/adelean/junit/jupiter/fibonacci.bin")
            byte[] fibonacci) {
        this.fibonacciAutowiredInConstructor = fibonacci;
    }

    @BinaryResource("/com/adelean/junit/jupiter/fibonacci.bin")
    public void setFibonacciAutowiredArgument(byte[] fibonacciAutowiredArgument) {
        this.fibonacciAutowiredArgument = fibonacciAutowiredArgument;
    }

    public static byte[] getFibonacciClassField() {
        return fibonacciClassField;
    }

    public byte[] getFibonacciInstanceField() {
        return fibonacciInstanceField;
    }

    public byte[] getFibonacciAutowiredArgument() {
        return fibonacciAutowiredArgument;
    }

    public byte[] getFibonacciAutowiredInConstructor() {
        return fibonacciAutowiredInConstructor;
    }
}
