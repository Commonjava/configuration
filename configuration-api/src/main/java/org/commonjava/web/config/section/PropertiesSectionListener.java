package org.commonjava.web.config.section;

import java.util.Properties;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.ConfigurationSectionListener;

public class PropertiesSectionListener
    implements ConfigurationSectionListener<Properties>
{

    private Properties parameters;

    @Override
    public void sectionStarted( final String name )
        throws ConfigurationException
    {
        parameters = new Properties();
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
    public Properties getConfiguration()
    {
        return parameters;
    }

}
