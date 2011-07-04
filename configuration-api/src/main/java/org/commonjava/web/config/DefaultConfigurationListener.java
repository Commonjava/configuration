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

import java.util.HashMap;
import java.util.Map;

import org.commonjava.web.config.annotation.SectionName;
import org.commonjava.web.config.section.BeanSectionListener;
import org.commonjava.web.config.section.ConfigurationSectionListener;
import org.commonjava.web.config.section.TypedConfigurationSectionListener;

public class DefaultConfigurationListener
    implements ConfigurationListener
{

    private final Map<String, ConfigurationSectionListener<?>> sectionListeners =
        new HashMap<String, ConfigurationSectionListener<?>>();

    public DefaultConfigurationListener()
    {
    }

    public DefaultConfigurationListener( final Class<?>... sectionTypes )
        throws ConfigurationException
    {
        for ( final Class<?> type : sectionTypes )
        {
            processSectionAnnotation( type, null );
        }
    }

    public DefaultConfigurationListener( final ConfigurationSectionListener<?>... sectionTypes )
        throws ConfigurationException
    {
        for ( final ConfigurationSectionListener<?> type : sectionTypes )
        {
            if ( type instanceof TypedConfigurationSectionListener )
            {
                final Class<?> cls = ( (TypedConfigurationSectionListener<?>) type ).getConfigurationType();
                processSectionAnnotation( cls, type );
            }
            else
            {
                throw new ConfigurationException( "Cannot automatically register section listener: %s", type );
            }
        }
    }

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    private void processSectionAnnotation( final Class cls, final ConfigurationSectionListener listener )
        throws ConfigurationException
    {
        final SectionName anno = (SectionName) cls.getAnnotation( SectionName.class );

        final String key = anno == null ? ConfigurationSectionListener.DEFAULT_SECTION : anno.value();
        if ( sectionListeners.containsKey( key ) )
        {
            throw new ConfigurationException(
                                              "Section collision! More than one ConfigurationParser bound to section: %s\n\t%s\n\t%s",
                                              key, sectionListeners.get( key ), cls.getName() );
        }

        this.sectionListeners.put( anno.value(), listener == null ? new BeanSectionListener( cls ) : listener );
    }

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

    public <T> T getConfiguration( final String sectionName, final Class<T> type )
    {
        final ConfigurationSectionListener<?> listener = sectionListeners.get( sectionName );
        return listener == null ? null : type.cast( listener.getConfiguration() );
    }
}
