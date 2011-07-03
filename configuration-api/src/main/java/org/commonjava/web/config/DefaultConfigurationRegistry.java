/*******************************************************************************
 * Copyright (C) 2011 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
