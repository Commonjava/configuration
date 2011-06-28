package org.commonjava.web.config.dotconf.fixture;

import java.util.LinkedHashMap;

public class OrderedMapEx<K, V>
    extends LinkedHashMap<K, V>
{

    private static final long serialVersionUID = 1L;

    public OrderedMapEx<K, V> with( final K key, final V value )
    {
        put( key, value );
        return this;
    }
}
