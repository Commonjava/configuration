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

public class DefaultConfigurationListener
    implements ConfigurationListener
{

    private final Map<String, ConfigurationSectionListener<?>> sectionListeners =
        new HashMap<String, ConfigurationSectionListener<?>>();

    public DefaultConfigurationListener()
    {
    }

    public DefaultConfigurationListener( final ConfigurationSectionListener<?>... sectionListeners )
    {
        for ( final ConfigurationSectionListener<?> sl : sectionListeners )
        {
            final SectionName anno = sl.getClass()
                                       .getAnnotation( SectionName.class );

            if ( anno == null )
            {
                throw new IllegalArgumentException( "No @SectionName annotation available for: " + sl.getClass()
                                                                                                     .getName() );
            }

            this.sectionListeners.put( anno.value(), sl );
        }
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
}
