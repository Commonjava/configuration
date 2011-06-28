package org.commonjava.web.config;

import java.util.HashMap;
import java.util.Map;

public class DefaultConfigurationListener
    implements ConfigurationListener
{

    private final Map<String, ConfigurationSectionListener<?>> sectionListeners =
        new HashMap<String, ConfigurationSectionListener<?>>();

    @Override
    public Map<String, ConfigurationSectionListener<?>> getSectionListeners()
    {
        return sectionListeners;
    }

    public DefaultConfigurationListener with( final String sectionName, final ConfigurationSectionListener<?> listener )
    {
        sectionListeners.put( sectionName, listener );
        return this;
    }

    @Override
    public void configurationComplete()
        throws ConfigurationException
    {
    }
}
