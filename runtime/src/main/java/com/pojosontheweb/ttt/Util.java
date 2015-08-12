package com.pojosontheweb.ttt;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * Utilty class (exception trapping and the like)
 */
public class Util {

    public static <T> void toRtExNoResult(String msg, RunnableEx<T> code) {
        try {
            code.run();
        } catch (Throwable e) {
            if (msg != null) {
                throw new RuntimeException(msg, e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T> void toRtExNoResult(RunnableEx<T> code) {
        toRtExNoResult(null, code);
    }

    public static <T> T toRtEx(SupplierEx<T> code) {
        return toRtEx(null, code);
    }

    public static <T> T toRtEx(String msg, SupplierEx<T> code) {
        try {
            return code.get();
        } catch (Throwable e) {
            if (msg == null) {
                throw new RuntimeException(e);
            } else {
                throw new RuntimeException(msg, e);
            }
        }
    }

    @FunctionalInterface
    public interface SupplierEx<T> {
        T get() throws Throwable;
    }

    @FunctionalInterface
    public interface RunnableEx<T> {
        void run() throws Throwable;
    }

    public static  <T> List<T> withEmptyListIfNull(List<T> l) {
        if (l==null) {
            return Collections.emptyList();
        }
        return l;
    }

    public static <T> T elvis(T t, T defaultValue) {
        return t == null ? defaultValue : t;
    }

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    public static Charset getCharsetWithDefault(Charset charset) {
        return elvis(charset, CHARSET_UTF8);
    }

}
