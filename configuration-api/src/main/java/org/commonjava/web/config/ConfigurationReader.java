package org.commonjava.web.config;

import java.io.InputStream;

public interface ConfigurationReader
{

    void loadConfiguration( InputStream stream )
        throws ConfigurationException;
}
