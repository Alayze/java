package com.jsoniter;

import com.jsoniter.spi.Decoder;
import com.jsoniter.spi.TypeLiteral;

import java.io.IOException;
import java.lang.reflect.Array;

public class ReflectionArrayDecoder implements Decoder {

    private final Class componentType;
    private final TypeLiteral componentTypeLiteral;

    public ReflectionArrayDecoder(Class clazz) {
        componentType = clazz.getComponentType();
        componentTypeLiteral = TypeLiteral.create(componentType);
    }

    @Override
    public Object decode(JsonIterator iter) throws IOException {
        if (iter.readNull()) {
            return null;
        }
        if (!CodegenAccess.readArrayStart(iter)) {
            return Array.newInstance(componentType, 0);
        }
        Object a1 = CodegenAccess.read(iter, componentTypeLiteral);
        if (CodegenAccess.nextToken(iter) != ',') {
            Object arr = Array.newInstance(componentType, 1);
            Array.set(arr, 0, a1);
            return arr;
        }
        Object a2 = CodegenAccess.read(iter, componentTypeLiteral);
        if (CodegenAccess.nextToken(iter) != ',') {
            Object arr = Array.newInstance(componentType, 2);
            Array.set(arr, 0, a1);
            Array.set(arr, 1, a2);
            return arr;
        }
        Object a3 = CodegenAccess.read(iter, componentTypeLiteral);
        if (CodegenAccess.nextToken(iter) != ',') {
            Object arr = Array.newInstance(componentType, 3);
            Array.set(arr, 0, a1);
            Array.set(arr, 1, a2);
            Array.set(arr, 2, a3);
            return arr;
        }
        Object a4 = CodegenAccess.read(iter, componentTypeLiteral);
        Object arr = Array.newInstance(componentType, 8);
        Array.set(arr, 0, a1);
        Array.set(arr, 1, a2);
        Array.set(arr, 2, a3);
        Array.set(arr, 3, a4);
        int i = 4;
        int arrLen = 8;
        while (CodegenAccess.nextToken(iter) == ',') {
            if (i == arrLen) {
                Object newArr = Array.newInstance(componentType, 2 * arrLen);
                System.arraycopy(arr, 0, newArr, 0, arrLen);
                arr = newArr;
                arrLen = 2 * arrLen;
            }
            Array.set(arr, i++, CodegenAccess.read(iter, componentTypeLiteral));
        }
        if (i == arrLen) {
            return arr;
        }
        Object newArr = Array.newInstance(componentType, i);
        System.arraycopy(arr, 0, newArr, 0, i);
        return newArr;
    }
}