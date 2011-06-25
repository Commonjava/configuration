package org.commonjava.web.config;

import java.io.InputStream;

public interface ConfigurationParser<T>
{

    String getContentType();

    void parse( InputStream stream )
        throws ConfigurationException;

    T getConfiguration();

}
