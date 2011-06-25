package org.commonjava.web.config;

import java.util.Map;

public interface ConfigurationListener
{

    Map<String, ConfigurationParser<?>> getSectionParsers();

    void configurationParsed()
        throws ConfigurationException;
}
