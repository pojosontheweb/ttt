package com.pojosontheweb.ttt;

public class Util {

    public static <T> T toRtEx(SupplierEx<T> code) {
        try {
            return code.get();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface SupplierEx<T> {
        T get() throws Exception;
    }

}
