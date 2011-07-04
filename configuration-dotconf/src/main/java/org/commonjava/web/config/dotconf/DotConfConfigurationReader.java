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
package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.readLines;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.ConfigurationListener;
import org.commonjava.web.config.ConfigurationReader;
import org.commonjava.web.config.ConfigurationRegistry;
import org.commonjava.web.config.DefaultConfigurationListener;
import org.commonjava.web.config.DefaultConfigurationRegistry;
import org.commonjava.web.config.section.ConfigurationSectionListener;

@Named( ".conf" )
public class DotConfConfigurationReader
    implements ConfigurationReader
{

    private final ConfigurationRegistry dispatch;

    private final Pattern parameter;

    @Inject
    public DotConfConfigurationReader( final ConfigurationRegistry dispatch )
    {
        this.dispatch = dispatch;
        parameter = Pattern.compile( "\\s*([-._a-zA-Z0-9]+)\\s*[:=]\\s*([^\\s#]+)(\\s*#.*)?" );
    }

    public DotConfConfigurationReader( final Class<?>... types )
        throws ConfigurationException
    {
        this( new DefaultConfigurationRegistry( new DefaultConfigurationListener( types ) ) );
    }

    public DotConfConfigurationReader( final ConfigurationSectionListener<?>... sectionListeners )
        throws ConfigurationException
    {
        this( new DefaultConfigurationRegistry( new DefaultConfigurationListener( sectionListeners ) ) );
    }

    public DotConfConfigurationReader( final ConfigurationListener... listeners )
        throws ConfigurationException
    {
        this( new DefaultConfigurationRegistry( listeners ) );
    }

    @Override
    public void loadConfiguration( final InputStream stream )
        throws ConfigurationException
    {
        List<String> lines;
        try
        {
            lines = readLines( stream );
        }
        catch ( final IOException e )
        {
            throw new ConfigurationException( "Failed to read configuration. Error: %s", e, e.getMessage() );
        }

        String sectionName = ConfigurationSectionListener.DEFAULT_SECTION;
        boolean processSection = dispatch.sectionStarted( sectionName );
        for ( final String line : lines )
        {
            final String trimmed = line.trim();
            if ( trimmed.startsWith( "[" ) && trimmed.endsWith( "]" ) )
            {
                if ( trimmed.length() == 2 )
                {
                    continue;
                }

                dispatch.sectionComplete( sectionName );
                sectionName = trimmed.substring( 1, trimmed.length() - 1 );
                processSection = dispatch.sectionStarted( sectionName );
            }
            else if ( processSection )
            {
                final Matcher matcher = parameter.matcher( line );
                if ( matcher.matches() )
                {
                    final String key = matcher.group( 1 );
                    final String value = matcher.group( 2 );
                    dispatch.parameter( sectionName, key, value );
                }
            }
        }

        dispatch.sectionComplete( sectionName );
        dispatch.configurationParsed();
    }

}
