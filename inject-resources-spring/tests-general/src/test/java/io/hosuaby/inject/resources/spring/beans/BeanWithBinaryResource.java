package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.BinaryResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanWithBinaryResource {

    @BinaryResource("/io/hosuaby/fibonacci.bin")
    private static byte[] fibonacciClassField;

    @BinaryResource("/io/hosuaby/fibonacci.bin")
    private byte[] fibonacciInstanceField;

    private byte[] fibonacciAutowiredArgument;

    private byte[] fibonacciAutowiredInConstructor;

    @Autowired
    public BeanWithBinaryResource(
            @BinaryResource("/io/hosuaby/fibonacci.bin")
            byte[] fibonacci) {
        this.fibonacciAutowiredInConstructor = fibonacci;
    }

    @BinaryResource("/io/hosuaby/fibonacci.bin")
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
