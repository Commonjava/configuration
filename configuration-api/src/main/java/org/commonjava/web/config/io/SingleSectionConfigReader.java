package org.commonjava.web.config.io;

import static org.commonjava.web.config.section.ConfigurationSectionListener.DEFAULT_SECTION;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Named;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.ConfigurationReader;
import org.commonjava.web.config.ConfigurationRegistry;
import org.commonjava.web.config.DefaultConfigurationRegistry;
import org.commonjava.web.config.section.ConfigurationSectionListener;

@Named( "single-section" )
public class SingleSectionConfigReader
    implements ConfigurationReader
{

    private final ConfigurationRegistry dispatch;

    public SingleSectionConfigReader( final ConfigurationSectionListener<?> listener )
        throws ConfigurationException
    {
        dispatch = new DefaultConfigurationRegistry( listener );
    }

    public SingleSectionConfigReader( final Object target )
        throws ConfigurationException
    {
        dispatch = new DefaultConfigurationRegistry( target );
    }

    @Override
    public void loadConfiguration( final InputStream stream )
        throws ConfigurationException
    {
        Properties props = new Properties();
        try
        {
            props.load( stream );
        }
        catch ( final IOException e )
        {
            throw new ConfigurationException( "Failed to read configuration. Error: %s", e,
                                              e.getMessage() );
        }

        if ( !dispatch.sectionStarted( DEFAULT_SECTION ) )
        {
            return;
        }

        for ( final Object k : props.keySet() )
        {
            String key = (String) k;
            dispatch.parameter( DEFAULT_SECTION, key, props.getProperty( key ) );
        }

        dispatch.sectionComplete( DEFAULT_SECTION );
        dispatch.configurationParsed();
    }

}
