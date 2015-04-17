/**
 * Copyright (C) 2011 John Casey (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.readLines;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.PropertiesBasedValueSource;
import org.codehaus.plexus.interpolation.StringSearchInterpolator;
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
        parameter = Pattern.compile( "\\s*([^#:=]+)\\s*[:=]\\s*([^#]+)(\\s*#.*)?" );
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

    public DotConfConfigurationReader( final Object... data )
        throws ConfigurationException
    {
        this( new DefaultConfigurationRegistry( data ) );
    }

    @Override
    public void loadConfiguration( final InputStream stream )
        throws ConfigurationException
    {
        loadConfiguration( stream, System.getProperties() );
    }

    @Override
    public void loadConfiguration( final InputStream stream, final Properties properties )
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

        final StringSearchInterpolator interp = new StringSearchInterpolator();
        interp.addValueSource( new PropertiesBasedValueSource( properties ) );

        String continuedKey = null;
        StringBuilder continuedVal = null;
        for ( final String line : lines )
        {
            final String trimmed = line.trim();
            if ( trimmed.startsWith( "#" ) )
            {
                continue;
            }

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
                if ( continuedKey != null )
                {
                    if ( trimmed.endsWith( "\\" ) )
                    {
                        continuedVal.append( trimmed.substring( 0, trimmed.length() - 1 ) );
                    }
                    else
                    {
                        continuedVal.append( trimmed );

                        try
                        {
                            final String value = interp.interpolate( continuedVal.toString() );
                            dispatch.parameter( sectionName, continuedKey.trim(), value.trim() );
                            continuedKey = null;
                            continuedVal = null;
                        }
                        catch ( final InterpolationException e )
                        {
                            throw new ConfigurationException( "Failed to resolve expressions in configuration '%s' (raw value: '%s'). Reason: %s", e,
                                                              continuedKey, continuedVal, e.getMessage() );
                        }
                    }
                }
                else
                {
                    final Matcher matcher = parameter.matcher( line );
                    if ( matcher.matches() )
                    {
                        final String key = matcher.group( 1 );
                        String value = matcher.group( 2 )
                                              .trim();

                        if ( value.endsWith( "\\" ) )
                        {
                            continuedKey = key;
                            continuedVal = new StringBuilder( value.substring( 0, value.length() - 1 ) );
                            continue;
                        }

                        try
                        {
                            value = interp.interpolate( value );
                        }
                        catch ( final InterpolationException e )
                        {
                            throw new ConfigurationException( "Failed to resolve expressions in configuration '%s' (raw value: '%s'). Reason: %s", e,
                                                              key, value, e.getMessage() );
                        }

                        dispatch.parameter( sectionName, key.trim(), value.trim() );
                    }
                }
            }
        }

        dispatch.sectionComplete( sectionName );
        dispatch.configurationParsed();
    }

}
