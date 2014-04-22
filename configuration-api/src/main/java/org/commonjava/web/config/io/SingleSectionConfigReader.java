/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.commonjava.web.config.io;

import static org.commonjava.web.config.section.ConfigurationSectionListener.DEFAULT_SECTION;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Named;

import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.PropertiesBasedValueSource;
import org.codehaus.plexus.interpolation.StringSearchInterpolator;
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
        loadConfiguration( stream, System.getProperties() );
    }

    @Override
    public void loadConfiguration( final InputStream stream, final Properties properties )
        throws ConfigurationException
    {
        final Properties props = new Properties();
        try
        {
            props.load( stream );
        }
        catch ( final IOException e )
        {
            throw new ConfigurationException( "Failed to read configuration. Error: %s", e, e.getMessage() );
        }

        if ( !dispatch.sectionStarted( DEFAULT_SECTION ) )
        {
            return;
        }

        final StringSearchInterpolator interp = new StringSearchInterpolator();
        interp.addValueSource( new PropertiesBasedValueSource( properties ) );

        for ( final Object k : props.keySet() )
        {
            final String key = (String) k;
            String value = props.getProperty( key );
            try
            {
                value = interp.interpolate( value );
            }
            catch ( final InterpolationException e )
            {
                throw new ConfigurationException( "Failed to resolve expressions in configuration '%s' (raw value: '%s'). Reason: %s", e, key, value,
                                                  e.getMessage() );
            }

            dispatch.parameter( DEFAULT_SECTION, key, props.getProperty( key ) );
        }

        dispatch.sectionComplete( DEFAULT_SECTION );
        dispatch.configurationParsed();
    }

}
