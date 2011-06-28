package org.commonjava.web.config.section;

import java.util.LinkedHashMap;
import java.util.Map;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.ConfigurationSectionListener;

public class MapSectionListener
    implements ConfigurationSectionListener<Map<String, String>>
{

    private Map<String, String> parameters;

    @Override
    public void sectionStarted( final String name )
        throws ConfigurationException
    {
        parameters = new LinkedHashMap<String, String>();
    }

    @Override
    public void parameter( final String name, final String value )
        throws ConfigurationException
    {
        parameters.put( name, value );
    }

    @Override
    public void sectionComplete( final String name )
        throws ConfigurationException
    {
        // NOP.
    }

    @Override
    public Map<String, String> getConfiguration()
    {
        return parameters;
    }

}
