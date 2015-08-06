package com.pojosontheweb.ttt.stripes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Holds request params.
 */
public class Parameters implements Supplier<Map<String,Object>> {

    private final Map<String,Object> params;

    public Parameters() {
        this(null);
    }

    public Parameters(Map<String, Object> params) {
        this.params = Collections.unmodifiableMap(
            params == null ?
                Collections.emptyMap() :
                new HashMap<>(params)
        );
    }

    public Parameters add(String name, Object value) {
        Map<String,Object> newMap = new HashMap<>(params);
        newMap.put(name, value);
        return new Parameters(newMap);
    }

    public Object get(String name) {
        return params.get(name);
    }

    @Override
    public Map<String, Object> get() {
        return params;
    }
}
