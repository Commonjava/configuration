package org.commonjava.web.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DefaultConfigurationRegistry implements ConfigurationRegistry
{
    @Inject
    private Collection<ConfigurationListener> listeners;

    private Map<String, ConfigurationSectionListener<?>> sectionMap;

    protected DefaultConfigurationRegistry()
    {
    }

    public DefaultConfigurationRegistry( final ConfigurationListener... listeners )
        throws ConfigurationException
    {
        this.listeners = Arrays.asList( listeners );
        mapSectionListeners();
    }

    @Override
    public void configurationParsed()
        throws ConfigurationException
    {
        if ( listeners != null )
        {
            for ( final ConfigurationListener listener : listeners )
            {
                listener.configurationComplete();
            }
        }
        else
        {
            // TODO: Log to debug level!
        }
    }

    @Override
    public boolean sectionStarted( final String name )
        throws ConfigurationException
    {
        final ConfigurationSectionListener<?> listener = sectionMap.get( name );
        if ( listener != null )
        {
            listener.sectionStarted( name );
            return true;
        }

        return false;
    }

    @Override
    public void sectionComplete( final String name )
        throws ConfigurationException
    {
        final ConfigurationSectionListener<?> listener = sectionMap.get( name );
        if ( listener != null )
        {
            listener.sectionComplete( name );
        }
    }

    @Override
    public void parameter( final String section, final String name, final String value )
        throws ConfigurationException
    {
        final ConfigurationSectionListener<?> secListener = sectionMap.get( section );
        secListener.parameter( name, value );
    }

    @PostConstruct
    protected void mapSectionListeners()
        throws ConfigurationException
    {
        sectionMap = new HashMap<String, ConfigurationSectionListener<?>>();

        if ( listeners != null )
        {
            for ( final ConfigurationListener listener : listeners )
            {
                final Map<String, ConfigurationSectionListener<?>> parsers = listener.getSectionListeners();
                for ( final Map.Entry<String, ConfigurationSectionListener<?>> entry : parsers.entrySet() )
                {
                    final String section = entry.getKey();
                    if ( sectionMap.containsKey( section ) )
                    {
                        throw new ConfigurationException(
                                                          "Section collision! More than one ConfigurationParser bound to section: %s\n\t%s\n\t%s",
                                                          section, sectionMap.get( section ), entry.getValue() );
                    }

                    sectionMap.put( section, entry.getValue() );
                }
            }
        }
        else
        {
            // TODO: Log to debug level!
        }
    }

}
