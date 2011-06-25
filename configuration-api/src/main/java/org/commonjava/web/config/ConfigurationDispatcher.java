package org.commonjava.web.config;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ConfigurationDispatcher
{
    @Inject
    private Collection<ConfigurationListener> listeners;

    private Map<String, ConfigurationParser<?>> parserMap;

    public void parseSection( final String sectionName, final InputStream stream )
        throws ConfigurationException
    {
        final ConfigurationParser<?> parser = parserMap.get( sectionName );
        if ( parser != null )
        {
            parser.parse( stream );
        }
        else
        {
            // skip it.
            // TODO: log in debug level!
        }
    }

    public void configurationParsed()
        throws ConfigurationException
    {
        if ( listeners != null )
        {
            for ( final ConfigurationListener listener : listeners )
            {
                listener.configurationParsed();
            }
        }
        else
        {
            // TODO: Log to debug level!
        }
    }

    @PostConstruct
    protected void mapParsers()
        throws ConfigurationException
    {
        parserMap = new HashMap<String, ConfigurationParser<?>>();

        if ( listeners != null )
        {
            for ( final ConfigurationListener listener : listeners )
            {
                final Map<String, ConfigurationParser<?>> parsers = listener.getSectionParsers();
                for ( final Map.Entry<String, ConfigurationParser<?>> entry : parsers.entrySet() )
                {
                    final String section = entry.getKey();
                    if ( parserMap.containsKey( section ) )
                    {
                        throw new ConfigurationException(
                                                          "Section collision! More than one ConfigurationParser bound to section: %s\n\t%s\n\t%s",
                                                          section, parserMap.get( section ), entry.getValue() );
                    }

                    parserMap.put( section, entry.getValue() );
                }
            }
        }
        else
        {
            // TODO: Log to debug level!
        }
    }

}
