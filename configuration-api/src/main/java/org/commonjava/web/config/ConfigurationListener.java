package org.commonjava.web.config;

import java.util.Map;

public interface ConfigurationListener
{

    Map<String, ConfigurationSectionListener<?>> getSectionListeners();

    void configurationComplete()
        throws ConfigurationException;
}
