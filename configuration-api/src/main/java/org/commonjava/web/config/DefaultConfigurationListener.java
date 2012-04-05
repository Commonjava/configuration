/*******************************************************************************
 * Copyright 2011 John Casey
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

        final String sectionName = anno == null ? ConfigurationSectionListener.DEFAULT_SECTION : anno.value();

        this.sectionListeners.put( sectionName, listener == null ? new BeanSectionListener( cls ) : listener );
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

    public <T> T getConfiguration( final Class<T> type )
        throws ConfigurationException
    {
        final SectionName secName = type.getAnnotation( SectionName.class );
        if ( secName == null )
        {
            throw new ConfigurationException(
                                              "Cannot find @SectionName annotation for: %s. Cannot lookup configuration section.",
                                              type.getName() );
        }

        final ConfigurationSectionListener<?> listener = sectionListeners.get( secName.value() );
        final Object configuration = listener == null ? null : listener.getConfiguration();
        return configuration == null ? null : type.cast( configuration );
    }
}
