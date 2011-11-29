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
package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.readLines;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.ConfigurationListener;
import org.commonjava.web.config.ConfigurationReader;
import org.commonjava.web.config.ConfigurationRegistry;
import org.commonjava.web.config.DefaultConfigurationListener;
import org.commonjava.web.config.DefaultConfigurationRegistry;
import org.commonjava.web.config.section.ConfigurationSectionListener;

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
        this(
              new DefaultConfigurationRegistry( new DefaultConfigurationListener( sectionListeners ) ) );
    }

    public DotConfConfigurationReader( final ConfigurationListener... listeners )
        throws ConfigurationException
    {
        this( new DefaultConfigurationRegistry( listeners ) );
    }

    public DotConfConfigurationReader( final Object... data )
        throws ConfigurationException
    {
        this( new DefaultConfigurationRegistry( data ) );
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
            throw new ConfigurationException( "Failed to read configuration. Error: %s", e,
                                              e.getMessage() );
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
